package com.jcsoft.emsystem.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.activity.MainActivity;
import com.jcsoft.emsystem.base.LocInfoBean;
import com.jcsoft.emsystem.bean.ResponseBean;
import com.jcsoft.emsystem.bean.TrackBean;
import com.jcsoft.emsystem.callback.DCommonCallback;
import com.jcsoft.emsystem.callback.DSingleDialogCallback;
import com.jcsoft.emsystem.constants.JCConstValues;
import com.jcsoft.emsystem.database.ConfigService;
import com.jcsoft.emsystem.http.DHttpUtils;
import com.jcsoft.emsystem.http.HttpConstants;
import com.jcsoft.emsystem.utils.CommonUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jimmy on 15/12/28.
 */
public class LocationFragment extends BaseFragment implements Runnable, View.OnClickListener {
    @ViewInject(R.id.map_view)
    MapView mapView;
    @ViewInject(R.id.iv_trajectory)
    ImageView trajectoryImageView;
    EditText startTimeEditText;
    EditText endTimeEditText;
    private AMap aMap;
    private UiSettings uiSettings;
    //标志位，标志已经初始化完成
    private boolean isPrepared;
    private Handler handler = new Handler();
    private LayoutInflater inflater;
    //查询轨迹时弹出框内部View
    private View dialogView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        x.view().inject(this, view);
        this.inflater = inflater;
        isPrepared = true;
        mapView.onCreate(savedInstanceState);
        init();
        //第一次定位电动车位置
        requestDatas();
        setListeners();
        return view;
    }

    private void setListeners() {
        trajectoryImageView.setOnClickListener(this);
    }

    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            uiSettings = aMap.getUiSettings();
            //不显示缩放按键
            uiSettings.setZoomControlsEnabled(false);
            //显示比例尺
            uiSettings.setScaleControlsEnabled(true);
        }
        moveToCenter("36.68445", "117.126229");
    }

    //设置地图中心点
    private void moveToCenter(String lat, String lng) {
        String centerLatLng = ConfigService.instance().getConfigValue(
                JCConstValues.S_CenterLngLat);
        if (centerLatLng.length() == 0) {
            centerLatLng = lat + "," + lng;
            ConfigService.instance().insertConfigValue(
                    JCConstValues.S_CenterLngLat, centerLatLng);
        }
        String[] latlngArr = centerLatLng.split(",");
        if (latlngArr.length < 2) {
            latlngArr[0] = lat;
            latlngArr[1] = lng;
            centerLatLng = lat + "," + lng;
            ConfigService.instance().insertConfigValue(
                    JCConstValues.S_CenterLngLat, centerLatLng);
        }
        CameraPosition cp = new CameraPosition(
                new LatLng(Double.valueOf(latlngArr[0]),
                        Double.valueOf(latlngArr[1])), 17, 0, 0);
        CameraUpdate center = CameraUpdateFactory.newCameraPosition(cp);
        aMap.moveCamera(center);
    }

    @Override
    protected void requestDatas() {
        if (!isPrepared || !isVisible || hasLoadedOnce || !isAdded()) {
            return;
        }
        RequestParams params = new RequestParams(HttpConstants.getLocInfoUrl());
        DHttpUtils.get_String((MainActivity) getActivity(), true, params, new DCommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ResponseBean<LocInfoBean> responseBean = new Gson().fromJson(result, new TypeToken<ResponseBean<LocInfoBean>>() {
                }.getType());
                if (responseBean.getCode() == 1) {
                    LocInfoBean locInfoBean = responseBean.getData();
                    if (locInfoBean.getLon() > 0 && locInfoBean.getLat() > 0) {
                        //添加障碍物
                        MarkerOptions markerOptions = new MarkerOptions();
                        LatLng position = new LatLng(locInfoBean.getLat() / 1000000.0, locInfoBean.getLon() / 1000000.0);
                        markerOptions.position(position);
                        markerOptions.title(locInfoBean.getSatelliteTime()).snippet(locInfoBean.getSpeed() + "km/h");
                        markerOptions.perspective(true);
                        if (locInfoBean.getIsOnline().equals("1")) {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ebike_online));
                        } else {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ebike_offline));
                        }
                        aMap.addMarker(markerOptions).showInfoWindow();
                        CameraUpdate cu = CameraUpdateFactory.changeLatLng(position);
                        aMap.moveCamera(cu);
                    } else {
                        showShortText("定位失败");
                    }
                } else {
                    showShortText(responseBean.getErrmsg());
                }
            }
        });
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        //每20秒刷新一次电动车位置
        handler.postDelayed(this, 1000 * 20);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        handler.removeCallbacks(this); //停止刷新
    }

    @Override
    public void run() {
        requestDatas();
        handler.postDelayed(this, 1000 * 20);// 间隔20秒
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_trajectory://轨迹
                dialogView = inflater.inflate(R.layout.view_custom_dialog_content1, null, false);
                startTimeEditText = (EditText) dialogView.findViewById(R.id.et_start_time);
                endTimeEditText = (EditText) dialogView.findViewById(R.id.et_end_time);
                //初始化开始时间和结束时间
                startTimeEditText.setText(CommonUtils.getYesterdayDateString("yyyy-MM-dd HH:mm"));
                endTimeEditText.setText(CommonUtils.getCurrentDateString("yyyy-MM-dd HH:mm"));
                CommonUtils.showCustomDialog1(getActivity(), "", dialogView, new DSingleDialogCallback() {
                    @Override
                    public void onPositiveButtonClick(String editText) {
                        // 发送命令到服务器，在获取了轨迹信息之后，展现在地图上
                        String startTime = startTimeEditText.getText().toString();
                        String endTime = endTimeEditText.getText().toString();
                        RequestParams params = new RequestParams(HttpConstants.getTrackInfoUrl(startTime, endTime));
                        DHttpUtils.get_String((MainActivity) getActivity(), true, params, new DCommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                ResponseBean<List<TrackBean>> responseBean = new Gson().fromJson(result, new TypeToken<ResponseBean<List<TrackBean>>>() {
                                }.getType());
                                if (responseBean.getCode() == 1) {
                                    if (responseBean.getData().size() <= 0) {
                                        return;
                                    }
                                    aMap.clear();
                                    String isShowNonGps = ConfigService.instance().getConfigValue(
                                            JCConstValues.S_IsShowNonGps);
                                    if (isShowNonGps == null || isShowNonGps.length() == 0) {
                                        // 系统中暂无此值，操作一次数据库
                                        ConfigService.instance().insertConfigValue(
                                                JCConstValues.S_IsShowNonGps, "1");
                                        isShowNonGps = "1";
                                    }
                                    boolean isShowNonGpsBool = isShowNonGps.equalsIgnoreCase("1");
                                    // 如果不显示基站定位，则过滤掉基站定位点
                                    List<LatLng> points = new ArrayList<LatLng>();
                                    int i = 0;
                                    int maxCount = responseBean.getData().size();
                                    for (TrackBean trackBean : responseBean.getData()) {
                                        int status = trackBean.getSourceType();
                                        boolean isGps = (status & 0x01) == 0; // 第0个二进制位 0：卫星定位；1：基站定位
                                        if (!isShowNonGpsBool && !isGps) {
                                            continue;
                                        }
                                        LatLng point = new LatLng(trackBean.getLat() / 1000000.0,
                                                trackBean.getLon() / 1000000.0);
                                        points.add(point);
                                        if (i != 0 && i < maxCount - 1) {
                                            addMarkerToMap(point,
                                                    getResources().getString(R.string.txt_track_prompt),
                                                    trackBean.getSatelliteTime(), trackBean.getSpeed(),
                                                    R.mipmap.point, false);
                                        }
//                                        if (!isGps) {
//                                            // 如果是基站定位，则在每个点上画一个红色圈
//                                            addMarkerToMap(point,
//                                                    getResources().getString(R.string.txt_track_prompt),
//                                                    trackBean.getSatelliteTime(), trackBean.getSpeed(),
//                                                    R.mipmap.point_e, false);
//                                        }
                                        i++;
                                    }
                                    if (i == 0) {
                                        // 如果所有信息点都是基站定位，则提示没有轨迹信息，并返回
                                        showShortText("您选择的时间段内只有基站定位信息，请在设置中开启显示基站定位！");
                                        return;
                                    }

                                    TrackBean start = responseBean.getData().get(0);
                                    TrackBean end = responseBean.getData().get(maxCount - 1);
                                    addMarkerToMap(points.get(0),
                                            getResources().getString(R.string.txt_start),
                                            start.getSatelliteTime(), start.getSpeed(),
                                            R.mipmap.start, true);
                                    if (maxCount > 1) {
                                        addMarkerToMap(points.get(points.size() - 1), getResources()
                                                        .getString(R.string.txt_end), end.getSatelliteTime(),
                                                end.getSpeed(), R.mipmap.end, true);
                                    }
                                    //在地图上添加轨迹实线
                                    drawTrack(points);
                                    CameraPosition cp = new CameraPosition(points.get(0), 17, 0, 0);
                                    CameraUpdate center = CameraUpdateFactory.newCameraPosition(cp);
                                    aMap.moveCamera(center);
                                } else {
                                    showShortText(responseBean.getErrmsg());
                                }
                            }
                        });

                    }
                });
                break;
        }
    }

    //在地图上添加轨迹蓝点
    private Marker addMarkerToMap(LatLng latlng, String devName, String time,
                                  double speed, int drawableId, boolean isPerspective) {
        MarkerOptions options = new MarkerOptions();
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        options.title(devName);
        options.snippet(time + "\n" + String.valueOf(speed) + "km/h");
        options.draggable(false);
        BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(drawableId);
        options.icon(bd);
        return aMap.addMarker(options);
    }
    //在地图上添加轨迹实线
    private void drawTrack(List<LatLng> points) {
        PolylineOptions options = new PolylineOptions();
        options.addAll(points);
        options.color(Color.argb(255, 47, 172, 245));
        aMap.addPolyline(options);
    }
}

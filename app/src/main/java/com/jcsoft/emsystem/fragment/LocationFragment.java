package com.jcsoft.emsystem.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import com.jcsoft.emsystem.event.RemoteLockCarEvent;
import com.jcsoft.emsystem.http.DHttpUtils;
import com.jcsoft.emsystem.http.HttpConstants;
import com.jcsoft.emsystem.utils.CommonUtils;
import com.jcsoft.emsystem.view.formview.FormTextDateTimeView;
import com.jcsoft.emsystem.view.formview.FormViewUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 卫星定位
 * Created by jimmy on 15/12/28.
 */
public class LocationFragment extends BaseFragment implements Runnable, View.OnClickListener {
    @ViewInject(R.id.map_view)
    MapView mapView;
    @ViewInject(R.id.iv_lock)
    ImageView lockImageView;
    @ViewInject(R.id.iv_trajectory)
    ImageView trajectoryImageView;
    @ViewInject(R.id.iv_fence)
    ImageView fenceImageView;
    //轨迹查询时间布局
    LinearLayout dateLayout;
    //轨迹查询开始时间
    FormTextDateTimeView startDateView;
    //轨迹查询结束时间
    FormTextDateTimeView endDateView;
    private AMap aMap;
    private UiSettings uiSettings;
    //标志位，标志已经初始化完成
    private boolean isPrepared;
    private Handler handler = new Handler();
    private boolean hasTrack;
    //车辆位置信息
    private LocInfoBean locInfoBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        x.view().inject(this, view);
        EventBus.getDefault().register(this);
        isPrepared = true;
        mapView.onCreate(savedInstanceState);
        init();
        //第一次定位电动车位置
        requestDatas();
        setListeners();
        return view;
    }

    private void setListeners() {
        lockImageView.setOnClickListener(this);
        trajectoryImageView.setOnClickListener(this);
        fenceImageView.setOnClickListener(this);
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
        if (hasTrack) {
            aMap.clear();
        }
        RequestParams params = new RequestParams(HttpConstants.getLocInfoUrl());
        DHttpUtils.get_String((MainActivity) getActivity(), true, params, new DCommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ResponseBean<LocInfoBean> responseBean = new Gson().fromJson(result, new TypeToken<ResponseBean<LocInfoBean>>() {
                }.getType());
                if (responseBean != null && responseBean.getCode() == 1) {
                    locInfoBean = responseBean.getData();
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
                        //判断锁车状态
                        if (locInfoBean.getLock().equals("1")) {
                            lockImageView.setImageResource(R.mipmap.voice_lock);
                        } else {
                            lockImageView.setImageResource(R.mipmap.voice_unlock);
                        }
                        //判断电子围栏
                        if (locInfoBean.isOpenVf()) {
                            fenceImageView.setImageResource(R.mipmap.fence);
                        } else {
                            fenceImageView.setImageResource(R.mipmap.fence);
                        }
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
        aMap.clear();
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
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void run() {
        requestDatas();
        handler.postDelayed(this, 1000 * 20);// 间隔20秒
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_lock://远程锁车
                if (locInfoBean.getLock().equals("1")) {
                    CommonUtils.showCustomDialog0(getActivity(), "提示", "你确定要解除对电动车的锁定吗？", new DSingleDialogCallback() {
                        @Override
                        public void onPositiveButtonClick(String editText) {
                            RequestParams params = new RequestParams(HttpConstants.getUnLockBikeUrl());
                            DHttpUtils.get_String((MainActivity) getActivity(), true, params, new DCommonCallback<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    ResponseBean<String> responseBean = new Gson().fromJson(result, new TypeToken<ResponseBean<String>>() {
                                    }.getType());
                                    if (responseBean != null) {
                                        showShortText(responseBean.getErrmsg());
                                        lockImageView.setEnabled(false);
                                    }
                                }
                            });
                        }
                    });
                } else {
                    CommonUtils.showCustomDialog0(getActivity(), "提示", "你确定要锁定电动车吗？", new DSingleDialogCallback() {
                        @Override
                        public void onPositiveButtonClick(String editText) {
                            RequestParams params = new RequestParams(HttpConstants.getlockBikeUrl());
                            DHttpUtils.get_String((MainActivity) getActivity(), true, params, new DCommonCallback<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    ResponseBean<String> responseBean = new Gson().fromJson(result, new TypeToken<ResponseBean<String>>() {
                                    }.getType());
                                    if (responseBean != null) {
                                        showShortText(responseBean.getErrmsg());
                                        lockImageView.setEnabled(false);
                                    }
                                }
                            });
                        }
                    });
                }
                break;
            case R.id.iv_trajectory://轨迹
                //选择时间弹出框
                initDateWindow();
                CommonUtils.showCustomDialog1(getActivity(), "", dateLayout, new DSingleDialogCallback() {
                    @Override
                    public void onPositiveButtonClick(String editText) {
                        // 发送命令到服务器，在获取了轨迹信息之后，展现在地图上
                        String startTime = startDateView.getDateText();
                        String endTime = endDateView.getDateText();
                        RequestParams params = new RequestParams(HttpConstants.getTrackInfoUrl(startTime, endTime));
                        DHttpUtils.get_String((MainActivity) getActivity(), true, params, new DCommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                ResponseBean<List<TrackBean>> responseBean = new Gson().fromJson(result, new TypeToken<ResponseBean<List<TrackBean>>>() {
                                }.getType());
                                if (responseBean != null && responseBean.getCode() == 1) {
                                    hasTrack = true;
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
                                    handler.removeCallbacks(LocationFragment.this); //停止刷新
                                } else {
                                    showShortText(responseBean.getErrmsg());
                                }
                            }
                        });

                    }
                });
                break;
            case R.id.iv_fence://电子围栏
                if (locInfoBean.isOpenVf()) {
                    CommonUtils.showCustomDialog0(getActivity(), "提示", "您确定要关闭电子围栏吗？", new DSingleDialogCallback() {
                        @Override
                        public void onPositiveButtonClick(String editText) {
                            RequestParams params = new RequestParams(HttpConstants.getcloseVfUrl());
                            DHttpUtils.get_String((MainActivity) getActivity(), true, params, new DCommonCallback<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    ResponseBean<String> responseBean = new Gson().fromJson(result, new TypeToken<ResponseBean<String>>() {
                                    }.getType());
                                    if (responseBean != null) {
                                        showShortText(responseBean.getErrmsg());
                                        fenceImageView.setEnabled(false);
                                    }
                                }
                            });
                        }
                    });
                } else {
//                    CommonUtils.showCustomDialog0(getActivity(), "提示", "您确定要开启电子围栏吗？", new DSingleDialogCallback() {
//                        @Override
//                        public void onPositiveButtonClick(String editText) {
//                            String vfRangeStr = ConfigService.instance()
//                                    .getConfigValue(JCConstValues.S_VFRange);
//                            if (vfRangeStr == null || vfRangeStr.length() == 0) {
//                                // 系统中暂无此值，操作一次数据库
//                                ConfigService.instance().insertConfigValue(
//                                        JCConstValues.S_VFRange, "100");
//                                vfRangeStr = "100";
//                            }
//                            RequestParams params = new RequestParams(HttpConstants.getcloseVfUrl());
//                            DHttpUtils.get_String((MainActivity) getActivity(), true, params, new DCommonCallback<String>() {
//                                @Override
//                                public void onSuccess(String result) {
//                                    ResponseBean<String> responseBean = new Gson().fromJson(result, new TypeToken<ResponseBean<String>>() {
//                                    }.getType());
//                                    if (responseBean != null) {
//                                        showShortText(responseBean.getErrmsg());
//                                        fenceImageView.setEnabled(false);
//                                    }
//                                }
//                            });
//                        }
//                    });
                }
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

    //选择时间弹出框
    private void initDateWindow() {
        //时间布局
        dateLayout = new LinearLayout(getActivity());
        dateLayout.setOrientation(LinearLayout.VERTICAL);
        dateLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //开始时间
        startDateView = FormViewUtils.createFormTextDateTimeView(getActivity(), "开始时间", 2, true, true, true);
        startDateView.setDateText(CommonUtils.getYesterdayDateString("yyyy-MM-dd HH:mm"));
        startDateView.setmFormViewOnclick(true);
        dateLayout.addView(startDateView);
        //结束时间
        endDateView = FormViewUtils.createFormTextDateTimeView(getActivity(), "结束时间", 2, true, true, true);
        endDateView.setDateText(CommonUtils.getCurrentDateString("yyyy-MM-dd HH:mm"));
        endDateView.setmFormViewOnclick(true);
        dateLayout.addView(endDateView);
    }

    //处理远程锁车推送
    public void onEvent(RemoteLockCarEvent event) {
        if (event != null) {
            if (event.getIsLock().equals("1")) {
                lockImageView.setImageResource(R.mipmap.voice_lock);
            } else {
                lockImageView.setImageResource(R.mipmap.voice_unlock);
            }
            lockImageView.setEnabled(true);
            showShortText(event.getMsg());
        }
    }

    //处理远程锁车推送
//    public void onEvent(RemoteLockCarEvent event) {
//        if (event != null) {
//            if (event.getIsLock().equals("1")) {
//                lockImageView.setImageResource(R.mipmap.voice_lock);
//            } else {
//                lockImageView.setImageResource(R.mipmap.voice_unlock);
//            }
//            lockImageView.setEnabled(true);
//            showShortText(event.getMsg());
//        }
//    }

}

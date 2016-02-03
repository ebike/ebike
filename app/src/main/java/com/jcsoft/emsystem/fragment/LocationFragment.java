package com.jcsoft.emsystem.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.activity.MainActivity;
import com.jcsoft.emsystem.activity.SimpleNaviActivity;
import com.jcsoft.emsystem.base.LocInfoBean;
import com.jcsoft.emsystem.bean.ResponseBean;
import com.jcsoft.emsystem.bean.TrackBean;
import com.jcsoft.emsystem.callback.DCommonCallback;
import com.jcsoft.emsystem.callback.DSingleDialogCallback;
import com.jcsoft.emsystem.client.JCLocationManager;
import com.jcsoft.emsystem.constants.AppConfig;
import com.jcsoft.emsystem.constants.JCConstValues;
import com.jcsoft.emsystem.database.ConfigService;
import com.jcsoft.emsystem.event.ChangeLocationEvent;
import com.jcsoft.emsystem.event.RemoteLockCarEvent;
import com.jcsoft.emsystem.event.RemoteVFEvent;
import com.jcsoft.emsystem.event.StopNaviEvent;
import com.jcsoft.emsystem.http.DHttpUtils;
import com.jcsoft.emsystem.http.DRequestParamsUtils;
import com.jcsoft.emsystem.http.HttpConstants;
import com.jcsoft.emsystem.map.TTSController;
import com.jcsoft.emsystem.utils.CommonUtils;
import com.jcsoft.emsystem.utils.MapUtils;
import com.jcsoft.emsystem.utils.Utils;
import com.jcsoft.emsystem.view.formview.FormTextDateTimeView;
import com.jcsoft.emsystem.view.formview.FormViewUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

/**
 * 卫星定位
 * Created by jimmy on 15/12/28.
 */
public class LocationFragment extends BaseFragment implements Runnable, View.OnClickListener,
        AMap.OnMapClickListener, AMapNaviListener, AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, GeocodeSearch.OnGeocodeSearchListener {
    @ViewInject(R.id.map_view)
    MapView mapView;
    @ViewInject(R.id.iv_traffic)
    ImageView trafficImageView;
    @ViewInject(R.id.iv_map_type)
    ImageView mapTypeImageView;
    @ViewInject(R.id.iv_lock)
    ImageView lockImageView;
    @ViewInject(R.id.iv_trajectory)
    ImageView trajectoryImageView;
    @ViewInject(R.id.iv_fence)
    ImageView fenceImageView;
    @ViewInject(R.id.iv_car_status)
    ImageView carStatusImageView;
    @ViewInject(R.id.iv_nav)
    ImageView navImageView;
    @ViewInject(R.id.tv_satellite)
    TextView satelliteTextView;
    @ViewInject(R.id.tv_plane)
    TextView planeTextView;
    @ViewInject(R.id.tv_equipment_serial_number)
    TextView equipmentSerialNumberTextView;
    @ViewInject(R.id.tv_positioning_state)
    TextView positioningStateTextView;
    @ViewInject(R.id.tv_online_status)
    TextView onlineStatusTextView;
    @ViewInject(R.id.tv_acc)
    TextView accTextView;
    @ViewInject(R.id.tv_main_power)
    TextView mainPowerTextView;
    @ViewInject(R.id.tv_lock_car_status)
    TextView lockCarStatusTextView;
    @ViewInject(R.id.tv_direction)
    TextView directionTextView;
    @ViewInject(R.id.tv_address)
    TextView addressTextView;
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
    //手动选择起点
    private boolean _isChooseStart;
    //手动选择起点的信息框
    private Marker _startMarker;
    //电动车上面信息框
    private Marker marker;
    //是否开启实时路况
    private boolean isOpenTraffic;
    //地图类型选择窗口
    private View mapTypeView;
    private PopupWindow popupWindow;
    //车辆信息
    private View carStatusView;
    private PopupWindow carPopupWindow;
    //其他
    private MediaPlayer _dingPlayer = null;
    private Timer _dingPlayerTimer = null;
    private int _dingPlayerTimerCount = 0;
    private MediaPlayer _ding2Player = null;
    private Timer _ding2PlayerTimer = null;
    private int _ding2PlayerTimerCount = 0;
    //电子围栏覆盖物
    private Circle circle;
    //地图导航类
    private AMapNavi mapNavi;
    //电动车上的信息框显示状态(默认显示)
    private boolean isHidden;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        x.view().inject(this, view);
        mapTypeView = inflater.inflate(R.layout.popupwindow_map_type, null, false);
        x.view().inject(this, mapTypeView);
        carStatusView = inflater.inflate(R.layout.popupwindow_car_status, null, false);
        carStatusView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        x.view().inject(this, carStatusView);
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
        trafficImageView.setOnClickListener(this);
        mapTypeImageView.setOnClickListener(this);
        lockImageView.setOnClickListener(this);
        trajectoryImageView.setOnClickListener(this);
        fenceImageView.setOnClickListener(this);
        navImageView.setOnClickListener(this);
        satelliteTextView.setOnClickListener(this);
        planeTextView.setOnClickListener(this);
        carStatusImageView.setOnClickListener(this);
    }

    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnMapClickListener(this);
            aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
            aMap.setOnInfoWindowClickListener(this);
            uiSettings = aMap.getUiSettings();
            //不显示缩放按键
            uiSettings.setZoomControlsEnabled(false);
            //显示比例尺
            uiSettings.setScaleControlsEnabled(true);
        }
        moveToCenter("36.68445", "117.126229");
        // 初始化语音模块
        TTSController ttsManager = TTSController.getInstance(getActivity());
        ttsManager.init();
        mapNavi = AMapNavi.getInstance(getActivity());
        mapNavi.setAMapNaviListener(ttsManager);// 设置语音模块播报
        mapNavi.setAMapNaviListener(this);
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
    public void requestDatas() {
        if (!isPrepared || !isVisible || hasLoadedOnce || !isAdded()) {
            return;
        }
        if (hasTrack) {
            aMap.clear();
        }
        RequestParams params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.getLocInfoUrl());
        DHttpUtils.get_String((MainActivity) getActivity(), true, params, new DCommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ResponseBean<LocInfoBean> responseBean = new Gson().fromJson(result, new TypeToken<ResponseBean<LocInfoBean>>() {
                }.getType());
                if (responseBean != null && responseBean.getCode() == 1) {
                    locInfoBean = responseBean.getData();
                    if (locInfoBean.getLon() > 0 && locInfoBean.getLat() > 0) {
                        //清除之前添加的障碍物
                        if (marker != null) {
                            marker.remove();
                        }
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
                        marker = aMap.addMarker(markerOptions);
                        if (!isHidden) {
                            marker.showInfoWindow();
                        } else {
                            marker.hideInfoWindow();
                        }
                        CameraUpdate cu = CameraUpdateFactory.changeLatLng(position);
                        aMap.moveCamera(cu);
                        //判断锁车状态
                        if (AppConfig.isExecuteLock == null) {
                            if (locInfoBean.getLock().equals("1")) {
                                AppConfig.isLock = true;
                                lockImageView.setImageResource(R.mipmap.voice_lock);
                            } else {
                                AppConfig.isLock = false;
                                lockImageView.setImageResource(R.mipmap.voice_unlock);
                            }
                        }
                        //判断电子围栏
                        if (AppConfig.isExecuteVF == null) {
                            if (locInfoBean.isOpenVf()) {
                                fenceImageView.setImageResource(R.mipmap.fence_open);
                                LatLng vfPosition = new LatLng(locInfoBean.getVfLat() / 1000000.0, locInfoBean.getVfLon() / 1000000.0);
                                if (circle == null) {
                                    circle = aMap.addCircle(new CircleOptions().center(vfPosition)
                                            .radius(100).strokeColor(Color.RED).fillColor(Color.TRANSPARENT)
                                            .strokeWidth(5));
                                }
                            } else {
                                fenceImageView.setImageResource(R.mipmap.fence_close);
                                if (circle != null) {
                                    circle.remove();
                                    circle = null;
                                }
                            }
                        }
                    } else {
                        showShortText("定位失败");
                    }
                    //刷新车辆信息
                    equipmentSerialNumberTextView.setText(locInfoBean.getCarId() + "");
                    if (locInfoBean.getSourceType() == 1) {
                        positioningStateTextView.setText("GPS定位");
                    } else if (locInfoBean.getSourceType() == 2) {
                        positioningStateTextView.setText("基站定位");
                    }
                    if (locInfoBean.getIsOnline().equals("1")) {
                        onlineStatusTextView.setText("在线");
                    } else {
                        onlineStatusTextView.setText("不在线");
                    }
                    if (locInfoBean.getAcc().equals("1")) {
                        accTextView.setText("开启");
                    } else {
                        accTextView.setText("关闭");
                    }
                    if (locInfoBean.getPower().equals("1")) {
                        mainPowerTextView.setText("开启");
                    } else {
                        mainPowerTextView.setText("关闭");
                    }
                    if (locInfoBean.getLock().equals("1")) {
                        lockCarStatusTextView.setText("锁定");
                    } else {
                        lockCarStatusTextView.setText("未锁定");
                    }
                    directionTextView.setText(MapUtils.directionStr(locInfoBean.getHeading()));
                    GeocodeSearch geocodeSearch = new GeocodeSearch(getActivity());
                    geocodeSearch.setOnGeocodeSearchListener(LocationFragment.this);
                    LatLonPoint latLonPoint = new LatLonPoint(locInfoBean.getLat() / 1000000.0, locInfoBean.getLon() / 1000000.0);
                    RegeocodeQuery regeocodeQuery = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
                    geocodeSearch.getFromLocationAsyn(regeocodeQuery);
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
        if (hasTrack) {
            aMap.clear();
            //每30秒刷新一次电动车位置
            handler.postDelayed(this, 1000 * 30);
            hasTrack = false;
        }
    }

    //切换回定位模式
    public void onEvent(ChangeLocationEvent event) {
        if (event != null && event.isChange() && hasTrack) {
            aMap.clear();
            requestDatas();
            //每30秒刷新一次电动车位置
            handler.postDelayed(this, 1000 * 30);
            hasTrack = false;
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        TTSController.getInstance(getActivity()).startSpeaking();
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
        mapNavi.destroy();
        mapView.onDestroy();
        handler.removeCallbacks(this); //停止刷新
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void run() {
        requestDatas();
        handler.postDelayed(this, 1000 * 30);// 间隔30秒
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_traffic://实时路况
                if (isOpenTraffic) {
                    isOpenTraffic = false;
                    trafficImageView.setImageResource(R.mipmap.traffic_close);
                } else {
                    isOpenTraffic = true;
                    trafficImageView.setImageResource(R.mipmap.traffic_open);
                }
                aMap.setTrafficEnabled(isOpenTraffic);
                break;
            case R.id.iv_map_type://切换地图类型
                if (popupWindow == null) {
                    popupWindow = CommonUtils.createPopupWindow(mapTypeView);
                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            mapTypeImageView.setImageResource(R.mipmap.map_switch);
                        }
                    });
                }
                popupWindow.showAsDropDown(mapTypeImageView);
                mapTypeImageView.setImageResource(R.mipmap.close_map_type_tip);
                break;
            case R.id.tv_satellite://卫星图
                aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
                changeMapType(AMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.tv_plane://平面图
                aMap.setMapType(AMap.MAP_TYPE_NORMAL);
                changeMapType(AMap.MAP_TYPE_NORMAL);
                break;
            case R.id.iv_lock://语音锁车/解锁
                if (AppConfig.isExecuteLock != null) {
                    if (AppConfig.isExecuteLock == 1) {
                        CommonUtils.showCustomDialogSignle3(getActivity(), "", "正在开启语音寻车，请稍等。");
                    } else if (AppConfig.isExecuteLock == 0) {
                        CommonUtils.showCustomDialogSignle3(getActivity(), "", "正在关闭语音寻车，请稍等。");
                    }
                } else {
                    if (locInfoBean.getLock().equals("1")) {
                        CommonUtils.showCustomDialog0(getActivity(), "提示", "您确定要关闭语音寻车吗？", new DSingleDialogCallback() {
                            @Override
                            public void onPositiveButtonClick(String editText) {
                                RequestParams params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.getUnLockBikeUrl());
                                DHttpUtils.get_String((MainActivity) getActivity(), true, params, new DCommonCallback<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        ResponseBean<String> responseBean = new Gson().fromJson(result, new TypeToken<ResponseBean<String>>() {
                                        }.getType());
                                        if (responseBean != null) {
                                            AppConfig.isExecuteLock = 0;
                                            AppConfig.lockCarType = 1;
                                            showShortText("关闭命令发送成功");
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        CommonUtils.showCustomDialog0(getActivity(), "提示", "您确定要开启语音寻车吗？", new DSingleDialogCallback() {
                            @Override
                            public void onPositiveButtonClick(String editText) {
                                RequestParams params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.getlockBikeUrl());
                                DHttpUtils.get_String((MainActivity) getActivity(), true, params, new DCommonCallback<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        ResponseBean<String> responseBean = new Gson().fromJson(result, new TypeToken<ResponseBean<String>>() {
                                        }.getType());
                                        if (responseBean != null) {
                                            AppConfig.isExecuteLock = 1;
                                            AppConfig.lockCarType = 1;
                                            showShortText("开启命令发送成功");
                                        }
                                    }
                                });
                            }
                        });
                    }
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
                        if (CommonUtils.moreThanAWeek(startTime, endTime)) {
                            showShortText("最多可查看一周的轨迹，您已超出时间范围，请修改后重试。");
                            return;
                        }
                        RequestParams params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.getTrackInfoUrl(startTime, endTime));
                        DHttpUtils.get_String((MainActivity) getActivity(), true, params, new DCommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                ResponseBean<List<TrackBean>> responseBean = new Gson().fromJson(result, new TypeToken<ResponseBean<List<TrackBean>>>() {
                                }.getType());
                                if (responseBean != null && responseBean.getCode() == 1) {
                                    hasTrack = true;
                                    aMap.clear();
                                    if (responseBean.getData().size() <= 0) {
                                        return;
                                    }
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
                                            R.mipmap.marker_start, true);
                                    if (maxCount > 1) {
                                        addMarkerToMap(points.get(points.size() - 1), getResources()
                                                        .getString(R.string.txt_end), end.getSatelliteTime(),
                                                end.getSpeed(), R.mipmap.marker_end, true);
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
                if (AppConfig.isExecuteVF != null) {
                    if (AppConfig.isExecuteVF == 1) {
                        CommonUtils.showCustomDialogSignle3(getActivity(), "", "正在打开电子围栏，请稍等。");
                    } else if (AppConfig.isExecuteVF == 0) {
                        CommonUtils.showCustomDialogSignle3(getActivity(), "", "正在关闭电子围栏，请稍等。");
                    }
                } else {
                    if (locInfoBean.isOpenVf()) {
                        CommonUtils.showCustomDialog0(getActivity(), "提示", "您确定要关闭电子围栏吗？", new DSingleDialogCallback() {
                            @Override
                            public void onPositiveButtonClick(String editText) {
                                RequestParams params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.getCloseVfUrl());
                                DHttpUtils.get_String((MainActivity) getActivity(), true, params, new DCommonCallback<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        ResponseBean<String> responseBean = new Gson().fromJson(result, new TypeToken<ResponseBean<String>>() {
                                        }.getType());
                                        if (responseBean != null) {
                                            AppConfig.isExecuteVF = 0;
                                            showShortText(responseBean.getErrmsg());
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        //打开电子围栏前需切回定位模式
                        onEvent(new ChangeLocationEvent(true));
                        CommonUtils.showCustomDialog0(getActivity(), "提示", "您确定要开启电子围栏吗？", new DSingleDialogCallback() {
                            @Override
                            public void onPositiveButtonClick(String editText) {
                                //常数
                                int r = 100;
                                int K = 111700 * 2;
                                double R = 3.141592654 / 180;
                                double latitude = locInfoBean.getLat() / 1000000.0;
                                double longitude = locInfoBean.getLon() / 1000000.0;
                                //计算电子围栏的范围
                                double maxLat = latitude + (double) r / K;//最大纬度
                                double minLat = latitude - (double) r / K;//最小纬度
                                double minLon = longitude - r / (K * Math.cos(latitude * R));//最小经度
                                double maxLon = longitude + r / (K * Math.cos(latitude * R));//最大经度
                                RequestParams params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.getOpenVfUrl(longitude, latitude, maxLon, maxLat, minLon, minLat));
                                DHttpUtils.get_String((MainActivity) getActivity(), true, params, new DCommonCallback<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        ResponseBean<String> responseBean = new Gson().fromJson(result, new TypeToken<ResponseBean<String>>() {
                                        }.getType());
                                        if (responseBean != null) {
                                            AppConfig.isExecuteVF = 1;
                                            showShortText(responseBean.getErrmsg());
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
                break;
            case R.id.iv_car_status://车辆信息
                if (carPopupWindow == null) {
                    carPopupWindow = CommonUtils.createAbovePopupWindow(carStatusView);
                    carPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            carStatusImageView.setImageResource(R.mipmap.car_status);
                        }
                    });
                }
                int popupWidth = carStatusView.getMeasuredWidth();
                int popupHeight = carStatusView.getMeasuredHeight();
                int[] vLocation = new int[2];
                v.getLocationOnScreen(vLocation);
                carPopupWindow.showAtLocation(v, Gravity.NO_GRAVITY, (vLocation[0] + v.getWidth() / 2) - popupWidth / 2,
                        vLocation[1] - popupHeight);
                carStatusImageView.setImageResource(R.mipmap.close_car_status_tip);
                break;
            case R.id.iv_nav://导航
                marker.hideInfoWindow();
                JCLocationManager.instance().start();
                Location location = JCLocationManager.instance().getCurrentLocation();
                LatLonPoint destPoint = new LatLonPoint(locInfoBean.getLat() / 1000000.0, locInfoBean.getLon() / 1000000.0);
                if (location != null) {
                    Toast.makeText(getActivity(), "正在进行路径计算...", Toast.LENGTH_LONG).show();
                    LatLonPoint startPoint = new LatLonPoint(location.getLatitude(), location.getLongitude());
                    startNavi(startPoint, destPoint);
                } else {
                    String text = "手机定位尚未完成，请先在地图上选择您的位置";
                    TTSController.getInstance(getActivity()).playText(text);
                    showShortText(text);
                    _isChooseStart = true;
                    CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(
                            new LatLng(destPoint.getLatitude(), destPoint.getLongitude()), 15);
                    aMap.moveCamera(cu);
                }
                break;
        }
    }

    //切换地图类型时变化页面UI
    private void changeMapType(int type) {
        if (type == AMap.MAP_TYPE_SATELLITE) {
            //选中卫星图
            Drawable satelliteDrawable = getResources().getDrawable(R.mipmap.satellite_hover);
            satelliteDrawable.setBounds(0, 0, satelliteDrawable.getMinimumWidth(), satelliteDrawable.getMinimumHeight());
            satelliteTextView.setCompoundDrawables(null, satelliteDrawable, null, null);
            //还原平面图
            Drawable planeDrawable = getResources().getDrawable(R.mipmap.plane);
            planeDrawable.setBounds(0, 0, planeDrawable.getMinimumWidth(), planeDrawable.getMinimumHeight());
            planeTextView.setCompoundDrawables(null, planeDrawable, null, null);
        } else if (type == AMap.MAP_TYPE_NORMAL) {
            //选中平面图
            Drawable planeDrawable = getResources().getDrawable(R.mipmap.plane_hover);
            planeDrawable.setBounds(0, 0, planeDrawable.getMinimumWidth(), planeDrawable.getMinimumHeight());
            planeTextView.setCompoundDrawables(null, planeDrawable, null, null);
            //选中卫星图
            Drawable satelliteDrawable = getResources().getDrawable(R.mipmap.satellite);
            satelliteDrawable.setBounds(0, 0, satelliteDrawable.getMinimumWidth(), satelliteDrawable.getMinimumHeight());
            satelliteTextView.setCompoundDrawables(null, satelliteDrawable, null, null);
        }
    }


    //在地图上添加轨迹蓝点
    private Marker addMarkerToMap(LatLng latlng, String devName, String time,
                                  double speed, int drawableId, boolean isPerspective) {
        MarkerOptions options = new MarkerOptions();
        options.position(latlng);
        options.title(devName);
        if (drawableId == R.mipmap.marker_start || drawableId == R.mipmap.marker_end) {
            options.anchor(0.5f, 1.0f);
        } else {
            options.anchor(0.5f, 0.5f);
        }
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
        startDateView = FormViewUtils.createFormTextDateTimeView(getActivity(), "开始时间", "请选择开始时间", 2, true, true, true);
        startDateView.setDateText(CommonUtils.getYesterdayDateString("yyyy-MM-dd HH:mm"));
        startDateView.setmFormViewOnclick(true);
        dateLayout.addView(startDateView);
        //结束时间
        endDateView = FormViewUtils.createFormTextDateTimeView(getActivity(), "结束时间", "请选择结束时间", 2, true, true, true);
        endDateView.setDateText(CommonUtils.getCurrentDateString("yyyy-MM-dd HH:mm"));
        endDateView.setmFormViewOnclick(true);
        dateLayout.addView(endDateView);
    }

    //处理远程锁车推送
    public void onEvent(RemoteLockCarEvent event) {
        if (event != null) {
            AppConfig.isExecuteLock = null;
            if (event.getIsLock().equals("1")) {
                lockImageView.setImageResource(R.mipmap.voice_lock);
                AppConfig.isLock = true;
                if (AppConfig.lockCarType == 1) {
                    showShortText("开启成功");
                }
            } else {
                lockImageView.setImageResource(R.mipmap.voice_unlock);
                AppConfig.isLock = false;
                if (AppConfig.lockCarType == 1) {
                    showShortText("关闭成功");
                }
            }

        }
    }

    //处理电子围栏推送
    public void onEvent(RemoteVFEvent event) {
        if (event != null) {
            if (event.getIsOpen().equals("1")) {
                fenceImageView.setImageResource(R.mipmap.fence_open);
                LatLng position = new LatLng(locInfoBean.getLat() / 1000000.0, locInfoBean.getLon() / 1000000.0);
                if (circle == null) {
                    circle = aMap.addCircle(new CircleOptions().center(position)
                            .radius(100).strokeColor(Color.RED).fillColor(Color.TRANSPARENT)
                            .strokeWidth(5));
                }
                locInfoBean.setIsOpenVf(true);
            } else {
                fenceImageView.setImageResource(R.mipmap.fence_close);
                if (circle != null) {
                    circle.remove();
                    circle = null;
                    locInfoBean.setIsOpenVf(false);
                }
            }
            AppConfig.isExecuteVF = null;
            showShortText(event.getMsg());
        }
    }

    //停止导航
    public void onEvent(StopNaviEvent event) {
        if (event != null && event.isStopNavi()) {
            this.stopNavi();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (_isChooseStart) {
            if (_startMarker != null) {
                _startMarker.remove();
            }
            _startMarker = aMap.addMarker(new MarkerOptions()
                    .anchor(0.5f, 1)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.mipmap.point)).position(latLng)
                    .snippet("点击选择为起点"));
            _startMarker.showInfoWindow();
        }
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation newLoc) {
        double d = calculateDistance(newLoc.getCoord().getLatitude(), newLoc.getCoord().getLongitude()
                , locInfoBean.getLat() / 1000000.0f
                , locInfoBean.getLon() / 1000000.0f);
        checkToPlayDing(d);
    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteSuccess() {
        Intent intent = new Intent(getActivity(),
                SimpleNaviActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        Bundle bundle = new Bundle();
        bundle.putInt(Utils.ACTIVITYINDEX, Utils.SIMPLEGPSNAVI);
        bundle.putBoolean(Utils.ISEMULATOR, false);
        intent.putExtras(bundle);
        startActivity(intent);
        ((MainActivity) getActivity()).dismissLoadingprogress();
    }

    @Override
    public void onCalculateRouteFailure(int i) {
        ((MainActivity) getActivity()).startLoadingProgress();
    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    //开始导航
    private void startNavi(LatLonPoint startPoint, LatLonPoint stopPoint) {
        NaviLatLng startP = new NaviLatLng(startPoint.getLatitude(), startPoint.getLongitude());
        NaviLatLng endP = new NaviLatLng(stopPoint.getLatitude(), stopPoint.getLongitude());
        AMapNavi.getInstance(getActivity()).calculateWalkRoute(startP, endP);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker != null && !marker.isInfoWindowShown()) {
            isHidden = false;
            marker.showInfoWindow();
        }
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        marker.hideInfoWindow();
        _isChooseStart = false;
        if (_startMarker != null && _startMarker.equals(marker)) {
            LatLonPoint startPoint = new LatLonPoint(_startMarker.getPosition().latitude, _startMarker.getPosition().longitude);
            LatLonPoint destPoint = new LatLonPoint(locInfoBean.getLat() / 1000000.0, locInfoBean.getLon() / 1000000.0);
            startNavi(startPoint, destPoint);
            _startMarker.remove();
            return;
        }
        if (marker != null && this.marker.equals(marker)) {
            isHidden = true;
        }
    }

    private double calculateDistance(double firstLat, double firstLon, double secondLat, double secondLon) {
        double d = getDistance(firstLat, firstLon, secondLat, secondLon);
        return d;
    }

    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double difference = radLat1 - radLat2;
        double mdifference = rad(lng1) - rad(lng2);
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(difference / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(mdifference / 2), 2)));
        distance = distance * 6378.137f;
        distance = Math.round(distance * 10000) / 10.0f;

        return distance;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    @SuppressLint("DefaultLocale")
    private void checkToPlayDing(double d) {
        String dStr = String.format("%.2f米", d);
        dStr = dStr.substring(0, dStr.indexOf("."));
        showShortText("与车辆最后位置直线距离：" + dStr);
        if (d > 40.0) {
            if (_dingPlayerTimer != null) {
                _dingPlayerTimer.cancel();
            }

            if (_ding2PlayerTimer != null) {
                _ding2PlayerTimer.cancel();
            }
        } else if (d <= 40.0 && d > 10.0) //如果小于40米，播放声音"叮"
        {
            if (_ding2PlayerTimer != null) {
                _ding2PlayerTimer.cancel();
            }
            playDing(1000);
        } else if (d <= 10.0) {
            if (_dingPlayerTimer != null) {
                _dingPlayerTimer.cancel();
            }
            playDing2(3000);
        }
    }

    private void playDing(int intervalMs) {
        if (_dingPlayerTimer != null) {
            _dingPlayerTimer.cancel();
        }
        _dingPlayerTimer = new Timer();
        _dingPlayerTimerCount = 0;

        _dingPlayerTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                playDingMsgPrompt();
                if (_dingPlayerTimerCount++ >= 20) {
                    _dingPlayerTimerCount = 0;
                    _dingPlayerTimer.cancel();
                }
            }
        }, 0, intervalMs);
    }

    private void playDing2(int intervalMs) {
        if (_ding2PlayerTimer != null) {
            _ding2PlayerTimer.cancel();
        }
        _ding2PlayerTimer = new Timer();
        _ding2PlayerTimerCount = 0;

        _ding2PlayerTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                playDing2MsgPrompt();
                if (_ding2PlayerTimerCount++ >= 20) {
                    _ding2PlayerTimerCount = 0;
                    _ding2PlayerTimer.cancel();
                }
            }
        }, 0, intervalMs);
    }

    private void playDingMsgPrompt() {
        _dingPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                _dingPlayer.stop();
                try {
                    _dingPlayer.prepare();
                } catch (IllegalStateException e) {
                    e.printStackTrace();

                    _dingPlayer.reset();
                    _dingPlayer = MediaPlayer.create(getActivity(), R.raw.ding_1);
                } catch (IOException e) {
                    e.printStackTrace();

                    _dingPlayer.reset();
                    _dingPlayer = MediaPlayer.create(getActivity(), R.raw.ding_1);
                } catch (Exception e) {
                    e.printStackTrace();

                    _dingPlayer.reset();
                    _dingPlayer = MediaPlayer.create(getActivity(), R.raw.ding_1);
                }
            }
        });

        _dingPlayer.start();
    }

    private void playDing2MsgPrompt() {
        _ding2Player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                _ding2Player.stop();
                try {
                    _ding2Player.prepare();
                } catch (IllegalStateException e) {
                    e.printStackTrace();

                    _ding2Player.reset();
                    _ding2Player = MediaPlayer.create(getActivity(), R.raw.ding_2);
                } catch (IOException e) {
                    e.printStackTrace();

                    _ding2Player.reset();
                    _ding2Player = MediaPlayer.create(getActivity(), R.raw.ding_2);
                } catch (Exception e) {
                    e.printStackTrace();

                    _ding2Player.reset();
                    _ding2Player = MediaPlayer.create(getActivity(), R.raw.ding_2);
                }
            }
        });

        _ding2Player.start();
    }

    public void stopNavi() {
        if (_dingPlayerTimer != null) {
            _dingPlayerTimer.cancel();
        }

        if (_ding2PlayerTimer != null) {
            _ding2PlayerTimer.cancel();
        }
        JCLocationManager.instance().stop();
        AMapNavi.getInstance(getActivity()).stopNavi();

    }

    //逆地理编码回调接口
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        if (i == 0 && regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null) {
            RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
            addressTextView.setText(regeocodeAddress.getFormatAddress());
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }
}

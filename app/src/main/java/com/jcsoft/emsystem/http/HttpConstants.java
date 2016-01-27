package com.jcsoft.emsystem.http;

import com.jcsoft.emsystem.constants.AppConfig;

import java.util.Map;

/**
 * Created by jimmy on 15/12/28.
 */
public class HttpConstants {

    //接口前缀
    public static String baseUrl = "http://api.gnets.cn/app";

    //登录
    public static String getLoginUrl(String loginName, String password) {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/checkLogin.do");
        sb.append("?loginName=").append(loginName);
        sb.append("&password=").append(password);
        sb.append("&clientId=").append(AppConfig.registrationId);
        return sb.toString();
    }

    //查询当前车辆位置信息
    public static String getLocInfoUrl() {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/map/getLocInfo.do");
        if (AppConfig.userInfoBean != null) {
            sb.append("?carId=").append(AppConfig.userInfoBean.getCarId());
        }
        return sb.toString();
    }

    //根据起止时间查询轨迹信息
    public static String getTrackInfoUrl(String startTime, String endTime) {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/map/searchTrack.do");
        if (AppConfig.userInfoBean != null) {
            sb.append("?carId=").append(AppConfig.userInfoBean.getCarId());
        }
        sb.append("&startTime=").append(startTime);
        sb.append("&endTime=").append(endTime);
        return sb.toString().replace(" ", "%20");
    }

    //返回车辆基本信息
    public static String getCarInfoUrl() {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/user/getCarInfo.do");
        sb.append("?carId=").append(AppConfig.userInfoBean.getCarId());
        return sb.toString();
    }

    //远程锁车
    public static String getlockBikeUrl() {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/lock/lockBike.do");
        sb.append("?carId=").append(AppConfig.userInfoBean.getCarId());
        return sb.toString();
    }

    //远程解锁
    public static String getUnLockBikeUrl() {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/lock/unLockBike.do");
        sb.append("?carId=").append(AppConfig.userInfoBean.getCarId());
        return sb.toString();
    }

    //关闭电子围栏
    public static String getCloseVfUrl() {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/vf/closeVf.do");
        sb.append("?carId=").append(AppConfig.userInfoBean.getCarId());
        return sb.toString();
    }

    //查询报警消息
    public static String getNewAlarmEventInfo(int mark, int eventId) {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/alarm/getNewAlarmEventInfo.do");
        sb.append("?carId=").append(AppConfig.userInfoBean.getCarId());
        sb.append("&mark=").append(mark);
        sb.append("&eventId=").append(eventId);
        return sb.toString();
    }

    //查看报警消息
    public static String viewAlarmEvent(int eventId) {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/alarm/viewAlarmEvent.do");
        sb.append("?eventId=").append(eventId);
        return sb.toString();
    }

    //获取用户基本信息
    public static String getUserInfo() {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/user/getUserInfo.do");
        sb.append("?carId=").append(AppConfig.userInfoBean.getCarId());
        return sb.toString();
    }

    //开启电子围栏
    public static String getOpenVfUrl(double lon, double lat, double maxLon, double maxLat, double minLon, double minLat) {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/vf/openVf.do");
        sb.append("?carId=").append(AppConfig.userInfoBean.getCarId());
        sb.append("&maxLon=").append(maxLon);
        sb.append("&maxLat=").append(maxLat);
        sb.append("&minLon=").append(minLon);
        sb.append("&minLat=").append(minLat);
        sb.append("&lon=").append(lon);
        sb.append("&lat=").append(lat);
        return sb.toString();
    }

    //获取用户保险信息
    public static String getInsurInfoUrl() {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/insur/getInsurInfo.do");
        sb.append("?carId=").append(AppConfig.userInfoBean.getCarId());
        return sb.toString();
    }

    //获取每日统计数据
    public static String getDayDataUrl(String date) {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/chart/getDayData.do");
        sb.append("?carId=").append(AppConfig.userInfoBean.getCarId());
        sb.append("&date=").append(date);
        return sb.toString();
    }

    //根据用户输入IMEI后八位自动补全IMEI
    public static String getSearchCarLikeImei(String imei) {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/user/searchCarLikeImei.do");
        sb.append("?imei=").append(imei);
        return sb.toString();
    }

    //用户注册
    public static String getRegDeviceUrl(Map<String, String> map) {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/user/regDevice.do");
        sb.append("?carId=").append(map.get("carId"));
        sb.append("&telNum=").append(map.get("telNum"));
        sb.append("&userName=").append(map.get("userName"));
        sb.append("&idNum=").append(map.get("idNum"));
        sb.append("&phone=").append(map.get("phone"));
        sb.append("&province=").append(map.get("province"));
        sb.append("&city=").append(map.get("city"));
        sb.append("&area=").append(map.get("area"));
        sb.append("&address=").append(map.get("address"));
        sb.append("&dealerId=").append(map.get("dealerId"));
        sb.append("&salesman=").append(map.get("salesman"));
        return sb.toString();
    }

    //获取指定天数的统计数据
    public static String getSomeDayDataUrl() {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/chart/getSomeDayData.do");
        sb.append("?carId=").append(AppConfig.userInfoBean.getCarId());
        sb.append("&dayNum=").append(15);
        return sb.toString();
    }
}

package com.jcsoft.emsystem.http;

import com.jcsoft.emsystem.constants.AppConfig;

/**
 * Created by jimmy on 15/12/28.
 */
public class HttpConstants {

    //接口前缀
    public static String baseUrl = "http://dev.gnets.cn/app";

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
    public static String getlockBikeUrl(){
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/lock/lockBike.do");
        sb.append("?carId=").append(AppConfig.userInfoBean.getCarId());
        return sb.toString();
    }

    //远程解锁
    public static String getUnLockBikeUrl(){
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/lock/unLockBike.do");
        sb.append("?carId=").append(AppConfig.userInfoBean.getCarId());
        return sb.toString();
    }

    //关闭电子围栏
    public static  String getcloseVfUrl(){
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/vf/closeVf.do");
        sb.append("?carId=").append(AppConfig.userInfoBean.getCarId());
        return sb.toString();
    }
}

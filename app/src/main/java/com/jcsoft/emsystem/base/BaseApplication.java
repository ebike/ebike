package com.jcsoft.emsystem.base;

import android.app.Application;
import android.telephony.TelephonyManager;

import com.jcsoft.emsystem.constants.AppConfig;
import com.jcsoft.emsystem.db.XUtil;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

/**
 * 启动应用是执行的类
 * 主要初始化第三方SDK
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化xutils框架
        x.Ext.init(this);
        x.Ext.setDebug(true);
        //初始化DB
        XUtil.initDB(this);
        // 设置开启日志,发布时请关闭日志
        JPushInterface.setDebugMode(true);
        // 初始化 JPush
        JPushInterface.init(this);
        //获取手机imei码
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        AppConfig.imei = TelephonyMgr.getDeviceId();
    }
}

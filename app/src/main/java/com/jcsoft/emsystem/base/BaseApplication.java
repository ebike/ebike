package com.jcsoft.emsystem.base;

import android.app.Application;

import org.xutils.x;

/**
 * 启动应用是执行的类
 * 主要初始化第三方SDK
 */
public class BaseApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化xutils框架
        x.Ext.init(this);
        x.Ext.setDebug(true);
    }
}

package com.jcsoft.emsystem.constants;

import com.jcsoft.emsystem.bean.UserInfoBean;

/*
 * 全局的常量存放工具
 */
public class AppConfig {
    //极光推送的ID
    public static String registrationId;
    public static String REGISTRATION_ID;
    //登录名
    public static String loginName;
    public static String LOGIN_NAME = "loginName";
    //登录密码（MD5加密）
    public static String password;
    public static String PASSWORD = "password";
    //当前用户信息
    public static UserInfoBean userInfoBean;
}

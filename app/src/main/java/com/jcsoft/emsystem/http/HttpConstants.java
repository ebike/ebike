package com.jcsoft.emsystem.http;

import com.jcsoft.emsystem.utils.CommonUtils;

/**
 * Created by jimmy on 15/12/28.
 */
public class HttpConstants {

    //接口前缀
    public static String baseUrl = "http://dev.gnets.cn/app";

    //登录
    public static String getLoginUrl(String userName, String password) {
        StringBuffer sb = new StringBuffer(baseUrl);
        sb.append("/checkLogin.do");
        sb.append("?userName=").append(userName);
        sb.append("&password=").append(CommonUtils.MD5(password));
        return sb.toString();
    }
}

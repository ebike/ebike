package com.jcsoft.emsystem.http;

import org.xutils.http.RequestParams;

import java.util.Map;

/**
 * 获取RequestParams的工具类
 * Created by jimmy on 15/12/11.
 */
public class DRequestParamsUtils {

    /**
     * @param url 请求地址
     * @param map 参数信息
     * @return
     */
    public static RequestParams getRequestParams(String url, Map<String, String> map) {
        RequestParams params = new RequestParams(url);
        for (String key : map.keySet()) {
            params.addBodyParameter(key, map.get(key));
        }
        return params;
    }


}

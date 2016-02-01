package com.jcsoft.emsystem.http;

import com.jcsoft.emsystem.base.SendParamsBean;
import com.jcsoft.emsystem.constants.AppConfig;

import org.xutils.http.RequestParams;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 获取RequestParams的工具类
 * Created by jimmy on 15/12/11.
 */
public class DRequestParamsUtils {

    /**
     * 获取含头部的请求参数
     *
     * @param url
     * @return
     */
    public static RequestParams getRequestParams_Header(String url) {
        RequestParams params = new RequestParams(url);
        params.addHeader("Authorization", AppConfig.userInfoBean.getUserToken());
        return params;
    }

    /**
     * 不含头部信息
     *
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

    /**
     * 含头部信息
     *
     * @param url 请求地址
     * @param map 参数信息
     * @return
     */
    public static RequestParams getRequestParams_Header(String url, Map<String, String> map) {
        RequestParams params = getRequestParams_Header(url);
        for (String key : map.keySet()) {
            params.addBodyParameter(key, map.get(key));
        }
        return params;
    }

    /**
     * 获取含有文件的表单参数
     * 不含头部信息
     *
     * @param url             请求地址
     * @param sendParamsBeans 参数信息
     * @return
     */
    public static RequestParams getRequestParamsHasFile(String url, List<SendParamsBean> sendParamsBeans) {
        RequestParams params = new RequestParams(url);
        params.setMultipart(true);
        for (SendParamsBean bean : sendParamsBeans) {
            if (bean.isFile()) {
                params.addBodyParameter(bean.getKey(), (File) bean.getValue());
            } else {
                params.addBodyParameter(bean.getKey(), (String) bean.getValue());
            }
        }
        return params;
    }

    /**
     * 获取含有文件的表单参数
     * 含头部信息
     *
     * @param url             请求地址
     * @param sendParamsBeans 参数信息
     * @return
     */
    public static RequestParams getRequestParamsHasFile_Header(String url, List<SendParamsBean> sendParamsBeans) {
        RequestParams params = getRequestParams_Header(url);
        params.setMultipart(true);
        for (SendParamsBean bean : sendParamsBeans) {
            if (bean.isFile()) {
                params.addBodyParameter(bean.getKey(), (File) bean.getValue());
            } else {
                params.addBodyParameter(bean.getKey(), (String) bean.getValue());
            }
        }
        return params;
    }

}

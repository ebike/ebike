package com.jcsoft.emsystem.callback;

/**
 * Created by jimmy on 2015/11/24.
 * 应用场景：只在成功时处理业务
 */
public interface DCommonCallback<T> {
    //请求成功的回调方法
    public void onSuccess(T result);
}
package com.jcsoft.emsystem.callback;

/**
 * Created by jimmy on 2015/11/25.
 * 应用场景：除了成功需要处理业务外，还需要在执行完成后处理一些业务
 */
public interface DFinishedCallback<T> extends DCommonCallback<T> {
    //请求结束的回调方法
    public void onFinished();
}

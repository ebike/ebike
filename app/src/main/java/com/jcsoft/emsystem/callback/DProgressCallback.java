package com.jcsoft.emsystem.callback;

/**
 * Created by huguangwen on 15/12/25.
 */
public interface DProgressCallback<T> extends DCommonCallback<T>{
    void onWaiting();

    void onStarted();
    //totleSize:总大小；uploadSize:已下载大小
    void onLoading(long totleSize, long uploadSize, boolean var5);
}

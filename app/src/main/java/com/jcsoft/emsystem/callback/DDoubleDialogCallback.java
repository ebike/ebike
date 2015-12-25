package com.jcsoft.emsystem.callback;

/**
 * Created by huguangwen on 15/12/15.
 */
public interface DDoubleDialogCallback extends DSingleDialogCallback{
    //“取消”回调方法
    void onNegativeButtonClick(String editText);
}

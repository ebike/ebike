package com.jcsoft.emsystem.http;

import android.view.Gravity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.base.BaseActivity;
import com.jcsoft.emsystem.bean.ResponseBean;
import com.jcsoft.emsystem.callback.DCommonCallback;
import com.jcsoft.emsystem.callback.DFinishedCallback;
import com.jcsoft.emsystem.callback.DSingleDialogCallback;
import com.jcsoft.emsystem.utils.CommonUtils;
import com.jcsoft.emsystem.utils.NetworkUtils;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * http请求接口工具类
 * Created by jimmy on 2015/11/24.
 */
public class DHttpUtils {

    /**
     * 处理请求时出现的异常
     *
     * @param ex
     */
    public static void dealException(BaseActivity activity, Throwable ex) {
        String exception = "";
        if (!NetworkUtils.isNetworkAvailable(activity)) {
            exception = activity.getResources().getString(R.string.no_network);
        } else if (ex instanceof HttpException) { // 网络错误
            HttpException httpEx = (HttpException) ex;
            int responseCode = httpEx.getCode();
            String responseMsg = httpEx.getMessage();
            String errorResult = httpEx.getResult();
            if (responseCode == 408) {//客户端超时
                exception = activity.getResources().getString(R.string.client_timeout);
            } else if (responseCode == 504) {//服务器超时
                exception = activity.getResources().getString(R.string.server_timeout);
            } else {
                exception = activity.getResources().getString(R.string.server_timeout);
            }
        } else { // 其他错误
            exception = activity.getResources().getString(R.string.client_timeout);
        }
        activity.showLongText(exception);
    }

    public static void validateAuthorization(String result, final BaseActivity activity, DCommonCallback<String> callback) {
        ResponseBean<Object> bean = new Gson().fromJson(result, new TypeToken<ResponseBean<Object>>() {
        }.getType());
        if (bean.getCode() == 0 && bean.getErrmsg().equals("认证失败")) {
            CommonUtils.showCustomDialogSignle(activity, "", "您的登录信息过期，请重新登录", Gravity.LEFT | Gravity.CENTER_VERTICAL, new DSingleDialogCallback() {
                @Override
                public void onPositiveButtonClick(String editText) {
                    activity.logoutClearData();
                }
            });
        } else {
            callback.onSuccess(result);
        }
    }

    /**
     * 封装xutils的post请求
     *
     * @param activity
     * @param showProgress
     * @param params
     * @param callback     接口中参数值类型为String类型，即：服务器返回的json
     * @return
     */
    public static Callback.Cancelable post_String(final BaseActivity activity, final boolean showProgress,
                                                  RequestParams params, final DCommonCallback<String> callback) {
        //是否显示加载框
        if (activity != null && showProgress) {
            activity.startLoadingProgress();
        }
        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                //关闭加载框
                if (activity != null && showProgress) {
                    activity.dismissLoadingprogress();
                }
                if (!CommonUtils.strIsEmpty(result)) {
                    validateAuthorization(result, activity, callback);
                } else {//数据异常
                    activity.showLongText(activity.getResources().getString(R.string.data_error));
                }
            }

            @Override
            public void onError(Throwable ex, boolean b) {
                //关闭加载框
                if (activity != null && showProgress) {
                    activity.dismissLoadingprogress();
                }
                dealException(activity, ex);
            }

            @Override
            public void onCancelled(CancelledException e) {
                //关闭加载框
                if (activity != null && showProgress) {
                    activity.dismissLoadingprogress();
                }
            }

            @Override
            public void onFinished() {
                if (callback instanceof DFinishedCallback) {
                    ((DFinishedCallback) callback).onFinished();
                }
            }

        });
        return cancelable;
    }

    /**
     * 封装xutils的get请求
     *
     * @param activity
     * @param showProgress
     * @param params
     * @param callback     接口中参数值类型为String类型，即：服务器返回的json
     * @return
     */
    public static Callback.Cancelable get_String(final BaseActivity activity, final boolean showProgress,
                                                 RequestParams params, final DCommonCallback<String> callback) {
        //是否显示加载框
        if (activity != null && showProgress) {
            activity.startLoadingProgress();
        }
        Callback.Cancelable cancelable = x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                //关闭加载框
                if (activity != null && showProgress) {
                    activity.dismissLoadingprogress();
                }
                if (!CommonUtils.strIsEmpty(result)) {
                    validateAuthorization(result, activity, callback);
                } else {//数据异常
                    activity.showLongText(activity.getResources().getString(R.string.data_error));
                }
            }

            @Override
            public void onError(Throwable ex, boolean b) {
                //关闭加载框
                if (activity != null && showProgress) {
                    activity.dismissLoadingprogress();
                }
                dealException(activity, ex);
            }

            @Override
            public void onCancelled(CancelledException e) {
                //关闭加载框
                if (activity != null && showProgress) {
                    activity.dismissLoadingprogress();
                }
            }

            @Override
            public void onFinished() {
                if (callback instanceof DFinishedCallback) {
                    ((DFinishedCallback) callback).onFinished();
                }
            }

        });
        return cancelable;
    }

}

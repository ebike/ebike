package com.jcsoft.emsystem.http;

import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.base.BaseActivity;
import com.jcsoft.emsystem.bean.ResponseBean;
import com.jcsoft.emsystem.callback.DCommonCallback;
import com.jcsoft.emsystem.callback.DFinishedCallback;
import com.jcsoft.emsystem.callback.DProgressCallback;
import com.jcsoft.emsystem.utils.CommonUtils;
import com.jcsoft.emsystem.utils.JsonDataUtils;
import com.jcsoft.emsystem.utils.NetworkUtils;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

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
                    callback.onSuccess(result);
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
                    callback.onSuccess(result);
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
     * 封装xutils的get请求-文件下载
     *
     * @param activity
     * @param showProgress
     * @param params
     * @param callback     接口中参数值类型为String类型，即：服务器返回的json
     * @return
     */
    public static Callback.Cancelable get_File(final BaseActivity activity, final boolean showProgress,
                                               RequestParams params, final DProgressCallback<File> callback) {
        //是否显示加载框
        if (activity != null && showProgress) {
            activity.startLoadingProgress();
        }
        Callback.Cancelable cancelable = x.http().get(params, new Callback.ProgressCallback<File>() {

            @Override
            public void onWaiting() {
                callback.onWaiting();
            }

            @Override
            public void onStarted() {
                callback.onStarted();
            }

            @Override
            public void onLoading(long totleSize, long uploadSize, boolean b) {
                callback.onLoading(totleSize, uploadSize, b);
            }

            @Override
            public void onSuccess(File file) {
                //关闭加载框
                if (activity != null && showProgress) {
                    activity.dismissLoadingprogress();
                }
                if (file != null) {
                    callback.onSuccess(file);
                } else {//数据异常
                    activity.showLongText(activity.getResources().getString(R.string.http_download_failed));
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
     * 封装xutils的post请求
     *
     * @param activity
     * @param showProgress
     * @param params
     * @param callback     接口中参数值类型为ResponseBean，即：将返回的json转化成ResponseBean对象
     * @return
     */
    public static Callback.Cancelable post_ResponseBean(final BaseActivity activity, final boolean showProgress,
                                                        RequestParams params, final DCommonCallback<ResponseBean> callback) {
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
                ResponseBean bean = null;
                if (!CommonUtils.strIsEmpty(result)) {
                    bean = JsonDataUtils.jsonToObject(result, ResponseBean.class);
                    if (bean.getCode() == 1) {
                        callback.onSuccess(bean);
                    } else {
                        activity.showLongText(bean.getErrmsg());
                    }
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

            }

        });
        return cancelable;
    }

    /**
     * 封装xutils的post请求
     *
     * @param activity
     * @param showProgress
     * @param params
     * @param callback     接口中参数值类型为ResponseBean，即：将返回的json转化成ResponseBean对象
     * @return
     */
    public static Callback.Cancelable get_Success_ResponseBean(final BaseActivity activity, final boolean showProgress,
                                                        RequestParams params, final DCommonCallback<ResponseBean> callback) {
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
                ResponseBean bean = null;
                if (!CommonUtils.strIsEmpty(result)) {
                    bean = JsonDataUtils.jsonToObject(result, ResponseBean.class);
                    if (bean.getCode() == 1) {
                        callback.onSuccess(bean);
                    } else {
                        activity.showLongText(bean.getErrmsg());
                    }
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

            }

        });
        return cancelable;
    }

    public static Callback.Cancelable get_ResponseBean(final BaseActivity activity, final boolean showProgress,
                                                       RequestParams params, final DCommonCallback<ResponseBean> callback) {
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
                ResponseBean bean = null;
                if (!CommonUtils.strIsEmpty(result)) {
                    bean = JsonDataUtils.jsonToObject(result, ResponseBean.class);
                    callback.onSuccess(bean);
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

            }

        });
        return cancelable;
    }

    /**
     * 封装xutils的post请求
     * 出现错误是不弹出错误信息，且没有加载框
     * 用于后台请求的方法
     *
     * @param params
     * @param callback 接口中参数值类型为ResponseBean，即：将返回的json转化成ResponseBean对象
     * @return
     */
    public static Callback.Cancelable post_ResponseBean_NoMsg(RequestParams params, final DCommonCallback<ResponseBean> callback) {
        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                if (!CommonUtils.strIsEmpty(result)) {
                    ResponseBean bean = JsonDataUtils.jsonToObject(result, ResponseBean.class);
                    if (bean.getCode() == 1) {
                        callback.onSuccess(bean);
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean b) {
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }

        });
        return cancelable;
    }
}

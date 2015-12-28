package com.jcsoft.emsystem.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jcsoft.emsystem.utils.DensityUtil;
import com.jcsoft.emsystem.utils.PreferencesUtil;
import com.jcsoft.emsystem.view.LoadingDialog;


/**
 * 所有activity的父类
 */
public abstract class BaseActivity extends FragmentActivity {

    private Handler mHandler = new Handler();
    private LoadingDialog mLoadingBar;
    protected PreferencesUtil preferencesUtil;
    protected DensityUtil densityUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesUtil = PreferencesUtil.getInstance();
        densityUtil = new DensityUtil(BaseActivity.this);
        loadXml();
        getIntentData(savedInstanceState);
        init();
        setListener();
        setData();
    }

    /**
     * 设置xml文件
     */
    public abstract void loadXml();

    /**
     * 获取intent数据
     *
     * @param savedInstanceState
     */
    public abstract void getIntentData(Bundle savedInstanceState);

    /**
     * view 初始化
     */
    public abstract void init();

    /**
     * 设置view监听器
     */
    public abstract void setListener();

    /**
     * 数据设置
     */
    public abstract void setData();

    /**
     * 长文本提示
     */
    public void showLongText(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    /**
     * 短文本提示
     */
    public void showShortText(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /***
     * 请求网络的时候开启loadding对话框
     */
    public void startLoadingProgress() {
        if (BaseActivity.this != null) {
            if (BaseActivity.this instanceof Activity) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ViewGroup rootView = (ViewGroup) (BaseActivity.this).getWindow().getDecorView().findViewById(android.R.id.content);
                        if (rootView.getChildAt(rootView.getChildCount() - 1) instanceof LoadingDialog) {
                            return;
                        }
                        mLoadingBar = new LoadingDialog(BaseActivity.this);
                        mLoadingBar.getBackground().setAlpha(0);
                        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
                        rootView.addView(mLoadingBar, params);
                        mLoadingBar.hideSoftInput();

                    }
                });
            }
        }
    }

    /***
     * 请求网络完成时候关闭loadding对话框
     */
    public void dismissLoadingprogress() {
        if (BaseActivity.this != null && null != mLoadingBar) {
            if (BaseActivity.this instanceof Activity) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ViewGroup rootView = (ViewGroup) (BaseActivity.this).getWindow().getDecorView().findViewById(android.R.id.content);
                        mLoadingBar.dismiss();
                        rootView.removeView(mLoadingBar);
                    }

                });
            }
        }
    }
}

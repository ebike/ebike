package com.jcsoft.emsystem.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.activity.LoginActivity;
import com.jcsoft.emsystem.callback.DSingleDialogCallback;
import com.jcsoft.emsystem.constants.AppConfig;
import com.jcsoft.emsystem.event.FinishActivityEvent;
import com.jcsoft.emsystem.event.OnlineExceptionEvent;
import com.jcsoft.emsystem.utils.CommonUtils;
import com.jcsoft.emsystem.utils.DensityUtil;
import com.jcsoft.emsystem.utils.PreferencesUtil;
import com.jcsoft.emsystem.view.LoadingDialog;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import de.greenrobot.event.EventBus;


/**
 * 所有activity的父类
 */
public abstract class BaseActivity extends FragmentActivity {

    private static final int MSG_SET_ALIAS = 1001;
    private Handler mHandler = new Handler();
    private LoadingDialog mLoadingBar;
    protected PreferencesUtil preferencesUtil;
    protected DensityUtil densityUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesUtil = PreferencesUtil.getInstance();
        densityUtil = new DensityUtil(BaseActivity.this);
        EventBus.getDefault().register(this);
        loadXml();
        initStatusBar();
        getIntentData(savedInstanceState);
        init();
        setListener();
        setData();
    }

    //初始化状态栏
    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        if (this.getLocalClassName().equals("activity.SplashActivity")
                || this.getLocalClassName().equals("activity.LoginActivity")) {
            tintManager.setStatusBarTintResource(R.drawable.bg_status);
        } else {
            tintManager.setStatusBarTintResource(R.color.stateBarColor);
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (AppConfig.isDisabled) {
            switch (AppConfig.eventType) {
                case 10:
                    CommonUtils.showCustomDialogSignle2(this, "", AppConfig.eventMsg, new DSingleDialogCallback() {
                        @Override
                        public void onPositiveButtonClick(String editText) {
                            logout();
                            AppConfig.isDisabled = false;
                            AppConfig.eventType = 0;
                            AppConfig.eventMsg = "";
                        }
                    });
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

    //设置别名
    protected void setAlias() {
        // 调用 Handler 来异步设置别名
        aliasHandler.sendMessage(aliasHandler.obtainMessage(MSG_SET_ALIAS, AppConfig.imei));
    }

    //设置别名回调方法
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            switch (code) {
                case 0://成功
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002://超时
                    // 延迟 60 秒来调用 Handler 设置别名
                    aliasHandler.sendMessageDelayed(aliasHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
            }
        }
    };

    //处理设置别名消息
    private final Handler aliasHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
            }
        }
    };

    public void onEvent(OnlineExceptionEvent onlineExceptionEvent) {
        if (onlineExceptionEvent.isFlag()) {
            CommonUtils.showCustomDialogSignle(this, "", onlineExceptionEvent.getMessage(), Gravity.LEFT | Gravity.CENTER_VERTICAL, new DSingleDialogCallback() {
                @Override
                public void onPositiveButtonClick(String editText) {
                    logout();
                }
            });
        }
    }

    public void onEvent(FinishActivityEvent event) {
        if (event != null && event.isFinish() && event.getTarget().equals("BaseActivity")) {
            this.finish();
        }
    }

    public void logout() {
        AppConfig.loginName = "";
        AppConfig.password = "";
        AppConfig.userInfoBean = null;
        preferencesUtil.setPrefString(this, AppConfig.LOGIN_NAME, "");
        preferencesUtil.setPrefString(this, AppConfig.PASSWORD, "");
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        EventBus.getDefault().post(new FinishActivityEvent(true, "BaseActivity"));
    }

}

package com.jcsoft.emsystem.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.widget.RelativeLayout;

import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.base.BaseActivity;
import com.jcsoft.emsystem.callback.DDoubleDialogCallback;
import com.jcsoft.emsystem.utils.CommonUtils;
import com.jcsoft.emsystem.view.CustomDialog;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


public class SplashActivity extends BaseActivity {
    private final static long MILLIS = 2000;// 启动画面延迟时间
    private boolean isReciverRegiest;
    @ViewInject(R.id.rl_splash)
    RelativeLayout splashRelativeLayout;
    //弹出框
    private CustomDialog dialog;
    //是否需要启动
    private boolean isStartUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        x.view().inject(this);
        isStartUp = true;
        init();
    }

    public void init() {
        try {
            if (!CommonUtils.isNetworkConnected(SplashActivity.this)) {
                //添加网络变化监听
                initNoNetReciver();
                if (dialog == null) {
                    dialog = CommonUtils.showNetCustomDialog(SplashActivity.this, "温馨提示", "当前网络不可用，请检查你的网络设置", "设置网络", new DDoubleDialogCallback() {

                        @Override
                        public void onNegativeButtonClick(String editText) {
                            SplashActivity.this.finish();
                            System.exit(0);
                        }

                        @Override
                        public void onPositiveButtonClick(String editText) {
                            //在Android版本10以下，调用的是：ACTION_WIRELESS_SETTINGS，版本在10以上的调用：ACTION_SETTINGS。
                            if (android.os.Build.VERSION.SDK_INT > 10) {
                                // 3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
                                startActivity(new Intent(Settings.ACTION_SETTINGS));
                            } else {
                                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                            }
                        }
                    });
                } else if (dialog != null && !dialog.isShowing()) {
                    dialog.show();
                }
                return;
            } else {
                if (isStartUp) {
                    isStartUp = false;
                    splashRelativeLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, MILLIS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //网络变化广播处理
    private BroadcastReceiver mLoadingConnectReciver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            init();
        }
    };

    //添加网络变化广播监听
    private void initNoNetReciver() {
        IntentFilter connectFilter = new IntentFilter();
        connectFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mLoadingConnectReciver, connectFilter);
        isReciverRegiest = true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            System.exit(0);
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销网络变化广播接收器
        if (mLoadingConnectReciver != null && isReciverRegiest == true) {
            unregisterReceiver(mLoadingConnectReciver);
        }
    }
}

package com.jcsoft.emsystem.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.AMapNaviViewOptions;
import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.event.StopNaviEvent;
import com.jcsoft.emsystem.map.TTSController;
import com.jcsoft.emsystem.utils.Utils;

import de.greenrobot.event.EventBus;

/**
 * 导航界面
 */
public class SimpleNaviActivity extends Activity implements
        AMapNaviViewListener {
    //导航View
    private AMapNaviView mAmapAMapNaviView;
    //是否为模拟导航
    private boolean mIsEmulatorNavi = true;
    //记录有哪个页面跳转而来，处理返回键
    private int mCode = -1;

    public static SimpleNaviActivity CurrentSimpleNaviActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simplenavi);
        Bundle bundle = getIntent().getExtras();
        processBundle(bundle);
        init(savedInstanceState);

        CurrentSimpleNaviActivity = this;
    }

    private void processBundle(Bundle bundle) {
        if (bundle != null) {
            mIsEmulatorNavi = bundle.getBoolean(Utils.ISEMULATOR, true);
            mCode = bundle.getInt(Utils.ACTIVITYINDEX);
        }
    }

    /**
     * 初始化
     *
     * @param savedInstanceState
     */
    private void init(Bundle savedInstanceState) {
        mAmapAMapNaviView = (AMapNaviView) findViewById(R.id.simplenavimap);
        mAmapAMapNaviView.onCreate(savedInstanceState);
        mAmapAMapNaviView.setAMapNaviViewListener(this);

        AMapNaviViewOptions options = mAmapAMapNaviView.getViewOptions();
        options.setLeaderLineEnabled(Color.rgb(0x41, 0x69, 0xE1));
        options.setScreenAlwaysBright(true);
        mAmapAMapNaviView.setViewOptions(options);

        TTSController.getInstance(this).startSpeaking();
        if (mIsEmulatorNavi) {
            // 设置模拟速度
            AMapNavi.getInstance(this).setEmulatorNaviSpeed(100);
            // 开启模拟导航
            AMapNavi.getInstance(this).startNavi(AMapNavi.EmulatorNaviMode);

        } else {
            // 开启实时导航
            AMapNavi.getInstance(this).startNavi(AMapNavi.GPSNaviMode);
        }
    }

//-----------------------------导航界面回调事件------------------------

    /**
     * 导航界面返回按钮监听
     */
    @Override
    public void onNaviCancel() {
        //startMainActivity();
        CurrentSimpleNaviActivity = null;
        finish();
    }

    private void startMainActivity() {
        Intent intent = new Intent(SimpleNaviActivity.this,
                MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }


    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviMapMode(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNaviTurnClick() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNextRoadClick() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onScanViewButtonClick() {
        // TODO Auto-generated method stub

    }

    /**
     * 返回键监听事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mCode == Utils.SIMPLEROUTENAVI) {
                //startMainActivity();
                CurrentSimpleNaviActivity = null;
                finish();

            } else if (mCode == Utils.SIMPLEGPSNAVI) {
                //startMainActivity();
                CurrentSimpleNaviActivity = null;
                finish();
            } else {
                CurrentSimpleNaviActivity = null;
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    // ------------------------------生命周期方法---------------------------
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mAmapAMapNaviView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAmapAMapNaviView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mAmapAMapNaviView.onPause();
        AMapNavi.getInstance(this).stopNavi();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAmapAMapNaviView.onDestroy();

        TTSController.getInstance(this).stopSpeaking();
        EventBus.getDefault().post(new StopNaviEvent(true));
    }

    @Override
    public void onLockMap(boolean arg0) {

        // TODO Auto-generated method stub

    }


}

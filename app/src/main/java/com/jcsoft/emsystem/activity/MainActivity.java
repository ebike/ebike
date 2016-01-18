package com.jcsoft.emsystem.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.adapter.ViewPagerFragmentAdapter;
import com.jcsoft.emsystem.base.BaseActivity;
import com.jcsoft.emsystem.base.TabIndicator;
import com.jcsoft.emsystem.constants.AppConfig;
import com.jcsoft.emsystem.fragment.AlarmMessageFragment;
import com.jcsoft.emsystem.fragment.ChartFragment;
import com.jcsoft.emsystem.fragment.LocationFragment;
import com.jcsoft.emsystem.fragment.MyFragment;
import com.jcsoft.emsystem.utils.ViewPagerUtils;
import com.jcsoft.emsystem.view.NotSlideViewPager;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.view_pager)
    NotSlideViewPager viewPager;
    @ViewInject(R.id.ll_location)
    LinearLayout locationLinearLayout;
    @ViewInject(R.id.iv_location)
    ImageView locationImageView;
    @ViewInject(R.id.tv_location)
    TextView locationTextView;
    @ViewInject(R.id.ll_alarm)
    LinearLayout alarmLinearLayout;
    @ViewInject(R.id.iv_alarm)
    ImageView alarmImageView;
    @ViewInject(R.id.tv_alarm)
    TextView alarmTextView;
    @ViewInject(R.id.ll_chart)
    LinearLayout chartLinearLayout;
    @ViewInject(R.id.iv_chart)
    ImageView chartImageView;
    @ViewInject(R.id.tv_chart)
    TextView chartTextView;
    @ViewInject(R.id.ll_my)
    LinearLayout myLinearLayout;
    @ViewInject(R.id.iv_my)
    ImageView myImageView;
    @ViewInject(R.id.tv_my)
    TextView myTextView;
    private List<ImageView> imageViews;
    private List<TextView> textViews;
    private List<Fragment> fragmentList;
    private List<TabIndicator> tabIndicatorList;
    private ViewPagerFragmentAdapter viewPagerFragmentAdapter;
    private long mExitTime = 0;
    private int fragmentPosition = 0;

    @Override
    public void loadXml() {
        setContentView(R.layout.activity_main);
        x.view().inject(this);
    }

    @Override
    public void getIntentData(Bundle savedInstanceState) {

    }

    @Override
    public void init() {
        //初始化缓存
        AppConfig.loginName = preferencesUtil.getPrefString(MainActivity.this, AppConfig.LOGIN_NAME, "");
        AppConfig.password = preferencesUtil.getPrefString(MainActivity.this, AppConfig.PASSWORD, "");
        //初始化viewpager
        imageViews = new ArrayList<ImageView>();
        imageViews.add(locationImageView);
        imageViews.add(alarmImageView);
        imageViews.add(chartImageView);
        imageViews.add(myImageView);
        textViews = new ArrayList<TextView>();
        textViews.add(locationTextView);
        textViews.add(alarmTextView);
        textViews.add(chartTextView);
        textViews.add(myTextView);
        //初始化ViewPager
        viewPager.setOffscreenPageLimit(4);
        // 包含4个fragment界面
        tabIndicatorList = ViewPagerUtils.getTabIndicator(4);
        // 4个fragment界面封装
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new LocationFragment());
        fragmentList.add(new AlarmMessageFragment());
        fragmentList.add(new ChartFragment());
        fragmentList.add(new MyFragment());
        // 设置ViewPager适配器
        viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), tabIndicatorList, fragmentList);
        viewPager.setAdapter(viewPagerFragmentAdapter);
    }

    @Override
    public void setListener() {
        locationLinearLayout.setOnClickListener(this);
        alarmLinearLayout.setOnClickListener(this);
        chartLinearLayout.setOnClickListener(this);
        myLinearLayout.setOnClickListener(this);
    }

    @Override
    public void setData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_location:
                fragmentPosition = 0;
                viewPager.setCurrentItem(fragmentPosition, false);
                ViewPagerUtils.setBottomBar(MainActivity.this, fragmentPosition, textViews, imageViews);
                break;
            case R.id.ll_alarm:
                fragmentPosition = 1;
                viewPager.setCurrentItem(fragmentPosition, false);
                ViewPagerUtils.setBottomBar(MainActivity.this, fragmentPosition, textViews, imageViews);
                break;
            case R.id.ll_chart:
                fragmentPosition = 2;
                viewPager.setCurrentItem(fragmentPosition, false);
                ViewPagerUtils.setBottomBar(MainActivity.this, fragmentPosition, textViews, imageViews);
                break;
            case R.id.ll_my:
                fragmentPosition = 3;
                viewPager.setCurrentItem(fragmentPosition, false);
                ViewPagerUtils.setBottomBar(MainActivity.this, fragmentPosition, textViews, imageViews);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                mExitTime = System.currentTimeMillis();
                showLongText("再按一次退出程序");
            } else {
                this.finish();
            }
            return true;
        }
        return false;
    }
}

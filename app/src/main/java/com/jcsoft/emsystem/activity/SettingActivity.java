package com.jcsoft.emsystem.activity;

import android.os.Bundle;

import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.base.BaseActivity;

import org.xutils.x;

public class SettingActivity extends BaseActivity {


    @Override
    public void loadXml() {
        setContentView(R.layout.activity_setting);
        x.view().inject(this);
    }

    @Override
    public void getIntentData(Bundle savedInstanceState) {

    }

    @Override
    public void init() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void setData() {

    }
}

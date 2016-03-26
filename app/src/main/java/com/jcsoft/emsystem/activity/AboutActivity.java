package com.jcsoft.emsystem.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.base.BaseActivity;
import com.jcsoft.emsystem.utils.CommonUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 关于
 */
public class AboutActivity extends BaseActivity {
    @ViewInject(R.id.tv_logo)
    TextView logoTextView;

    @Override
    public void loadXml() {
        setContentView(R.layout.activity_about);
        x.view().inject(this);
    }

    @Override
    public void getIntentData(Bundle savedInstanceState) {

    }

    @Override
    public void init() {
        String versionName = CommonUtils.getVersionName(this);
        logoTextView.setText("For Android V" + versionName);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void setData() {

    }
}

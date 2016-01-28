package com.jcsoft.emsystem.activity;

import android.os.Bundle;

import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.base.BaseAreaActivity;
import com.jcsoft.emsystem.view.RowLabelEditView;
import com.jcsoft.emsystem.view.RowLabelValueView;
import com.jcsoft.emsystem.view.TopBarView;

import org.xutils.view.annotation.ViewInject;

/**
 * 预约安装
 */
public class BookInstallActivity extends BaseAreaActivity {
    @ViewInject(R.id.top_bar)
    TopBarView topBarView;
    @ViewInject(R.id.rlev_name)
    RowLabelEditView nameRowLabelEditView;
    @ViewInject(R.id.rlev_phone)
    RowLabelEditView phoneRowLabelEditView;
    @ViewInject(R.id.rlvv_area)
    RowLabelValueView areaRowLabelValueView;
    @ViewInject(R.id.rlev_address)
    RowLabelEditView addressRowLabelEditView;
    @ViewInject(R.id.rlev_install_count)
    RowLabelEditView installCountRowLabelEditView;

    @Override
    public void loadXml() {
        setContentView(R.layout.activity_book_install);

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

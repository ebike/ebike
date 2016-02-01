package com.jcsoft.emsystem.activity;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.base.BaseActivity;
import com.jcsoft.emsystem.bean.ResponseBean;
import com.jcsoft.emsystem.bean.UserInfoBean;
import com.jcsoft.emsystem.callback.DCommonCallback;
import com.jcsoft.emsystem.constants.AppConfig;
import com.jcsoft.emsystem.http.DHttpUtils;
import com.jcsoft.emsystem.http.DRequestParamsUtils;
import com.jcsoft.emsystem.http.HttpConstants;
import com.jcsoft.emsystem.view.RowLabelValueView;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 基本资料
 */
public class BaseInformationActivity extends BaseActivity {
    @ViewInject(R.id.rlvv_equipment_serial_number)
    RowLabelValueView equipmentSerialNumberRowLabelValueView;
    @ViewInject(R.id.rlvv_imei)
    RowLabelValueView imeiRowLabelValueView;
    @ViewInject(R.id.rlvv_data_card)
    RowLabelValueView dataCardRowLabelValueView;
    @ViewInject(R.id.rlvv_open_date)
    RowLabelValueView openDateRowLabelValueView;
    @ViewInject(R.id.rlvv_due_date)
    RowLabelValueView dueDateRowLabelValueView;
    @ViewInject(R.id.rlvv_name)
    RowLabelValueView nameRowLabelValueView;
    @ViewInject(R.id.rlvv_sex)
    RowLabelValueView sexRowLabelValueView;
    @ViewInject(R.id.rlvv_id_card)
    RowLabelValueView idCardRowLabelValueView;
    @ViewInject(R.id.rlvv_phone)
    RowLabelValueView phoneRowLabelValueView;
    @ViewInject(R.id.rlvv_area)
    RowLabelValueView areaRowLabelValueView;
    @ViewInject(R.id.rlvv_address)
    RowLabelValueView addressRowLabelValueView;
    //用户信息
    private UserInfoBean userInfoBean;

    @Override
    public void loadXml() {
        setContentView(R.layout.activity_base_information);
        x.view().inject(this);
    }

    @Override
    public void getIntentData(Bundle savedInstanceState) {

    }

    @Override
    public void init() {
        if (userInfoBean != null) {
            equipmentSerialNumberRowLabelValueView.setValue(userInfoBean.getCarId() + "");
            imeiRowLabelValueView.setValue(userInfoBean.getImei() + "");
            dataCardRowLabelValueView.setValue(userInfoBean.getTelNum());
            openDateRowLabelValueView.setValue(userInfoBean.getActiveDate());
            dueDateRowLabelValueView.setValue(userInfoBean.getExpireDate());
            nameRowLabelValueView.setValue(userInfoBean.getUserName());
            if (userInfoBean.getSex() == 0) {
                sexRowLabelValueView.setValue("男");
            } else if (userInfoBean.getSex() == 1) {
                sexRowLabelValueView.setValue("女");
            }
            idCardRowLabelValueView.setValue(userInfoBean.getIdNum());
            phoneRowLabelValueView.setValue(userInfoBean.getPhone());
            areaRowLabelValueView.setValue(userInfoBean.getProvince() + "-" + userInfoBean.getCity() + "-" + userInfoBean.getArea());
            addressRowLabelValueView.setValue(userInfoBean.getAddress());
        }
    }

    @Override
    public void setListener() {

    }

    @Override
    public void setData() {
        RequestParams params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.getUserInfo());
        DHttpUtils.get_String(this, true, params, new DCommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ResponseBean<UserInfoBean> bean = new Gson().fromJson(result, new TypeToken<ResponseBean<UserInfoBean>>() {
                }.getType());
                if (bean.getCode() == 1) {
                    userInfoBean = bean.getData();
                    //更新界面
                    init();
                    //更新缓存
                    AppConfig.userInfoBean = bean.getData();
                } else {
                    showShortText(bean.getErrmsg());
                }
            }
        });
    }
}

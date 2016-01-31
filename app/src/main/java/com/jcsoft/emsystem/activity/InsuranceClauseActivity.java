package com.jcsoft.emsystem.activity;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.base.BaseActivity;
import com.jcsoft.emsystem.bean.InsurInfoBean;
import com.jcsoft.emsystem.bean.ResponseBean;
import com.jcsoft.emsystem.callback.DCommonCallback;
import com.jcsoft.emsystem.http.DHttpUtils;
import com.jcsoft.emsystem.http.HttpConstants;
import com.jcsoft.emsystem.utils.CommonUtils;
import com.jcsoft.emsystem.view.RowLabelValueView;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;

/**
 * 保险信息
 */
public class InsuranceClauseActivity extends BaseActivity {
    @ViewInject(R.id.rlvv_equipment_serial_number)
    RowLabelValueView equipmentSerialNumberRowLabelValueView;
    @ViewInject(R.id.rlvv_policy_number)
    RowLabelValueView policyNumberRowLabelValueView;
    @ViewInject(R.id.rlvv_start_date)
    RowLabelValueView startDateRowLabelValueView;
    @ViewInject(R.id.rlvv_used_date)
    RowLabelValueView usedDateRowLabelValueView;
    @ViewInject(R.id.rlvv_due_date)
    RowLabelValueView dueDateRowLabelValueView;
    @ViewInject(R.id.rlvv_insured_person)
    RowLabelValueView insuredPersonRowLabelValueView;
    @ViewInject(R.id.rlvv_id_card)
    RowLabelValueView idCardRowLabelValueView;
    @ViewInject(R.id.rlvv_phone)
    RowLabelValueView phoneRowLabelValueView;
    @ViewInject(R.id.rlvv_coverage_area)
    RowLabelValueView coverageAreaRowLabelValueView;
    //保险条款
    private InsurInfoBean insurInfoBean;

    @Override
    public void loadXml() {
        setContentView(R.layout.activity_insurance_clause);
        x.view().inject(this);
    }

    @Override
    public void getIntentData(Bundle savedInstanceState) {

    }

    @Override
    public void init() {
        if (insurInfoBean != null) {
            equipmentSerialNumberRowLabelValueView.setValue(insurInfoBean.getCarId() + "");
            policyNumberRowLabelValueView.setValue(insurInfoBean.getInsurNum());
            startDateRowLabelValueView.setValue(CommonUtils.DateToString(new Date(insurInfoBean.getStartDate()), "yyyy-MM-dd"));
            usedDateRowLabelValueView.setValue(CommonUtils.AfterAWeekDate(insurInfoBean.getStartDate()));
            dueDateRowLabelValueView.setValue(CommonUtils.DateToString(new Date(insurInfoBean.getEndDate()),"yyyy-MM-dd"));
            insuredPersonRowLabelValueView.setValue(insurInfoBean.getUserName());
            idCardRowLabelValueView.setValue(insurInfoBean.getIdNum());
            phoneRowLabelValueView.setValue(insurInfoBean.getPhone());
            coverageAreaRowLabelValueView.setValue(insurInfoBean.getProvince() + " " + insurInfoBean.getCity());
        }
    }

    @Override
    public void setListener() {

    }

    @Override
    public void setData() {
        RequestParams params = new RequestParams(HttpConstants.getInsurInfoUrl());
        DHttpUtils.get_String(this, true, params, new DCommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ResponseBean<InsurInfoBean> bean = new Gson().fromJson(result, new TypeToken<ResponseBean<InsurInfoBean>>() {
                }.getType());
                if (bean.getCode() == 1) {
                    insurInfoBean = bean.getData();
                    //更新界面
                    init();
                } else {
                    showShortText(bean.getErrmsg());
                }
            }
        });
    }
}

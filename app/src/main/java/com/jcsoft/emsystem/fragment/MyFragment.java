package com.jcsoft.emsystem.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.activity.BaseInformationActivity;
import com.jcsoft.emsystem.constants.AppConfig;
import com.jcsoft.emsystem.view.RowEntryView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by jimmy on 15/12/28.
 */
public class MyFragment extends BaseFragment implements RowEntryView.OnClickCallback {
    @ViewInject(R.id.tv_name)
    TextView nameTextView;
    @ViewInject(R.id.tv_equipment_serial_number)
    TextView equipmentSerialNumberTextView;
    @ViewInject(R.id.rev_base_info)
    RowEntryView baseInfoRowEntryView;
    @ViewInject(R.id.rev_car_info)
    RowEntryView carInfoRowEntryView;
    @ViewInject(R.id.rev_remote_lock_car)
    RowEntryView remoteLockCarRowEntryView;
    @ViewInject(R.id.rev_deal_insurance)
    RowEntryView dealInsuranceRowEntryView;
    @ViewInject(R.id.rev_insurance_clause)
    RowEntryView insuranceClauseRowEntryView;
    @ViewInject(R.id.rev_setting)
    RowEntryView settingRowEntryView;
    //标志位，标志已经初始化完成
    private boolean isPrepared;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        x.view().inject(this, view);
        isPrepared = true;
        init();
        initListener();
        //获取车辆基本信息
//        getCarInfo();
        return view;
    }

    private void init() {
        if (AppConfig.userInfoBean != null) {
            nameTextView.setText(AppConfig.userInfoBean.getUserName());
            equipmentSerialNumberTextView.setText(getResources().getString(R.string.equipment_serial_number) + AppConfig.userInfoBean.getCarId());
        }
    }

    private void initListener() {
        baseInfoRowEntryView.setOnClickCallback(this);
        carInfoRowEntryView.setOnClickCallback(this);
        remoteLockCarRowEntryView.setOnClickCallback(this);
        dealInsuranceRowEntryView.setOnClickCallback(this);
        insuranceClauseRowEntryView.setOnClickCallback(this);
        settingRowEntryView.setOnClickCallback(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rev_base_info://基本资料
                intent = new Intent(getActivity(), BaseInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.rev_car_info://车辆资料

                break;
            case R.id.rev_remote_lock_car://远程锁车

                break;
            case R.id.rev_deal_insurance://办理保险

                break;
            case R.id.rev_insurance_clause://保险条款

                break;
            case R.id.rev_setting://设置

                break;
        }
    }

    @Override
    public void requestDatas() {

    }

    //获取车辆基本信息
//    private void getCarInfo() {
//        RequestParams params = new RequestParams(HttpConstants.getCarInfoUrl());
//        DHttpUtils.get_String((MainActivity) getActivity(), false, params, new DCommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                Log.e("getCarInfo", "result:" + result);
//            }
//        });
//    }
}

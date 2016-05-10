package com.jcsoft.emsystem.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.activity.AboutActivity;
import com.jcsoft.emsystem.activity.BaseInformationActivity;
import com.jcsoft.emsystem.activity.CarInformationActivity;
import com.jcsoft.emsystem.activity.DealInsuranceActivity;
import com.jcsoft.emsystem.activity.InsuranceClauseActivity;
import com.jcsoft.emsystem.activity.MainActivity;
import com.jcsoft.emsystem.activity.WebActivity;
import com.jcsoft.emsystem.callback.DSingleDialogCallback;
import com.jcsoft.emsystem.constants.AppConfig;
import com.jcsoft.emsystem.utils.CommonUtils;
import com.jcsoft.emsystem.view.RowEntryView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by jimmy on 15/12/28.
 */
public class MyFragment extends BaseFragment implements RowEntryView.OnClickCallback, View.OnClickListener {
    @ViewInject(R.id.tv_name)
    TextView nameTextView;
    @ViewInject(R.id.tv_equipment_serial_number)
    TextView equipmentSerialNumberTextView;
    @ViewInject(R.id.rev_base_info)
    RowEntryView baseInfoRowEntryView;
    @ViewInject(R.id.rev_car_info)
    RowEntryView carInfoRowEntryView;
    @ViewInject(R.id.rev_deal_insurance)
    RowEntryView dealInsuranceRowEntryView;
    @ViewInject(R.id.rev_insurance_clause)
    RowEntryView insuranceClauseRowEntryView;
    @ViewInject(R.id.rev_terms_of_service)
    RowEntryView termsOfServiceRowEntryView;
    @ViewInject(R.id.rev_contact_customer_service)
    RowEntryView contactCustomerServiceRowEntryView;
    @ViewInject(R.id.rev_about)
    RowEntryView aboutRowEntryView;
    @ViewInject(R.id.tv_logout)
    TextView logoutTextView;
    //标志位，标志已经初始化完成
    private boolean isPrepared;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        x.view().inject(this, view);
        isPrepared = true;
        init();
        initListener();
        return view;
    }

    private void init() {
        if (AppConfig.userInfoBean != null) {
            nameTextView.setText(AppConfig.userInfoBean.getUserName());
            equipmentSerialNumberTextView.setText(getResources().getString(R.string.equipment_serial_number) + AppConfig.userInfoBean.getCarId());
        }
        if (!CommonUtils.strIsEmpty(AppConfig.userInfoBean.getInsurNum())) {
            dealInsuranceRowEntryView.setTitleTextView(getResources().getString(R.string.my_insurance_info));
        } else {
            dealInsuranceRowEntryView.setTitleTextView(getResources().getString(R.string.my_deal_insurance));
        }
    }

    private void initListener() {
        baseInfoRowEntryView.setOnClickCallback(this);
        carInfoRowEntryView.setOnClickCallback(this);
        dealInsuranceRowEntryView.setOnClickCallback(this);
        insuranceClauseRowEntryView.setOnClickCallback(this);
        termsOfServiceRowEntryView.setOnClickCallback(this);
        contactCustomerServiceRowEntryView.setOnClickCallback(this);
        aboutRowEntryView.setOnClickCallback(this);
        logoutTextView.setOnClickListener(this);
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
                intent = new Intent(getActivity(), CarInformationActivity.class);
                intent.putExtra("leftText", "我");
                startActivity(intent);
                break;
            case R.id.rev_deal_insurance://办理保险
                if (!CommonUtils.strIsEmpty(AppConfig.userInfoBean.getInsurNum())) {
                    intent = new Intent(getActivity(), InsuranceClauseActivity.class);
                    startActivity(intent);
                } else if (CommonUtils.strIsEmpty(AppConfig.userInfoBean.getInsurNum())
                        && !CommonUtils.strIsEmpty(AppConfig.userInfoBean.getInsurUpdateTime())) {
                    CommonUtils.showCustomDialogSignle3(getActivity(), "", "您的资料已提交，无需再次操作");
                } else {
                    intent = new Intent(getActivity(), DealInsuranceActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.rev_insurance_clause://保险条款
                intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("title", getResources().getString(R.string.top_bar_insurance_policy));
                intent.putExtra("url", "http://api.gnets.cn/app/h5/insurance_policy.html");
                startActivity(intent);
                break;
            case R.id.rev_terms_of_service://服务条款
                intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("title", getResources().getString(R.string.top_bar_terms_of_service));
                intent.putExtra("url", "http://api.gnets.cn/app/h5/service_terms.html");
                startActivity(intent);
                break;
            case R.id.rev_contact_customer_service://联系客服
                CommonUtils.showCustomDialog3(getActivity(), "呼叫", "取消", "", "0531-67805000", new DSingleDialogCallback() {
                    @Override
                    public void onPositiveButtonClick(String editText) {
                        // 用intent启动拨打电话
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:053167805000"));
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case R.id.rev_about://关于
                intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_logout://退出
                CommonUtils.showCustomDialog0(getActivity(), "", "您确定要退出登录吗？", new DSingleDialogCallback() {
                    @Override
                    public void onPositiveButtonClick(String editText) {
                        ((MainActivity) getActivity()).logout();
                    }
                });
                break;
        }
    }

    @Override
    public void requestDatas() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

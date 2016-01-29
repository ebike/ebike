package com.jcsoft.emsystem.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.activity.AboutActivity;
import com.jcsoft.emsystem.activity.BaseInformationActivity;
import com.jcsoft.emsystem.activity.CarInformationActivity;
import com.jcsoft.emsystem.activity.DealInsuranceActivity;
import com.jcsoft.emsystem.activity.InsuranceClauseActivity;
import com.jcsoft.emsystem.activity.MainActivity;
import com.jcsoft.emsystem.activity.WebActivity;
import com.jcsoft.emsystem.bean.ResponseBean;
import com.jcsoft.emsystem.callback.DCommonCallback;
import com.jcsoft.emsystem.callback.DSingleDialogCallback;
import com.jcsoft.emsystem.constants.AppConfig;
import com.jcsoft.emsystem.event.RemoteLockCarEvent;
import com.jcsoft.emsystem.http.DHttpUtils;
import com.jcsoft.emsystem.http.HttpConstants;
import com.jcsoft.emsystem.utils.CommonUtils;
import com.jcsoft.emsystem.view.RowEntryView;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import de.greenrobot.event.EventBus;

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
    @ViewInject(R.id.rev_remote_lock_car)
    RowEntryView remoteLockCarRowEntryView;
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
        EventBus.getDefault().register(this);
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
        remoteLockCarRowEntryView.setOnClickCallback(this);
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
                startActivity(intent);
                break;
            case R.id.rev_remote_lock_car://远程锁车/解锁
                if (AppConfig.isExecuteLock != null) {
                    if (AppConfig.isExecuteLock == 1) {
                        CommonUtils.showCustomDialogSignle3(getActivity(), "", "正在执行锁车命令，请稍等。");
                    } else if (AppConfig.isExecuteLock == 0) {
                        CommonUtils.showCustomDialogSignle3(getActivity(), "", "正在执行解锁命令，请稍等。");
                    }
                } else {
                    if (AppConfig.isLock) {
                        //解锁
                        CommonUtils.showCustomDialog0(getActivity(), "提示", "您确定要执行远程解锁吗？", new DSingleDialogCallback() {
                            @Override
                            public void onPositiveButtonClick(String editText) {
                                RequestParams params = new RequestParams(HttpConstants.getUnLockBikeUrl());
                                DHttpUtils.get_String((MainActivity) getActivity(), true, params, new DCommonCallback<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        ResponseBean<String> responseBean = new Gson().fromJson(result, new TypeToken<ResponseBean<String>>() {
                                        }.getType());
                                        if (responseBean != null) {
                                            AppConfig.isExecuteLock = 0;
                                            AppConfig.lockCarType = 2;
                                            showShortText("解锁命令发送成功");
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        //锁车
                        CommonUtils.showCustomDialog0(getActivity(), "提示", "您确定要执行远程锁车吗？", new DSingleDialogCallback() {
                            @Override
                            public void onPositiveButtonClick(String editText) {
                                RequestParams params = new RequestParams(HttpConstants.getlockBikeUrl());
                                DHttpUtils.get_String((MainActivity) getActivity(), true, params, new DCommonCallback<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        ResponseBean<String> responseBean = new Gson().fromJson(result, new TypeToken<ResponseBean<String>>() {
                                        }.getType());
                                        if (responseBean != null) {
                                            AppConfig.isExecuteLock = 1;
                                            AppConfig.lockCarType = 2;
                                            showShortText("锁车命令发送成功");
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
                break;
            case R.id.rev_deal_insurance://办理保险
                if (!CommonUtils.strIsEmpty(AppConfig.userInfoBean.getInsurNum())) {
                    intent = new Intent(getActivity(), InsuranceClauseActivity.class);
                } else {
                    intent = new Intent(getActivity(), DealInsuranceActivity.class);
                }
                startActivity(intent);
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
        if (AppConfig.isLock) {
            remoteLockCarRowEntryView.setTitleTextView(getResources().getString(R.string.my_remote_open_car));
        } else {
            remoteLockCarRowEntryView.setTitleTextView(getResources().getString(R.string.my_remote_lock_car));
        }
    }

    //处理远程锁车推送
    public void onEvent(RemoteLockCarEvent event) {
        if (event != null) {
            AppConfig.isExecuteLock = null;
            if (event.getIsLock().equals("1")) {
                remoteLockCarRowEntryView.setTitleTextView(getResources().getString(R.string.my_remote_open_car));
                AppConfig.isLock = true;
                if (AppConfig.lockCarType == 2) {
                    showShortText("锁车成功");
                }
            } else {
                remoteLockCarRowEntryView.setTitleTextView(getResources().getString(R.string.my_remote_lock_car));
                AppConfig.isLock = false;
                if (AppConfig.lockCarType == 2) {
                    showShortText("解锁成功");
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

package com.jcsoft.emsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.base.BaseActivity;
import com.jcsoft.emsystem.base.LocationJson;
import com.jcsoft.emsystem.bean.ResponseBean;
import com.jcsoft.emsystem.bean.UserInfoBean;
import com.jcsoft.emsystem.callback.DCommonCallback;
import com.jcsoft.emsystem.constants.AppConfig;
import com.jcsoft.emsystem.db.ProvinceInfoDao;
import com.jcsoft.emsystem.http.DHttpUtils;
import com.jcsoft.emsystem.http.DRequestParamsUtils;
import com.jcsoft.emsystem.http.HttpConstants;
import com.jcsoft.emsystem.view.RowLabelValueView;
import com.jcsoft.emsystem.view.wheel.AddressThreeWheelViewDialog;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本资料
 */
public class BaseInformationActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.rlvv_equipment_serial_number)
    RowLabelValueView equipmentSerialNumberView;
    @ViewInject(R.id.rlvv_imei)
    RowLabelValueView imeiView;
    @ViewInject(R.id.rlvv_data_card)
    RowLabelValueView dataCardView;
    @ViewInject(R.id.rlvv_open_date)
    RowLabelValueView openDateView;
    @ViewInject(R.id.rlvv_due_date)
    RowLabelValueView dueDateView;
    @ViewInject(R.id.rlvv_name)
    RowLabelValueView nameView;
    @ViewInject(R.id.rlvv_sex)
    RowLabelValueView sexView;
    @ViewInject(R.id.rlvv_id_card)
    RowLabelValueView idCardView;
    @ViewInject(R.id.rlvv_phone)
    RowLabelValueView phoneView;
    @ViewInject(R.id.rlvv_area)
    RowLabelValueView areaView;
    @ViewInject(R.id.rlvv_address)
    RowLabelValueView addressView;
    //用户信息
    private UserInfoBean userInfoBean;

    private AddressThreeWheelViewDialog dialog;
    private ProvinceInfoDao provinceDao;
    private List<LocationJson> mProvinceList;

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
            equipmentSerialNumberView.setValue(userInfoBean.getCarId() + "");
            imeiView.setValue(userInfoBean.getImei() + "");
            dataCardView.setValue(userInfoBean.getTelNum());
            openDateView.setValue(userInfoBean.getActiveDate());
            dueDateView.setValue(userInfoBean.getExpireDate());
            nameView.setValue(userInfoBean.getUserName());
            if (userInfoBean.getSex() == 0) {
                sexView.setValue("男");
            } else if (userInfoBean.getSex() == 1) {
                sexView.setValue("女");
            }
            idCardView.setValue(userInfoBean.getIdNum());
            phoneView.setValue(userInfoBean.getPhone());
            areaView.setValue(userInfoBean.getProvince() + "-" + userInfoBean.getCity() + "-" + userInfoBean.getArea());
            addressView.setValue(userInfoBean.getAddress());
        }

        dialog = new AddressThreeWheelViewDialog(this);
        provinceDao = new ProvinceInfoDao(this);
        mProvinceList = provinceDao.queryAll();
    }

    @Override
    public void setListener() {
        nameView.setOnClickListener(this);
        sexView.setOnClickListener(this);
        phoneView.setOnClickListener(this);
        idCardView.setOnClickListener(this);
        areaView.setOnClickListener(this);
        addressView.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rlvv_name:
                intent = new Intent(this, UpdateTextValueActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("fieldName_CH", getString(R.string.base_information_name));
                intent.putExtra("fieldValue", userInfoBean.getUserName());
                intent.putExtra("fieldName", "userName");
                intent.putExtra("length", 32);
                startActivity(intent);
                break;
            case R.id.rlvv_sex:
                startActivity(new Intent(this, UpdateSexActivity.class));
                break;
            case R.id.rlvv_phone:
                intent = new Intent(this, UpdateTextValueActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("fieldName_CH", getString(R.string.base_information_phone));
                intent.putExtra("fieldValue", userInfoBean.getPhone());
                intent.putExtra("fieldName", "phone");
                intent.putExtra("inputType", InputType.TYPE_CLASS_PHONE);
                intent.putExtra("length", 11);
                startActivity(intent);
                break;
            case R.id.rlvv_id_card:
                intent = new Intent(this, UpdateTextValueActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("fieldName_CH", getString(R.string.base_information_id_card));
                intent.putExtra("fieldValue", userInfoBean.getIdNum());
                intent.putExtra("fieldName", "idNum");
                intent.putExtra("isIdcard", true);
                intent.putExtra("length", 18);
                startActivity(intent);
                break;
            case R.id.rlvv_area:
                if (userInfoBean != null) {
                    dialog.setData(mProvinceList, userInfoBean.getProvince(), userInfoBean.getCity(), userInfoBean.getArea());
                } else {
                    dialog.setData(mProvinceList);
                }
                dialog.show(new AddressThreeWheelViewDialog.ConfirmAction() {
                    @Override
                    public void doAction(LocationJson root, LocationJson child, LocationJson child2) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("carId", AppConfig.userInfoBean.getCarId() + "");
                        map.put("province", root.getName());
                        map.put("city", child.getName());
                        map.put("area", child2.getName());
                        RequestParams params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.getUpdateUserUrl(), map);
                        DHttpUtils.post_String(BaseInformationActivity.this, true, params, new DCommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                ResponseBean<UserInfoBean> bean = new Gson().fromJson(result, new TypeToken<ResponseBean<UserInfoBean>>() {
                                }.getType());
                                if (bean.getCode() == 1) {
                                    onEvent(bean.getData());
                                    //更新缓存
                                    AppConfig.userInfoBean = bean.getData();
                                } else {
                                    showShortText(bean.getErrmsg());
                                }
                            }
                        });
                    }
                });
                break;
            case R.id.rlvv_address:
                intent = new Intent(this, UpdateTextValueActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("fieldName_CH", getString(R.string.base_information_address));
                intent.putExtra("fieldValue", userInfoBean.getAddress());
                intent.putExtra("fieldName", "address");
                intent.putExtra("length", 64);
                startActivity(intent);
                break;
        }
    }

    public void onEvent(UserInfoBean user) {
        if (user != null) {
            userInfoBean = user;
            init();
        }
    }
}

package com.jcsoft.emsystem.activity;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.base.BaseActivity;
import com.jcsoft.emsystem.bean.CarLikeImeiBean;
import com.jcsoft.emsystem.bean.ResponseBean;
import com.jcsoft.emsystem.callback.DCommonCallback;
import com.jcsoft.emsystem.http.DHttpUtils;
import com.jcsoft.emsystem.http.HttpConstants;
import com.jcsoft.emsystem.utils.CommonUtils;
import com.jcsoft.emsystem.view.RowLabelEditView;
import com.jcsoft.emsystem.view.RowLabelValueView;
import com.jcsoft.emsystem.view.TopBarView;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册账号
 */
public class RegisterActivity extends BaseActivity {
    @ViewInject(R.id.top_bar)
    TopBarView topBarView;
    @ViewInject(R.id.rlev_imei)
    RowLabelEditView imeiRowLabelEditView;
    @ViewInject(R.id.rlev_equipment_serial_number)
    RowLabelValueView equipmentSerialNumberRowLabelValueView;
    @ViewInject(R.id.rlev_data_card)
    RowLabelEditView dataCardRowLabelEditView;
    @ViewInject(R.id.rlev_name)
    RowLabelEditView nameRowLabelEditView;
    @ViewInject(R.id.rlev_phone)
    RowLabelEditView phoneRowLabelEditView;
    @ViewInject(R.id.rlev_id_card)
    RowLabelEditView idCardRowLabelEditView;
    @ViewInject(R.id.rlev_area)
    RowLabelEditView areaRowLabelEditView;
    @ViewInject(R.id.rlev_address)
    RowLabelEditView addressRowLabelEditView;
    @ViewInject(R.id.rlev_dealer_number)
    RowLabelEditView dealerNumberRowLabelEditView;
    @ViewInject(R.id.rlev_salesman_name)
    RowLabelEditView salesmanNameRowLabelEditView;

    @Override
    public void loadXml() {
        setContentView(R.layout.activity_register);
        x.view().inject(this);
    }

    @Override
    public void getIntentData(Bundle savedInstanceState) {

    }

    @Override
    public void init() {
        imeiRowLabelEditView.setEditInteger();
        imeiRowLabelEditView.setEditLength(8);
        phoneRowLabelEditView.setEditInteger();
    }

    @Override
    public void setListener() {
        //根据IMEI码获取设备编号
        imeiRowLabelEditView.setTextChangedCallback(new RowLabelEditView.EditTextChangedCallback() {
            @Override
            public void afterTextChanged() {
                String imei = imeiRowLabelEditView.getValue();
                if (!CommonUtils.strIsEmpty(imei) && imei.length() == 8) {
                    RequestParams params = new RequestParams(HttpConstants.getSearchCarLikeImei(imei));
                    DHttpUtils.get_String(RegisterActivity.this, true, params, new DCommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            ResponseBean<CarLikeImeiBean> bean = new Gson().fromJson(result, new TypeToken<ResponseBean<CarLikeImeiBean>>() {
                            }.getType());
                            if (bean.getCode() == 1) {
                                equipmentSerialNumberRowLabelValueView.setValue(bean.getData().getCarId() + "");
                            } else {
                                showShortText(bean.getErrmsg());
                                imeiRowLabelEditView.setValue("");
                                equipmentSerialNumberRowLabelValueView.setValue("");
                            }
                        }
                    });
                }
            }
        });
        //提交
        topBarView.setRightCallback(new TopBarView.TopBarRightCallback() {
            @Override
            public void setRightOnClickListener() {
                //验证必填
                if (CommonUtils.strIsEmpty(equipmentSerialNumberRowLabelValueView.getValue())) {
                    imeiRowLabelEditView.setValue("");
                    imeiRowLabelEditView.setHintColor(R.color.orange_dark);
                    return;
                }
                if (CommonUtils.strIsEmpty(dataCardRowLabelEditView.getValue())) {
                    dataCardRowLabelEditView.setHintColor(R.color.orange_dark);
                    return;
                }
                if (CommonUtils.strIsEmpty(nameRowLabelEditView.getValue())) {
                    nameRowLabelEditView.setHintColor(R.color.orange_dark);
                    return;
                }
                if (CommonUtils.strIsEmpty(phoneRowLabelEditView.getValue())) {
                    phoneRowLabelEditView.setHintColor(R.color.orange_dark);
                    return;
                }
                if (CommonUtils.strIsEmpty(idCardRowLabelEditView.getValue())) {
                    idCardRowLabelEditView.setHintColor(R.color.orange_dark);
                    return;
                }
                if (CommonUtils.strIsEmpty(areaRowLabelEditView.getValue())) {
                    areaRowLabelEditView.setHintColor(R.color.orange_dark);
                    return;
                }
                if (CommonUtils.strIsEmpty(addressRowLabelEditView.getValue())) {
                    addressRowLabelEditView.setHintColor(R.color.orange_dark);
                    return;
                }
                //整理参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("carId", equipmentSerialNumberRowLabelValueView.getValue());
                map.put("telNum", dataCardRowLabelEditView.getValue());
                map.put("userName", nameRowLabelEditView.getValue());
                map.put("idNum", idCardRowLabelEditView.getValue());
                map.put("phone", phoneRowLabelEditView.getValue());
                map.put("province", "");
                map.put("city", "");
                map.put("area", "");
                map.put("address", addressRowLabelEditView.getValue());
                map.put("dealerId", dealerNumberRowLabelEditView.getValue());
                map.put("salesman", salesmanNameRowLabelEditView.getValue());
                //提交
                RequestParams params = new RequestParams(HttpConstants.getRegDeviceUrl(map));
                DHttpUtils.get_String(RegisterActivity.this, true, params, new DCommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        ResponseBean<Object> bean = new Gson().fromJson(result, new TypeToken<ResponseBean<CarLikeImeiBean>>() {
                        }.getType());
                        if (bean.getCode() == 1) {
                            showShortText("提交成功");
                            RegisterActivity.this.finish();
                        } else {
                            showShortText(bean.getErrmsg());
                        }
                    }
                });
            }
        });
    }

    @Override
    public void setData() {

    }
}

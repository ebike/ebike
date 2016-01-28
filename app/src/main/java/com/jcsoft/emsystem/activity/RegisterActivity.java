package com.jcsoft.emsystem.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.base.BaseActivity;
import com.jcsoft.emsystem.base.LocationJson;
import com.jcsoft.emsystem.bean.CarLikeImeiBean;
import com.jcsoft.emsystem.bean.ResponseBean;
import com.jcsoft.emsystem.callback.DCommonCallback;
import com.jcsoft.emsystem.callback.DSingleDialogCallback;
import com.jcsoft.emsystem.db.ProvinceInfoDao;
import com.jcsoft.emsystem.http.DHttpUtils;
import com.jcsoft.emsystem.http.HttpConstants;
import com.jcsoft.emsystem.utils.CommonUtils;
import com.jcsoft.emsystem.view.RowLabelEditView;
import com.jcsoft.emsystem.view.RowLabelValueView;
import com.jcsoft.emsystem.view.TopBarView;
import com.jcsoft.emsystem.view.wheel.AddressThreeWheelViewDialog;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注册账号
 */
public class RegisterActivity extends BaseActivity {
    @ViewInject(R.id.top_bar)
    TopBarView topBarView;
    @ViewInject(R.id.rlev_imei)
    RowLabelEditView imeiRowLabelEditView;
    @ViewInject(R.id.rlvv_equipment_serial_number)
    RowLabelValueView equipmentSerialNumberRowLabelValueView;
    @ViewInject(R.id.rlev_data_card)
    RowLabelEditView dataCardRowLabelEditView;
    @ViewInject(R.id.rlev_name)
    RowLabelEditView nameRowLabelEditView;
    @ViewInject(R.id.tb_sex)
    ToggleButton sexToggleButton;
    @ViewInject(R.id.rlev_phone)
    RowLabelEditView phoneRowLabelEditView;
    @ViewInject(R.id.rlev_id_card)
    RowLabelEditView idCardRowLabelEditView;
    @ViewInject(R.id.rlvv_area)
    RowLabelValueView areaRowLabelValueView;
    @ViewInject(R.id.rlev_address)
    RowLabelEditView addressRowLabelEditView;
    @ViewInject(R.id.rlev_dealer_number)
    RowLabelEditView dealerNumberRowLabelEditView;
    @ViewInject(R.id.rlev_salesman_name)
    RowLabelEditView salesmanNameRowLabelEditView;
    private AddressThreeWheelViewDialog dialog;
    private ProvinceInfoDao provinceDao;
    private List<LocationJson> mProvinceList;
    private int provinceId;
    private int cityId;
    private int districtId;
    private String provinceName;
    private String cityName;
    private String districtName;
    private String sex;

    @Override
    public void loadXml() {
        setContentView(R.layout.activity_register);
        x.view().inject(this);
    }

    @Override
    public void getIntentData(Bundle savedInstanceState) {
        provinceId = getIntent().getIntExtra("provinceId", -1);
        cityId = getIntent().getIntExtra("cityId", -1);
        districtId = getIntent().getIntExtra("districtId", -1);
    }

    @Override
    public void init() {
        sex = "0";
        imeiRowLabelEditView.setEditInteger();
        imeiRowLabelEditView.setEditLength(8);
        dataCardRowLabelEditView.setEditInteger();
        dataCardRowLabelEditView.setEditLength(13);
        phoneRowLabelEditView.setEditInteger();
        idCardRowLabelEditView.setEditInteger();
        idCardRowLabelEditView.setEditLength(18);
        dialog = new AddressThreeWheelViewDialog(this);
        provinceDao = new ProvinceInfoDao(this);
        mProvinceList = provinceDao.queryAll();
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
        sexToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {//选中
                    sex = "1";
                } else {//未选中
                    sex = "0";
                }
            }
        });
        //选择省市县
        areaRowLabelValueView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setData(mProvinceList);
                dialog.show(new AddressThreeWheelViewDialog.ConfirmAction() {
                    @Override
                    public void doAction(LocationJson root, LocationJson child, LocationJson child2) {
                        areaRowLabelValueView.setValue(root.getName() + " " + child.getName() + " " + child2.getName());
                        areaRowLabelValueView.setValueColor(R.color.gray_6);
                        provinceId = root.getId();
                        provinceName = root.getName();
                        cityId = child.getId();
                        cityName = child.getName();
                        districtId = child2.getId();
                        districtName = child2.getName();
                    }
                });
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
                    dataCardRowLabelEditView.setHint("必填");
                    dataCardRowLabelEditView.setHintColor(R.color.orange_dark);
                    return;
                }
                if (dataCardRowLabelEditView.getValue().length() < 4 ||
                        (dataCardRowLabelEditView.getValue().length() > 3 && !dataCardRowLabelEditView.getValue().substring(0, 4).equals("1064"))) {
                    dataCardRowLabelEditView.setValue("");
                    dataCardRowLabelEditView.setHint("请以1064开头");
                    dataCardRowLabelEditView.setHintColor(R.color.orange_dark);
                    return;
                }
                if (CommonUtils.strIsEmpty(nameRowLabelEditView.getValue())) {
                    nameRowLabelEditView.setHintColor(R.color.orange_dark);
                    return;
                }
                if (CommonUtils.strIsEmpty(idCardRowLabelEditView.getValue())) {
                    idCardRowLabelEditView.setHintColor(R.color.orange_dark);
                    return;
                }
                if (CommonUtils.strIsEmpty(phoneRowLabelEditView.getValue())) {
                    phoneRowLabelEditView.setHintColor(R.color.orange_dark);
                    return;
                }
                if (provinceId <= 0 && cityId <= 0 && districtId <= 0) {
                    areaRowLabelValueView.setValueColor(R.color.orange_dark);
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
                map.put("sex", sex);
                map.put("idNum", idCardRowLabelEditView.getValue());
                map.put("phone", phoneRowLabelEditView.getValue());
                map.put("province", provinceName);
                map.put("city", cityName);
                map.put("area", districtName);
                map.put("address", addressRowLabelEditView.getValue());
                map.put("dealerId", dealerNumberRowLabelEditView.getValue());
                map.put("salesman", salesmanNameRowLabelEditView.getValue());
                //提交
                RequestParams params = new RequestParams(HttpConstants.getRegDeviceUrl(map));
                params.setCharset("UTF-8");
                DHttpUtils.post_String(RegisterActivity.this, true, params, new DCommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        ResponseBean<Object> bean = new Gson().fromJson(result, new TypeToken<ResponseBean<CarLikeImeiBean>>() {
                        }.getType());
                        if (bean.getCode() == 1) {
                            CommonUtils.showCustomDialogSignle2(RegisterActivity.this, "恭喜您，注册成功", "\n您的账号为：" + equipmentSerialNumberRowLabelValueView.getValue() + "\n初始密码为：666666", new DSingleDialogCallback() {
                                @Override
                                public void onPositiveButtonClick(String editText) {
                                    RegisterActivity.this.finish();
                                }
                            });
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

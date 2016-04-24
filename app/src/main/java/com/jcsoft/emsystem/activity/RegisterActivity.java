package com.jcsoft.emsystem.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.jcsoft.emsystem.http.DRequestParamsUtils;
import com.jcsoft.emsystem.http.HttpConstants;
import com.jcsoft.emsystem.utils.CommonUtils;
import com.jcsoft.emsystem.utils.KeyboardUtils;
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
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
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

    @ViewInject(R.id.rlvv_remote_control)
    RowLabelValueView remoteControlView;

    LinearLayout remoteLockLayout;
    ImageView remoteLockView;
    LinearLayout voiceSeachLayout;
    ImageView voiceSeachView;
    LinearLayout oneKeyStartLayout;
    ImageView oneKeyStartView;

    @ViewInject(R.id.rlvv_voltage)
    RowLabelValueView voltageView;

    LinearLayout v48Layout;
    ImageView v48View;
    LinearLayout v60Layout;
    ImageView v60View;
    LinearLayout v72Layout;
    ImageView v72View;
    EditText customVoltageText;

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
    private View remoteControlDialogView;
    private Integer remoteControlTempValue;
    private Integer remoteControlValue;
    private View voltageDialogView;
    private Integer voltageTempValue;
    private Integer voltageValue;

    @Override
    public void loadXml() {
        setContentView(R.layout.activity_register);
        x.view().inject(this);

//        voltageDialogView = LayoutInflater.from(this).inflate(R.layout.view_voltage, null);
    }

    @Override
    public void getIntentData(Bundle savedInstanceState) {

    }

    @Override
    public void init() {
        sex = "0";
        imeiRowLabelEditView.setEditInteger();
        imeiRowLabelEditView.setEditLength(8);
        dataCardRowLabelEditView.setEditInteger();
        dataCardRowLabelEditView.setEditLength(13);
        phoneRowLabelEditView.setEditInteger();
        idCardRowLabelEditView.setEditFormat("0123456789xX");
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
        //选择远程控制
        remoteControlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRemoteControlDialogView(remoteControlValue);
                CommonUtils.showCustomDialog1(RegisterActivity.this, "选择远程控制功能", remoteControlDialogView, new DSingleDialogCallback() {
                    @Override
                    public void onPositiveButtonClick(String editText) {
                        if (remoteControlTempValue != null) {
                            remoteControlValue = remoteControlTempValue;
                            if (remoteControlValue == 1) {
                                remoteControlView.setValue("远程锁车");
                            } else if (remoteControlValue == 2) {
                                remoteControlView.setValue("语音寻车");
                            } else if (remoteControlValue == 3) {
                                remoteControlView.setValue("一键启动");
                            }
                        }
                    }
                });
            }
        });
        //选择电动车电压
        voltageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initVoltageDialogView(voltageValue);
                CommonUtils.showCustomDialog2(RegisterActivity.this, "选择电动车电压", voltageDialogView, new DSingleDialogCallback() {
                    @Override
                    public void onPositiveButtonClick(String editText) {
                        KeyboardUtils.hideSoftInput(RegisterActivity.this);
                    }
                }, new DSingleDialogCallback() {
                    @Override
                    public void onPositiveButtonClick(String editText) {
                        KeyboardUtils.hideSoftInput(RegisterActivity.this);
                        if (voltageTempValue != null) {
                            voltageValue = voltageTempValue;
                            voltageView.setValue(voltageValue + "V");
                        }
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
                    dataCardRowLabelEditView.setHint(R.string.app_require_input);
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
                    nameRowLabelEditView.setHint(R.string.app_require_input);
                    nameRowLabelEditView.setHintColor(R.color.orange_dark);
                    return;
                }
                if (CommonUtils.strIsEmpty(idCardRowLabelEditView.getValue())) {
                    idCardRowLabelEditView.setHint(R.string.app_require_input);
                    idCardRowLabelEditView.setHintColor(R.color.orange_dark);
                    return;
                }
                if (CommonUtils.strIsEmpty(phoneRowLabelEditView.getValue())) {
                    phoneRowLabelEditView.setHint(R.string.app_require_input);
                    phoneRowLabelEditView.setHintColor(R.color.orange_dark);
                    return;
                }
                if (provinceId <= 0 && cityId <= 0 && districtId <= 0) {
                    areaRowLabelValueView.setValueColor(R.color.orange_dark);
                    return;
                }
                if (CommonUtils.strIsEmpty(addressRowLabelEditView.getValue())) {
                    addressRowLabelEditView.setHint(R.string.app_require_input);
                    addressRowLabelEditView.setHintColor(R.color.orange_dark);
                    return;
                }
                if (remoteControlValue == null) {
                    remoteControlView.setValueColor(R.color.orange_dark);
                }
                if (voltageValue == null) {
                    voltageView.setValueColor(R.color.orange_dark);
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
                map.put("controlType", remoteControlValue + "");
                map.put("voltage", voltageValue + "");
                //提交
                RequestParams params = DRequestParamsUtils.getRequestParams(HttpConstants.getRegDeviceUrl(), map);
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

    private View initRemoteControlDialogView(Integer remoteControlValue) {
        remoteControlDialogView = LayoutInflater.from(this).inflate(R.layout.view_remote_control, null);
        remoteLockLayout = (LinearLayout) remoteControlDialogView.findViewById(R.id.ll_remote_lock);
        voiceSeachLayout = (LinearLayout) remoteControlDialogView.findViewById(R.id.ll_voice_seach);
        oneKeyStartLayout = (LinearLayout) remoteControlDialogView.findViewById(R.id.ll_one_key_start);
        remoteLockView = (ImageView) remoteControlDialogView.findViewById(R.id.iv_remote_lock);
        voiceSeachView = (ImageView) remoteControlDialogView.findViewById(R.id.iv_voice_seach);
        oneKeyStartView = (ImageView) remoteControlDialogView.findViewById(R.id.iv_one_key_start);

        if (remoteControlValue != null) {
            if (remoteControlValue == 1) {
                remoteLockView.setVisibility(View.VISIBLE);
                voiceSeachView.setVisibility(View.GONE);
                oneKeyStartView.setVisibility(View.GONE);
            } else if (remoteControlValue == 2) {
                remoteLockView.setVisibility(View.GONE);
                voiceSeachView.setVisibility(View.VISIBLE);
                oneKeyStartView.setVisibility(View.GONE);
            } else if (remoteControlValue == 3) {
                remoteLockView.setVisibility(View.GONE);
                voiceSeachView.setVisibility(View.GONE);
                oneKeyStartView.setVisibility(View.VISIBLE);
            }
        }

        remoteLockLayout.setOnClickListener(this);
        voiceSeachLayout.setOnClickListener(this);
        oneKeyStartLayout.setOnClickListener(this);

        return remoteControlDialogView;
    }

    private View initVoltageDialogView(Integer voltageValue) {
        voltageDialogView = LayoutInflater.from(this).inflate(R.layout.view_voltage, null);
        v48Layout = (LinearLayout) voltageDialogView.findViewById(R.id.ll_48v);
        v60Layout = (LinearLayout) voltageDialogView.findViewById(R.id.ll_60v);
        v72Layout = (LinearLayout) voltageDialogView.findViewById(R.id.ll_72v);
        v48View = (ImageView) voltageDialogView.findViewById(R.id.iv_48v);
        v60View = (ImageView) voltageDialogView.findViewById(R.id.iv_60v);
        v72View = (ImageView) voltageDialogView.findViewById(R.id.iv_72v);
        customVoltageText = (EditText) voltageDialogView.findViewById(R.id.et_custom_voltage);

        if (voltageValue != null) {
            if (voltageValue == 48) {
                v48View.setVisibility(View.VISIBLE);
                v60View.setVisibility(View.GONE);
                v72View.setVisibility(View.GONE);
                customVoltageText.setText("");
            } else if (voltageValue == 60) {
                v48View.setVisibility(View.GONE);
                v60View.setVisibility(View.VISIBLE);
                v72View.setVisibility(View.GONE);
                customVoltageText.setText("");
            } else if (voltageValue == 72) {
                v48View.setVisibility(View.GONE);
                v60View.setVisibility(View.GONE);
                v72View.setVisibility(View.VISIBLE);
                customVoltageText.setText("");
            } else {
                customVoltageText.setText(voltageValue + "");
            }
        }

        v48Layout.setOnClickListener(this);
        v60Layout.setOnClickListener(this);
        v72Layout.setOnClickListener(this);
        customVoltageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String value = customVoltageText.getText().toString().trim();
                if (!CommonUtils.strIsEmpty(value)) {
                    voltageTempValue = Integer.valueOf(value);
                    v48View.setVisibility(View.GONE);
                    v60View.setVisibility(View.GONE);
                    v72View.setVisibility(View.GONE);
                }
            }
        });

        return voltageDialogView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_remote_lock:
                remoteControlTempValue = 1;
                remoteLockView.setVisibility(View.VISIBLE);
                voiceSeachView.setVisibility(View.GONE);
                oneKeyStartView.setVisibility(View.GONE);
                break;
            case R.id.ll_voice_seach:
                remoteControlTempValue = 2;
                remoteLockView.setVisibility(View.GONE);
                voiceSeachView.setVisibility(View.VISIBLE);
                oneKeyStartView.setVisibility(View.GONE);
                break;
            case R.id.ll_one_key_start:
                remoteControlTempValue = 3;
                remoteLockView.setVisibility(View.GONE);
                voiceSeachView.setVisibility(View.GONE);
                oneKeyStartView.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_48v:
                voltageTempValue = 48;
                v48View.setVisibility(View.VISIBLE);
                v60View.setVisibility(View.GONE);
                v72View.setVisibility(View.GONE);
                customVoltageText.setText("");
                CommonUtils.hideSoftInput(this, customVoltageText);
                break;
            case R.id.ll_60v:
                voltageTempValue = 60;
                v48View.setVisibility(View.GONE);
                v60View.setVisibility(View.VISIBLE);
                v72View.setVisibility(View.GONE);
                customVoltageText.setText("");
                CommonUtils.hideSoftInput(this, customVoltageText);
                break;
            case R.id.ll_72v:
                voltageTempValue = 72;
                v48View.setVisibility(View.GONE);
                v60View.setVisibility(View.GONE);
                v72View.setVisibility(View.VISIBLE);
                customVoltageText.setText("");
                CommonUtils.hideSoftInput(this, customVoltageText);
                break;
        }
    }
}

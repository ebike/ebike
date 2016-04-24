package com.jcsoft.emsystem.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.base.BaseActivity;
import com.jcsoft.emsystem.bean.CarInfoBean;
import com.jcsoft.emsystem.bean.ResponseBean;
import com.jcsoft.emsystem.bean.UserInfoBean;
import com.jcsoft.emsystem.callback.DCommonCallback;
import com.jcsoft.emsystem.constants.AppConfig;
import com.jcsoft.emsystem.http.DHttpUtils;
import com.jcsoft.emsystem.http.DRequestParamsUtils;
import com.jcsoft.emsystem.http.HttpConstants;
import com.jcsoft.emsystem.view.TopBarView;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class UpdateTextValueActivity extends BaseActivity {
    @ViewInject(R.id.top_bar_view)
    TopBarView topBarView;
    @ViewInject(R.id.et_field)
    EditText fieldEditText;
    private int type;
    private String fieldName;
    private String fieldValue;
    private String fieldName_CH;
    private Integer inputType;
    private Integer length;
    private boolean isIdcard;

    @Override
    public void loadXml() {
        setContentView(R.layout.activity_update_text_value);
        x.view().inject(this);
    }

    @Override
    public void getIntentData(Bundle savedInstanceState) {
        type = getIntent().getIntExtra("type", -1);
        fieldName_CH = getIntent().getStringExtra("fieldName_CH");
        fieldValue = getIntent().getStringExtra("fieldValue");
        fieldName = getIntent().getStringExtra("fieldName");
        inputType = getIntent().getIntExtra("inputType", -1);
        length = getIntent().getIntExtra("length", -1);
        isIdcard = getIntent().getBooleanExtra("isIdcard", false);
    }

    @Override
    public void init() {
        topBarView.setCenterTextView(fieldName_CH);
        fieldEditText.setText(fieldValue);
        if (inputType != null && inputType != -1) {
            fieldEditText.setInputType(inputType);
        }
        if (length != null && length != -1) {
            fieldEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
        }
        if(isIdcard){
            String digits = "0123456789xX";
            fieldEditText.setKeyListener(DigitsKeyListener.getInstance(digits));
        }
        fieldEditText.requestFocus();
    }

    @Override
    public void setListener() {
        topBarView.setRightCallback(new TopBarView.TopBarRightCallback() {
            @Override
            public void setRightOnClickListener() {
                String newValue = fieldEditText.getText().toString();
                //老值和新值不同是进行保存
                if (!fieldValue.equals(newValue)) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("carId", AppConfig.userInfoBean.getCarId() + "");
                    map.put(fieldName, newValue);
                    String url = "";
                    if (type == 1) {
                        url = HttpConstants.getUpdateUserUrl();
                    } else if (type == 2) {
                        url = HttpConstants.getUpdateCarUrl();
                    }
                    RequestParams params = DRequestParamsUtils.getRequestParams_Header(url, map);
                    DHttpUtils.post_String(UpdateTextValueActivity.this, true, params, new DCommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            if (type == 1) {
                                ResponseBean<UserInfoBean> bean = new Gson().fromJson(result, new TypeToken<ResponseBean<UserInfoBean>>() {
                                }.getType());
                                if (bean.getCode() == 1) {
                                    AppConfig.userInfoBean = bean.getData();
                                    EventBus.getDefault().post(bean.getData());
                                } else {
                                    showShortText(bean.getErrmsg());
                                }
                            } else if (type == 2) {
                                ResponseBean<CarInfoBean> bean = new Gson().fromJson(result, new TypeToken<ResponseBean<CarInfoBean>>() {
                                }.getType());
                                if (bean.getCode() == 1) {
                                    EventBus.getDefault().post(bean.getData());
                                } else {
                                    showShortText(bean.getErrmsg());
                                }
                            }

                        }
                    });
                }
                UpdateTextValueActivity.this.finish();
            }
        });
    }

    @Override
    public void setData() {

    }
}

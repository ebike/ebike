package com.jcsoft.emsystem.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.base.BaseActivity;
import com.jcsoft.emsystem.bean.ResponseBean;
import com.jcsoft.emsystem.callback.DCommonCallback;
import com.jcsoft.emsystem.constants.AppConfig;
import com.jcsoft.emsystem.event.RemoteLockCarEvent;
import com.jcsoft.emsystem.http.DHttpUtils;
import com.jcsoft.emsystem.http.DRequestParamsUtils;
import com.jcsoft.emsystem.http.HttpConstants;
import com.jcsoft.emsystem.utils.CommonUtils;
import com.jcsoft.emsystem.view.TopBarView;
import com.jcsoft.emsystem.view.XiuView;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class RemoteControlActivity extends BaseActivity {

    @ViewInject(R.id.top_bar)
    TopBarView topBarView;
    @ViewInject(R.id.xv_execute)
    XiuView executeView;
    @ViewInject(R.id.tv_message)
    TextView messageView;

    private String controlType;
    private String controlTypeName;
    private String isLock;
    private boolean canClick = true;

    @Override
    public void loadXml() {
        setContentView(R.layout.activity_remote_control);
        x.view().inject(this);
    }

    @Override
    public void getIntentData(Bundle savedInstanceState) {
        controlType = getIntent().getStringExtra("controlType");
        isLock = getIntent().getStringExtra("isLock");
    }

    @Override
    public void init() {
        if (controlType != null && controlType.equals("2")) {
            controlTypeName = "语音寻车";
            if (isLock.equals("1")) {
                executeView.setCircleResource(R.mipmap.bg_control_close);
                executeView.setBtnNormalResource(R.mipmap.icon_sound_close);
            } else {
                executeView.setCircleResource(R.mipmap.bg_control_open);
                executeView.setBtnNormalResource(R.mipmap.icon_sound_open);
            }
        } else if (controlType != null && controlType.equals("3")) {
            controlTypeName = "一键启动";
            if (isLock.equals("1")) {
                executeView.setCircleResource(R.mipmap.bg_control_close);
                executeView.setBtnNormalResource(R.mipmap.icon_onekey_end);
            } else {
                executeView.setCircleResource(R.mipmap.bg_control_open);
                executeView.setBtnNormalResource(R.mipmap.icon_onekey_start);
            }
        } else {
            controlTypeName = "远程锁车";
            if (isLock.equals("1")) {
                executeView.setCircleResource(R.mipmap.bg_control_close);
                executeView.setBtnNormalResource(R.mipmap.icon_lock_close);
            } else {
                executeView.setCircleResource(R.mipmap.bg_control_open);
                executeView.setBtnNormalResource(R.mipmap.icon_lock_open);
            }
        }

        topBarView.setCenterTextView(controlTypeName);
        messageView.setText("此功能只有设备端远程控制线为" + controlTypeName + "才支持");

        if (AppConfig.isExecuteLock != null) {
            executeView.setIsCycle(true);
            executeView.setEnabled(false);
            executeView.openMoveCircle();
        }

    }

    @Override
    public void setListener() {
        executeView.setOnBtnPressListener(new XiuView.OnBtnPressListener() {
            @Override
            public void btnClick() {
                executeView.setIsCycle(true);
                executeView.setEnabled(false);
                if (isLock.equals("1")) {
                    RequestParams params = null;
                    if (controlType != null && controlType.equals("2")) {
                        params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.getUnLockBikeUrl("1"));
                    } else if (controlType != null && controlType.equals("3")) {
                        params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.getUnLockBikeUrl("2"));
                    } else {
                        params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.getUnLockBikeUrl("0"));
                    }
                    DHttpUtils.get_String(RemoteControlActivity.this, false, params, new DCommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            ResponseBean<String> responseBean = new Gson().fromJson(result, new TypeToken<ResponseBean<String>>() {
                            }.getType());
                            if (responseBean != null) {
                                AppConfig.isExecuteLock = 0;
                                showShortText(responseBean.getErrmsg());
                            }
                        }
                    });
                } else {
                    RequestParams params = null;
                    if (controlType != null && controlType.equals("2")) {
                        params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.getlockBikeUrl("1"));
                    } else if (controlType != null && controlType.equals("3")) {
                        params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.getlockBikeUrl("2"));
                    } else {
                        params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.getlockBikeUrl("0"));
                    }
                    DHttpUtils.get_String(RemoteControlActivity.this, false, params, new DCommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            ResponseBean<String> responseBean = new Gson().fromJson(result, new TypeToken<ResponseBean<String>>() {
                            }.getType());
                            if (responseBean != null) {
                                AppConfig.isExecuteLock = 1;
                                showShortText(responseBean.getErrmsg());
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void setData() {

    }

    //处理远程锁车推送
    public void onEvent(RemoteLockCarEvent event) {
        if (event != null) {
            AppConfig.isExecuteLock = null;
            executeView.setIsCycle(false);
            executeView.clearMoveCircle();
            executeView.setEnabled(true);
            if (event.getIsLock().equals("1")) {
                if (!CommonUtils.strIsEmpty(controlType)) {
                    executeView.setCircleResource(R.mipmap.bg_control_close);
                    if (controlType != null && controlType.equals("2")) {
                        executeView.setBtnNormalResource(R.mipmap.icon_sound_close);
                    } else if (controlType != null && controlType.equals("3")) {
                        executeView.setBtnNormalResource(R.mipmap.icon_onekey_end);
                    } else {
                        executeView.setBtnNormalResource(R.mipmap.icon_lock_close);
                    }
                }
                AppConfig.isLock = true;
            } else {
                if (!CommonUtils.strIsEmpty(controlType)) {
                    executeView.setCircleResource(R.mipmap.bg_control_open);
                    if (controlType != null && controlType.equals("2")) {
                        executeView.setBtnNormalResource(R.mipmap.icon_sound_open);
                    } else if (controlType != null && controlType.equals("3")) {
                        executeView.setBtnNormalResource(R.mipmap.icon_onekey_start);
                    } else {
                        executeView.setBtnNormalResource(R.mipmap.icon_lock_open);
                    }
                }
                AppConfig.isLock = false;
            }
        }
    }
}

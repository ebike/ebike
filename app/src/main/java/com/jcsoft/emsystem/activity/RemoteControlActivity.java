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
        if (!CommonUtils.strIsEmpty(controlType)) {
            if (controlType.equals("1")) {
                controlTypeName = "远程锁车";
                if (isLock.equals("1")) {
                    executeView.setBtnNormalResource(R.mipmap.icon_lock_open);
                } else {
                    executeView.setBtnNormalResource(R.mipmap.icon_lock_close);
                }
            } else if (controlType.equals("2")) {
                controlTypeName = "语音寻车";
                if (isLock.equals("1")) {
                    executeView.setBtnNormalResource(R.mipmap.icon_sound_open);
                } else {
                    executeView.setBtnNormalResource(R.mipmap.icon_sound_close);
                }
            } else if (controlType.equals("3")) {
                controlTypeName = "一键启动";
                if (isLock.equals("1")) {
                    executeView.setBtnNormalResource(R.mipmap.icon_onekey_start);
                } else {
                    executeView.setBtnNormalResource(R.mipmap.icon_onekey_end);
                }
            }
        }

        topBarView.setCenterTextView(controlTypeName);

        if (AppConfig.isExecuteLock != null) {
            executeView.setEnabled(false);
            executeView.openMoveCircle();
        }

    }

    @Override
    public void setListener() {
        executeView.setOnBtnPressListener(new XiuView.OnBtnPressListener() {
            @Override
            public void btnClick() {
                executeView.setEnabled(false);
                if (isLock.equals("1")) {
                    RequestParams params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.getUnLockBikeUrl());
                    DHttpUtils.get_String(RemoteControlActivity.this, false, params, new DCommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            ResponseBean<String> responseBean = new Gson().fromJson(result, new TypeToken<ResponseBean<String>>() {
                            }.getType());
                            if (responseBean != null) {
                                AppConfig.isExecuteLock = 0;
                                AppConfig.lockCarType = 1;
                                showShortText("关闭命令发送成功");
                            }
                        }
                    });
                } else {
                    RequestParams params = DRequestParamsUtils.getRequestParams_Header(HttpConstants.getlockBikeUrl());
                    DHttpUtils.get_String(RemoteControlActivity.this, false, params, new DCommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            ResponseBean<String> responseBean = new Gson().fromJson(result, new TypeToken<ResponseBean<String>>() {
                            }.getType());
                            if (responseBean != null) {
                                AppConfig.isExecuteLock = 1;
                                AppConfig.lockCarType = 1;
                                showShortText("开启命令发送成功");
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
            executeView.closeMoveCircle();
            executeView.setEnabled(true);
            if (event.getIsLock().equals("1")) {
                if (!CommonUtils.strIsEmpty(controlType)) {
                    if (controlType.equals("1")) {
                        executeView.setBtnNormalResource(R.mipmap.icon_lock_open);
                    } else if (controlType.equals("2")) {
                        executeView.setBtnNormalResource(R.mipmap.icon_sound_open);
                    } else if (controlType.equals("3")) {
                        executeView.setBtnNormalResource(R.mipmap.icon_onekey_start);
                    }
                }
                AppConfig.isLock = true;
            } else {
                if (!CommonUtils.strIsEmpty(controlType)) {
                    if (controlType.equals("1")) {
                        executeView.setBtnNormalResource(R.mipmap.icon_lock_close);
                    } else if (controlType.equals("2")) {
                        executeView.setBtnNormalResource(R.mipmap.icon_sound_close);
                    } else if (controlType.equals("3")) {
                        executeView.setBtnNormalResource(R.mipmap.icon_onekey_end);
                    }
                }
                AppConfig.isLock = false;
            }
        }
    }
}

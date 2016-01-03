package com.jcsoft.emsystem.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.base.BaseActivity;
import com.jcsoft.emsystem.bean.ResponseBean;
import com.jcsoft.emsystem.bean.UserInfoBean;
import com.jcsoft.emsystem.callback.DCommonCallback;
import com.jcsoft.emsystem.constants.AppConfig;
import com.jcsoft.emsystem.http.DHttpUtils;
import com.jcsoft.emsystem.http.HttpConstants;
import com.jcsoft.emsystem.utils.CommonUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    @ViewInject(R.id.rl_rootView)
    RelativeLayout rootViewRelativeLayout;
    @ViewInject(R.id.et_loginName)
    EditText loginNameEditText;
    @ViewInject(R.id.et_password)
    EditText passwordEditText;
    @ViewInject(R.id.btn_login)
    Button loginButton;
    private String loginName;
    private String password;

    @Override
    public void loadXml() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_login);
        x.view().inject(this);
        controlKeyboardLayout(rootViewRelativeLayout, loginButton);
    }

    @Override
    public void getIntentData(Bundle savedInstanceState) {

    }

    public void init() {
        if (CommonUtils.strIsEmpty(AppConfig.loginName)) {
            AppConfig.loginName = preferencesUtil.getPrefString(LoginActivity.this, AppConfig.LOGIN_NAME, "");
        }
        loginNameEditText.setText(AppConfig.loginName);
        loginNameEditText.requestFocus();
    }

    @Override
    public void setListener() {
        //监控输入变化
        loginNameEditText.addTextChangedListener(this);
        passwordEditText.addTextChangedListener(this);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void setData() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                loginName = loginNameEditText.getText().toString().trim();
                password = passwordEditText.getText().toString().trim();
                if (CommonUtils.strIsEmpty(loginName)) {
                    showShortText("请输入手机号");
                    return;
                } else if (CommonUtils.strIsEmpty(password)) {
                    showShortText("请输入密码");
                    return;
                }
                RequestParams params = new RequestParams(HttpConstants.getLoginUrl(loginName, CommonUtils.MD5(password)));
                DHttpUtils.get_String(LoginActivity.this, true, params, new DCommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        ResponseBean<UserInfoBean> responseBean = new Gson().fromJson(result, new TypeToken<ResponseBean<UserInfoBean>>() {
                        }.getType());
                        if (responseBean.getCode() == 1) {
                            AppConfig.userInfoBean = responseBean.getData();
                            preferencesUtil.setPrefString(LoginActivity.this, AppConfig.LOGIN_NAME, loginName);
                            preferencesUtil.setPrefString(LoginActivity.this, AppConfig.PASSWORD, CommonUtils.MD5(password));
                            preferencesUtil.setPrefString(LoginActivity.this, AppConfig.REGISTRATION_ID, AppConfig.registrationId);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            LoginActivity.this.finish();
                        } else {
                            showShortText(responseBean.getErrmsg());
                        }
                    }
                });
                break;
        }
    }

    /**
     * 处理软键盘遮挡登陆按钮
     *
     * @param root         最外层布局，需要调整的布局
     * @param scrollToView 被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
     */
    private void controlKeyboardLayout(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                //若不可视区域高度大于100，则键盘显示
                if (rootInvisibleHeight > 100) {
                    int[] location = new int[2];
                    //获取scrollToView在窗体的坐标
                    scrollToView.getLocationInWindow(location);
                    //计算root滚动高度，使scrollToView在可见区域
                    int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
                    root.scrollTo(0, srollHeight);
                } else {
                    //键盘隐藏
                    root.scrollTo(0, 0);
                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        //判断手机和密码是否填写
        loginName = loginNameEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();
        if (!CommonUtils.strIsEmpty(loginName) && !CommonUtils.strIsEmpty(password)) {
            //设置登录按钮可点击
            loginButton.setEnabled(true);
            //改变按钮样式
            loginButton.setAlpha(1f);
            loginButton.setTextColor(getResources().getColor(R.color.white));
        } else {
            //设置验证按钮不可点击
            loginButton.setEnabled(false);
            //改变按钮样式
            loginButton.setAlpha(0.4f);
            loginButton.setTextColor(getResources().getColor(R.color.login_text_disabled));
        }
    }

}

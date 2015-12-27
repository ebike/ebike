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

import com.jcsoft.emsystem.R;
import com.jcsoft.emsystem.base.BaseActivity;
import com.jcsoft.emsystem.constants.AppConfig;
import com.jcsoft.emsystem.utils.CommonUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    @ViewInject(R.id.relayout_rootView)
    RelativeLayout linearLayoutRootView;
    @ViewInject(R.id.button_login)
    Button button_login;
    @ViewInject(R.id.edit_account)
    EditText edtAccount;
    @ViewInject(R.id.edit_password)
    EditText edtPassword;
    private String phone;
    private String password;
    // 只有用户点了登录，才允许在建立连接成功后自动登录
    private boolean _isAllowLogin = false;
    private String _serverAddr = "android.gnets.cn";
    private int _port = 8111;


    @Override
    public void loadXml() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_login);
        x.view().inject(this);
        controlKeyboardLayout(linearLayoutRootView, button_login);
    }

    @Override
    public void getIntentData(Bundle savedInstanceState) {

    }

    public void init() {
        if (CommonUtils.strIsEmpty(AppConfig.userAccount)) {
            AppConfig.userAccount = preferencesUtil.getPrefString(LoginActivity.this, AppConfig.USER_ACCOUNT, "");
        }
        edtAccount.setText(AppConfig.userAccount);
        edtAccount.requestFocus();
        //设置验证按钮不可点击
        button_login.setEnabled(false);
        //改变按钮样式
        button_login.setAlpha(0.4f);
        button_login.setTextColor(getResources().getColor(R.color.login_text_disabled));
    }

    @Override
    public void setListener() {
        //监控输入变化
        edtAccount.addTextChangedListener(this);
        edtPassword.addTextChangedListener(this);
        button_login.setOnClickListener(this);
//        button_findKey.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, FindKeyActivity.class);
//                intent.putExtra("Account", edtAccount.getText().toString());
//                startActivity(intent);
//            }
//        });
        //检测帐号变化
        edtAccount.addTextChangedListener(new TextWatcher() {
            private int oldLength;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                oldLength = charSequence.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (oldLength - editable.length() > 0) {
                    edtPassword.setText("");
                }
            }
        });
    }

    @Override
    public void setData() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                // 只要点击登录按钮，就设置_isFirstClickLogin为true
//                _isFirstClickLogin = true;
                phone = edtAccount.getText().toString().trim();
                password = edtPassword.getText().toString().trim();
                if (CommonUtils.strIsEmpty(phone)) {
                    showShortText("请输入手机号");
                    return;
                } else if (CommonUtils.strIsEmpty(password)) {
                    showShortText("请输入密码");
                    return;
                }
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                String storagePassword = ConfigService.instance()
//                        .getConfigValue(JCConstValues.S_Password);
//                password = CommonUtils.MD5(password); // 进行加密
//                if (!password.equalsIgnoreCase(storagePassword)) {
//                    // 密码被修改了，认为之前没有登录成功过
////                    _isLoginSuccessBefore = false;
//                }
//                // 暂时不删除，正式时删除
//                ConfigService.instance().deleteConfigValue(
//                        JCConstValues.S_Password); // 删除之前保存的密码
//                _isAllowLogin = true;
//                initNetwork();
//                JCClient.instance().login(phone, password);
//                button_login.setEnabled(false);
//                startLoadingProgress();
                break;
        }
    }

//    private void initNetwork() {
//        _serverAddr = ConfigService.instance().getConfigValue(
//                JCConstValues.S_ServerAddr);
//        String portStr = ConfigService.instance().getConfigValue(
//                JCConstValues.S_Port);
//        if (_serverAddr == null || _serverAddr.length() == 0 || portStr == null
//                || portStr.length() == 0) {
//            _serverAddr = "android.gnets.cn";
//            portStr = "8111";
//            ConfigService.instance().updateOrInsert(JCConstValues.S_ServerAddr,
//                    _serverAddr);
//            ConfigService.instance().updateOrInsert(JCConstValues.S_Port,
//                    portStr);
//        }
//        _port = Integer.valueOf(portStr);
//        JCClient.instance().init(this, _serverAddr, _port);
//        JCClient.instance().setListener(new JCClientListener(this));
//    }

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
        phone = edtAccount.getText().toString().trim();
        password = edtPassword.getText().toString().trim();
        if (!CommonUtils.strIsEmpty(phone) && !CommonUtils.strIsEmpty(password)) {
            //设置登录按钮可点击
            button_login.setEnabled(true);
            //改变按钮样式
            button_login.setAlpha(1f);
            button_login.setTextColor(getResources().getColor(R.color.white));
        } else {
            //设置验证按钮不可点击
            button_login.setEnabled(false);
            //改变按钮样式
            button_login.setAlpha(0.4f);
            button_login.setTextColor(getResources().getColor(R.color.login_text_disabled));
        }
    }

}

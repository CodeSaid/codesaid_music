package com.codesaid_music.view.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import androidx.annotation.Nullable;

import com.codesaid.lib_commin_ui.base.BaseActivity;
import com.codesaid_music.R;
import com.codesaid_music.view.login.inter.IUserLoginView;
import com.codesaid_music.view.login.presenter.UserLoginPresenter;
import com.google.android.material.textfield.TextInputEditText;

/**
 * Created By codesaid
 * On :2019-11-03
 * Package Name: com.codesaid_music.view.login
 * desc: 登录页面
 */
public class LoginActivity extends BaseActivity implements IUserLoginView {

    private UserLoginPresenter mUserLoginPresenter;

    private TextInputEditText mEmail = null;
    private TextInputEditText mPassword = null;

    private String email;
    private String password;


    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        mEmail = findViewById(R.id.edit_sign_in_email);
        mEmail = findViewById(R.id.edit_sign_in_password);

        //初始化P层
        mUserLoginPresenter = new UserLoginPresenter(this);

        findViewById(R.id.login_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForm()) {
                    mUserLoginPresenter.login(email, password);
                }
            }
        });
    }

    private boolean checkForm() {
        email = mEmail.getText().toString().trim();
        password = mPassword.getText().toString().trim();

        // 表示是否通过
        boolean isPass = true;

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("错误的邮箱格式");
            isPass = false;
        } else {
            mEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            mPassword.setError("密码不能为空或者小于6位数");
            isPass = false;
        } else {
            mPassword.setError(null);
        }

        return isPass;
    }

    @Override
    public String getUserName() {
        return "17871211558";
    }

    @Override
    public String getPassword() {
        return "123456";
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void showLoginFailedView() {
        //登陆失败处理
    }

    @Override
    public void showLoadingView() {
        //显示加载中UI
    }

    @Override
    public void hideLoadingView() {
        //隐藏加载布局
    }
}

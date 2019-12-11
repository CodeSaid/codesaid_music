package com.codesaid_music.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.codesaid.lib_commin_ui.base.BaseActivity;
import com.codesaid.lib_network.okhttp.listener.DisposeDataListener;
import com.codesaid_music.R;
import com.codesaid_music.api.RequestCenter;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.FormBody;

/**
 * Created By codesaid
 * On :2019-12-10
 * Package Name: com.codesaid_music.view.login
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private TextInputEditText mName = null;
    private TextInputEditText mEmail = null;
    private TextInputEditText mPhone = null;
    private TextInputEditText mPassword = null;
    private TextInputEditText mPasswordTwo = null;
    private AppCompatButton mBtnRegister = null;
    private AppCompatTextView mTvLogin;

    private String name;
    private String email;
    private String phone;
    private String password;
    private String passwordTwo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_layout);
        mName = findViewById(R.id.edit_sign_up_name);
        mEmail = findViewById(R.id.edit_sign_up_email);
        mPhone = findViewById(R.id.edit_sign_up_phone);
        mPassword = findViewById(R.id.edit_sign_up_password);
        mPasswordTwo = findViewById(R.id.edit_sign_up_password_two);
        mBtnRegister = findViewById(R.id.btn_sign_up);
        mTvLogin = findViewById(R.id.tv_link_sign_in);

        mBtnRegister.setOnClickListener(this);
        mTvLogin.setOnClickListener(this);
    }

    /**
     * 检查用户输入内容的 格式
     *
     * @return true:正确
     */
    private boolean checkForm() {
        name = mName.getText().toString().trim();
        email = mEmail.getText().toString().trim();
        phone = mPhone.getText().toString().trim();
        password = mPassword.getText().toString().trim();
        passwordTwo = mPasswordTwo.getText().toString().trim();

        // 表示是否通过
        boolean isPass = true;

        if (name.isEmpty()) {
            mName.setError("请输入姓名");
            isPass = false;
        } else {
            mName.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("错误的邮箱格式");
            isPass = false;
        } else {
            mEmail.setError(null);
        }

        if (phone.isEmpty() || phone.length() != 11) {
            mPhone.setError("错误的手机号码");
            isPass = false;
        } else {
            mPhone.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            mPassword.setError("密码不能为空或者小于6位数");
            isPass = false;
        } else {
            mPassword.setError(null);
        }

        if (passwordTwo.isEmpty() || passwordTwo.length() < 6 || !(passwordTwo.equals(password))) {
            mPasswordTwo.setError("两次输入的密码必须一致");
            isPass = false;
        } else {
            mPasswordTwo.setError(null);
        }

        return isPass;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_up:
                if (checkForm()) {
                    // 向服务器提交注册信息
                    FormBody formBody = new FormBody.Builder()
                            .add("name", name)
                            .add("phone", phone)
                            .add("email", email)
                            .add("password", password)
                            .build();
                    RequestCenter.postUserRegister(formBody, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {

                        }

                        @Override
                        public void onFailure(Object reasonObj) {

                        }
                    });
                }
                break;
            case R.id.tv_link_sign_in:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(intent);
                break;
        }
    }
}

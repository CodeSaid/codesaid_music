package com.codesaid_music.view.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.codesaid.lib_commin_ui.base.BaseActivity;
import com.codesaid.lib_network.okhttp.listener.DisposeDataListener;
import com.codesaid_music.R;
import com.codesaid_music.api.RequestCenter;
import com.codesaid_music.model.login.LoginEvent;
import com.codesaid_music.model.user.User;
import com.codesaid_music.utils.UserManager;
import com.codesaid_music.view.login.inter.IUserLoginView;
import com.codesaid_music.view.login.presenter.UserLoginPresenter;

import org.greenrobot.eventbus.EventBus;

/**
 * Created By codesaid
 * On :2019-11-03
 * Package Name: com.codesaid_music.view.login
 * desc: 登录页面
 */
public class LoginActivity extends BaseActivity implements IUserLoginView {

    private UserLoginPresenter mUserLoginPresenter;

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);

        //初始化P层
        mUserLoginPresenter = new UserLoginPresenter(this);

        findViewById(R.id.login_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserLoginPresenter.login(getUserName(), getPassword());
            }
        });
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

package com.codesaid_music.view.login.presenter;

import android.widget.Toast;

import com.codesaid.lib_network.okhttp.listener.DisposeDataListener;
import com.codesaid_music.api.MockData;
import com.codesaid_music.api.RequestCenter;
import com.codesaid_music.application.MusicVoiceApplication;
import com.codesaid_music.model.login.LoginEvent;
import com.codesaid_music.model.user.User;
import com.codesaid_music.utils.UserManager;
import com.codesaid_music.view.login.inter.IUserLoginPresenter;
import com.codesaid_music.view.login.inter.IUserLoginView;
import com.codesaid_music.view.login.sign.ISignListener;
import com.codesaid_music.view.login.sign.SignHandler;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

/**
 * Created By codesaid
 * On :2019-12-03
 * Package Name: com.codesaid_music.view.login.presenter
 * desc  : login 页面对应Presenter
 */
public class UserLoginPresenter implements IUserLoginPresenter, DisposeDataListener {

    private IUserLoginView mIView;

    private String email;
    private String password;

    public UserLoginPresenter(IUserLoginView iView) {
        mIView = iView;
    }

    @Override
    public void onSuccess(Object responseObj) {
        mIView.hideLoadingView();
        User user = (User) responseObj;
        // 判断用户输入的账号密码是否正确
        if (email.equals(user.mobile) && password.equals(user.password)) {
            UserManager.getInstance().setUser(user);
            // 发送 登录 Event
            EventBus.getDefault().post(new LoginEvent());
            // 把数据存储到数据库中
            SignHandler.onSignUp((String) responseObj, new ISignListener() {
                @Override
                public void onSignInSuccess() {

                }

                @Override
                public void onSignUpSuccess() {

                }
            });
            mIView.finishActivity();
        } else {
            Toast.makeText(MusicVoiceApplication.getInstance(),
                    "您的账号或者密码有误!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Object reasonObj) {
        mIView.hideLoadingView();
        onSuccess(new Gson().fromJson(MockData.LOGIN_DATA, User.class));
        mIView.showLoginFailedView();
    }

    @Override
    public void login(String email, String password) {
        mIView.showLoadingView();
        this.email = email;
        this.password = password;
        RequestCenter.login(this);
    }
}

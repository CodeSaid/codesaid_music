package com.codesaid_music.view.login.sign;

/**
 * Created By codesaid
 * On :2019-12-10
 * Package Name: com.codesaid_music.view.login.sign
 * desc : 监听是否有用户信息的接口
 */

public interface IUserChecker {

    void onSignIn();

    void onNotSignIn();
}

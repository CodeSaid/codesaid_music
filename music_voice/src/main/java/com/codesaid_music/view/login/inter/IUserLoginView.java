package com.codesaid_music.view.login.inter;

/**
 * Created By codesaid
 * On :2019-12-03
 * Package Name: com.codesaid_music.view.login.inter
 * desc : UI层对外提供的方法
 */
public interface IUserLoginView {

    String getUserName();

    String getPassword();

    void finishActivity();

    void showLoginFailedView();

    void showLoadingView();

    void hideLoadingView();
}

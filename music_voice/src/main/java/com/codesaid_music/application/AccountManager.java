package com.codesaid_music.application;

import com.codesaid_music.utils.storage.LattePreference;
import com.codesaid_music.view.login.sign.IUserChecker;

/**
 * Created By codesaid
 * On :2019-12-10
 * Package Name: com.codesaid_music.application
 * desc : 管理用户信息
 */
public class AccountManager {
    private enum SignTag {
        SIGN_TAG
    }

    /**
     * 保存用户登录状态，登录后调用
     *
     * @param state
     */
    public static void setSignState(boolean state) {
        LattePreference.setAppFlag(SignTag.SIGN_TAG.name(), state);
    }

    public static boolean isSignIn() {
        return LattePreference.getAppFlag(SignTag.SIGN_TAG.name());
    }

    public static void checkAccount(IUserChecker checker) {
        if (isSignIn()) {
            checker.onSignIn();
        } else {
            checker.onNotSignIn();
        }
    }
}

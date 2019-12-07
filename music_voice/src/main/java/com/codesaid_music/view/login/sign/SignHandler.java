package com.codesaid_music.view.login.sign;

import android.accounts.AccountManager;

import org.json.JSONObject;

/**
 * Created By codesaid
 * On :2019-12-07
 * Package Name: com.codesaid_music.view.login.sign
 */
public class SignHandler {
    public static void onSignUp(String json, ISignListener signListener) {
        JSONObject profileJson = JSON.parseObject(json).getJSONObject("data");
        final long userId = profileJson.getLong("userId");
        final String name = profileJson.getString("name");
        final String avatar = profileJson.getString("avatar");
        final String gender = profileJson.getString("gender");
        final String address = profileJson.getString("address");

        final UserProfile profile = new UserProfile(userId, name, avatar, gender, address);
        // 把数据插入到数据库中
        DatabaseManager.getInstance().getDao().insert(profile);

        // 已经注册并且登录成功了，保存用户信息状态
        AccountManager.setSignState(true);
        signListener.onSignInSuccess();
    }

    public static void onSignIn(String json, ISignListener signListener) {
        JSONObject profileJson = JSON.parseObject(json).getJSONObject("data");
        final long userId = profileJson.getLong("userId");
        final String name = profileJson.getString("name");
        final String avatar = profileJson.getString("avatar");
        final String gender = profileJson.getString("gender");
        final String address = profileJson.getString("address");

        final UserProfile profile = new UserProfile(userId, name, avatar, gender, address);
        // 把数据插入到数据库中
        DatabaseManager.getInstance().getDao().insert(profile);

        // 已经注册并且登录成功了，保存用户信息状态
        AccountManager.setSignState(true);
        signListener.onSignUpSuccess();
    }
}

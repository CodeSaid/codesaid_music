package com.codesaid_music.api;

import com.codesaid.lib_network.CommonOkHttpClient;
import com.codesaid.lib_network.okhttp.listener.DisposeDataHandle;
import com.codesaid.lib_network.okhttp.listener.DisposeDataListener;
import com.codesaid_music.model.discory.BaseRecommendModel;
import com.codesaid_music.model.discory.BaseRecommendMoreModel;
import com.codesaid_music.model.friend.FriendModel;
import com.codesaid_music.model.user.User;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 * Created By codesaid
 * On :2019-11-01
 * Package Name: com.codesaid_music.api
 * desc: 网络请求中心
 */
public class RequestCenter {

    private static final String ROOT_URL = "http://132.232.72.120:8080";

    private static final String USER_REGISTER = ROOT_URL + "/music/register.json";

    private static String LOGIN = ROOT_URL + "/music/login.json";

    private static String FRIEND = ROOT_URL + "/music/friend.json";

    private static String HOME_RECOMMEND = ROOT_URL + "/music/recommand.json";

    private static String HOME_RECOMMEND_MORE = ROOT_URL + "/music/recommand_more.json";


    static class HttpConstants {
        //        private static final String ROOT_URL = "http://imooc.com/api";
        private static final String ROOT_URL = "http://132.232.72.120:8080/music";

        /**
         * 首页请求接口
         */
        private static String HOME_RECOMMAND = ROOT_URL + "/module_voice/home_recommand";

        private static String HOME_RECOMMAND_MORE = ROOT_URL + "/module_voice/home_recommand_more";

        private static String HOME_FRIEND = ROOT_URL + "/module_voice/home_friend";

        /**
         * 登陆接口
         */
        //static String LOGIN = "http://132.232.72.120:8080/music/login.json";
    }

    //根据参数发送所有post请求
    private static void getRequest(String url, DisposeDataListener listener,
                                   Class<?> clazz) {

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        CommonOkHttpClient.get(request, new DisposeDataHandle(listener, clazz));
    }

    private static void postRequest(String url, FormBody formBody,
                                    DisposeDataListener listener) {
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        CommonOkHttpClient.post(request, new DisposeDataHandle(listener));
    }

    /**
     * 用户登陆请求
     */
    public static void login(DisposeDataListener listener) {
        RequestCenter.getRequest(LOGIN, listener, User.class);
    }

    /**
     * 获取朋友页面数据
     *
     * @param listener listener
     */
    public static void getFriendData(DisposeDataListener listener) {
        RequestCenter.getRequest(FRIEND, listener, FriendModel.class);
    }

    /**
     * 获取首页 Fragment 发现页面数据
     *
     * @param listener listener
     */
    public static void getRecommendData(DisposeDataListener listener) {
        RequestCenter.getRequest(HOME_RECOMMEND, listener, BaseRecommendModel.class);
    }

    /**
     * 获取首页 Fragment 发现页面   加载更多    数据
     *
     * @param listener listener
     */
    public static void getRecommendMoreData(DisposeDataListener listener) {
        RequestCenter.getRequest(HOME_RECOMMEND_MORE, listener, BaseRecommendMoreModel.class);
    }

    public static void postUserRegister(FormBody formBody, DisposeDataListener listener) {
        RequestCenter.postRequest(USER_REGISTER, formBody, listener);
    }
}
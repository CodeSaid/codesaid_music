package com.codesaid_music.api;

import com.codesaid.lib_network.CommonOkHttpClient;
import com.codesaid.lib_network.okhttp.listener.DisposeDataHandle;
import com.codesaid.lib_network.okhttp.listener.DisposeDataListener;
import com.codesaid_music.model.user.User;

import okhttp3.Request;

/**
 * Created By codesaid
 * On :2019-11-01
 * Package Name: com.codesaid_music.api
 * desc: 网络请求中心
 */
public class RequestCenter {

    public static String LOGIN = "http://132.232.72.120:8080/music/login.json";

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

    //    public static void requestRecommandData(DisposeDataListener listener) {
    //        RequestCenter.getRequest(HttpConstants.HOME_RECOMMAND, null, listener,
    //                BaseRecommandModel.class);
    //    }

    //    public static void requestRecommandMore(DisposeDataListener listener) {
    //        RequestCenter.getRequest(HttpConstants.HOME_RECOMMAND_MORE, null, listener,
    //                BaseRecommandMoreModel.class);
    //    }

    //    public static void requestFriendData(DisposeDataListener listener) {
    //        RequestCenter.getRequest(HttpConstants.HOME_FRIEND, null, listener, BaseFriendModel.class);
    //    }

    /**
     * 用户登陆请求
     */
    public static void login(DisposeDataListener listener) {
        RequestCenter.getRequest(LOGIN, listener, User.class);
    }
}

package com.codesaid_music.api;

import com.codesaid.lib_network.CommonOkHttpClient;
import com.codesaid.lib_network.okhttp.listener.DisposeDataHandle;
import com.codesaid.lib_network.okhttp.listener.DisposeDataListener;
import com.codesaid.lib_network.okhttp.request.CommonRequest;
import com.codesaid.lib_network.okhttp.request.RequestParams;

/**
 * Created By codesaid
 * On :2019-11-01
 * Package Name: com.codesaid_music.api
 * desc: 网络请求中心
 */
public class RequestCenter {

    static class HttpConstants {
        private static final String ROOT_URL = "http://imooc.com/api";
        //private static final String ROOT_URL = "http://39.97.122.129";

        /**
         * 首页请求接口
         */
        private static String HOME_RECOMMAND = ROOT_URL + "/module_voice/home_recommand";

        private static String HOME_RECOMMAND_MORE = ROOT_URL + "/module_voice/home_recommand_more";

        private static String HOME_FRIEND = ROOT_URL + "/module_voice/home_friend";

        /**
         * 登陆接口
         */
        static String LOGIN = ROOT_URL + "/module_voice/login_phone";
    }

    //根据参数发送所有post请求
    private static void getRequest(String url, RequestParams params, DisposeDataListener listener,
                                   Class<?> clazz) {
        CommonOkHttpClient.post(CommonRequest.
                createPostRequest(url, params), new DisposeDataHandle(listener, clazz));
    }

    /**
     * 用户登陆请求
     */
    public static void login(DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        params.put("mb", "18734924592");
        params.put("pwd", "999999q");
        RequestCenter.getRequest(HttpConstants.LOGIN, params, listener, null);
    }
}

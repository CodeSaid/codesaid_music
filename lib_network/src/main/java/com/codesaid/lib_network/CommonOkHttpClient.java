package com.codesaid.lib_network;

import android.text.TextUtils;

import com.codesaid.lib_network.okhttp.exception.OkHttpException;
import com.codesaid.lib_network.okhttp.listener.DisposeDataHandle;
import com.codesaid.lib_network.okhttp.response.CommonFileCallback;
import com.codesaid.lib_network.okhttp.response.CommonJsonCallback;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created By codesaid
 * On :2019-11-01
 * Package Name: com.codesaid.lib_network
 * desc: 用来发送 get/post 请求的工具类，包含一些共用的参数
 */
public class CommonOkHttpClient {

    protected static final int NETWORK_ERROR = -1; // the network relative error
    protected static final String EMPTY_MSG = "";
    protected static final int JSON_ERROR = -2; // the JSON relative error

    private static final int TIME_OUT = 30;
    private static OkHttpClient mOkHttpClient;

    private DisposeDataHandle handle;

    /**
     * 对 OkHttpClient 进行初始化
     */
    static {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        /**
         *  为所有请求添加请求头，看个人需求
         */
        //        okHttpClientBuilder.addInterceptor(new Interceptor() {
        //            @Override
        //            public Response intercept(Chain chain) throws IOException {
        //                Request request =
        //                        chain.request().newBuilder().addHeader("User-Agent", "Imooc-Mobile") // 标明发送本次请求的客户端
        //                                .build();
        //                return chain.proceed(request);
        //            }
        //        });
        //        okHttpClientBuilder.cookieJar(new SimpleCookieJar());
        okHttpClientBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.followRedirects(true);
        /**
         * trust all the https point
         */
        //        okHttpClientBuilder.sslSocketFactory(HttpsUtils.initSSLSocketFactory(),
        //                HttpsUtils.initTrustManager());
        mOkHttpClient = okHttpClientBuilder.build();
    }

    public static OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * get 请求
     *
     * @param request 请求参数
     * @param handle  回调
     * @return
     */
    public static Call get(Request request, final DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        //        call.enqueue(new CommonJsonCallback(handle));
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handle.mListener.onFailure(new OkHttpException(NETWORK_ERROR, e));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String result = response.body().string();
                // 判断返回的数据是否为空
                if (TextUtils.isEmpty(result)) {
                    handle.mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    // 判断是否需要解析数据
                    if (handle.mClass == null) {
                        handle.mListener.onSuccess(jsonObject);
                    } else {
                        // 解析数据为 json
                        Object object = new Gson().fromJson(result, handle.mClass);
                        if (object != null) {
                            // 判断解析出来的数据是否为空
                            handle.mListener.onSuccess(object);
                        } else {
                            handle.mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                        }
                    }
                } catch (Exception e) {
                    // 解析异常
                    handle.mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                    e.printStackTrace();
                }
            }
        });
        return call;
    }

    /**
     * post 请求
     *
     * @param request 请求参数
     * @param handle  回调
     * @return
     */
    public static Call post(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }

    /**
     * 文件下载
     *
     * @param request 请求参数
     * @param handle  回调
     * @return
     */
    public static Call downloadFile(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonFileCallback(handle));
        return call;
    }
}

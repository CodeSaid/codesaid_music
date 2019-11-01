package com.codesaid.lib_network.okhttp.response;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.codesaid.lib_network.okhttp.exception.OkHttpException;
import com.codesaid.lib_network.okhttp.listener.DisposeDataHandle;
import com.codesaid.lib_network.okhttp.listener.DisposeDataListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created By codesaid
 * On :2019-11-01
 * Package Name: com.codesaid.lib_network.okhttp.response
 * desc: 处理 json 类型的响应
 */
public class CommonJsonCallback implements Callback {

    /**
     * the logic layer exception, may alter in different app
     */
    protected final String RESULT_CODE = "ecode"; // 有返回则对于http请求来说是成功的，但还有可能是业务逻辑上的错误
    protected final int RESULT_CODE_VALUE = 0;
    protected final String ERROR_MSG = "emsg";
    protected final String EMPTY_MSG = "";

    /**
     * the java layer exception, do not same to the logic error
     */
    protected final int NETWORK_ERROR = -1; // the network relative error
    protected final int JSON_ERROR = -2; // the JSON relative error
    protected final int OTHER_ERROR = -3; // the unknow error

    private Handler mDeliveryHandler;
    private DisposeDataListener mListener;
    private Class<?> mClass;

    public CommonJsonCallback(DisposeDataHandle handle) {
        this.mListener = handle.mListener;
        this.mClass = handle.mClass;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(NETWORK_ERROR, e));
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String result = response.body().string();
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);
            }
        });
    }

    /**
     * 处理返回的数据
     *
     * @param result 服务器返回的数据
     */
    private void handleResponse(Object result) {
        // 判断返回的数据是否为空
        if (TextUtils.isEmpty(result.toString())) {
            mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(result.toString());
            // 判断是否需要解析数据
            if (mClass == null) {
                mListener.onSuccess(jsonObject);
            } else {
                // 解析数据为 json
                Object object = new Gson().fromJson(result.toString(), mClass);
                if (object != null) {
                    // 判断解析出来的数据是否为空
                    mListener.onSuccess(object);
                } else {
                    mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                }
            }
        } catch (Exception e) {
            // 解析异常
            mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
            e.printStackTrace();
        }
    }
}

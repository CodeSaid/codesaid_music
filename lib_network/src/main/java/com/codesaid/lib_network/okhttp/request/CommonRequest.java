package com.codesaid.lib_network.okhttp.request;

import java.io.File;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created By codesaid
 * On :2019-11-01
 * Package Name: com.codesaid.lib_network.okhttp.request
 * desc: 封装 get/post 文件上传请求对象
 */
public class CommonRequest {

    /**
     * 对外创建 post 请求对象 , 无请求头
     *
     * @param url    地址
     * @param params 请求参数
     * @return request 对象
     */
    public static Request createPostRequest(String url, RequestParams params) {
        return createPostRequest(url, params, null);
    }

    /**
     * 对外创建 post 请求对象 , 带请求头
     *
     * @param url    地址
     * @param params 请求参数
     * @param header 请求头
     * @return request 对象
     */
    public static Request createPostRequest(String url, RequestParams params, RequestParams header) {
        FormBody.Builder formBody = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                // 参数遍历
                formBody.add(entry.getKey(), entry.getValue());
            }
        }

        Headers.Builder mHeaderBuilder = new Headers.Builder();
        if (header != null) {
            for (Map.Entry<String, String> entry : header.urlParams.entrySet()) {
                // 请求头遍历
                mHeaderBuilder.add(entry.getKey(), entry.getValue());
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .headers(mHeaderBuilder.build())
                .post(formBody.build())
                .build();

        return request;
    }

    /**
     * 对外创建 get 请求对象 , 无请求头
     *
     * @param url    地址
     * @param params 请求参数
     * @return request 对象
     */
    public static Request createGetRequest(String url, RequestParams params) {
        return createGetRequest(url, params, null);
    }

    /**
     * 对外创建 get 请求对象 , 带请求头
     *
     * @param url    地址
     * @param params 请求参数
     * @param header 请求头
     * @return request 对象
     */
    public static Request createGetRequest(String url, RequestParams params, RequestParams header) {
        StringBuilder stringBuilder = new StringBuilder(url).append("?");
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }

        Headers.Builder mHeaderBuilder = new Headers.Builder();
        if (header != null) {
            for (Map.Entry<String, String> entry : header.urlParams.entrySet()) {
                mHeaderBuilder.add(entry.getKey(), entry.getValue());
            }
        }

        Request request = new Request.Builder()
                .url(stringBuilder.substring(0, stringBuilder.length() - 1))
                .get()
                .headers(mHeaderBuilder.build())
                .build();

        return request;
    }


    private static final MediaType FILE_TYPE = MediaType.parse("application/octet-stream");

    /**
     * 文件上传请求
     *
     * @param url    地址
     * @param params 请求参数
     * @return request 对象
     */
    public static Request createMultiPostRequest(String url, RequestParams params) {
        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        requestBody.setType(MultipartBody.FORM);
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.fileParams.entrySet()) {
                if (entry.getValue() instanceof File) {
                    requestBody.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
                            RequestBody.create(FILE_TYPE, (File) entry.getValue()));
                } else if (entry.getValue() instanceof String) {

                    requestBody.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
                            RequestBody.create(null, (String) entry.getValue()));
                }
            }
        }
        return new Request.Builder().url(url).post(requestBody.build()).build();
    }
}

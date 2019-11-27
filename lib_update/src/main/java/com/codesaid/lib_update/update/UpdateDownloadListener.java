package com.codesaid.lib_update.update;

/**
 * Created By codesaid
 * On :2019-11-27
 * Package Name: com.codesaid.lib_update.update
 * desc :下载不同状态接口回调
 */
public interface UpdateDownloadListener {
    /**
     * 下载请求开始回调
     */
    void onStarted();

    /**
     * 请求成功，下载前的准备回调
     *
     * @param contentLength 文件长度
     * @param downloadUrl   下载地址
     */
    void onPrepared(long contentLength, String downloadUrl);

    /**
     * 进度更新回调
     *
     * @param progress
     * @param downloadUrl
     */
    void onProgressChanged(int progress, String downloadUrl);

    /**
     * 下载过程中暂停的回调
     *
     * @param completeSize
     * @param downloadUrl
     */
    void onPaused(int progress, int completeSize, String downloadUrl);

    /**
     * 下载完成回调
     *
     * @param completeSize
     * @param downloadUrl
     */
    void onFinished(int completeSize, String downloadUrl);

    /**
     * 下载失败回调
     */
    void onFailure();
}

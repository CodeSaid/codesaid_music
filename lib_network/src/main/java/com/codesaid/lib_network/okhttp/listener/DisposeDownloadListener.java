package com.codesaid.lib_network.okhttp.listener;

/**
 * @author codesaid
 * desc: 监听下载进度
 */
public interface DisposeDownloadListener extends DisposeDataListener {
    void onProgress(int progress);
}

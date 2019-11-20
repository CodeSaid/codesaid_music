package com.codesaid.video.videoplayer;

/**
 * Created By codesaid
 * On :2019-11-18
 * Package Name: com.codesaid.video.videoplayer
 * desc : 与应用层通信接口
 */
public interface VideoContextInterface {
    void onVideoSuccess();

    void onVideoFailed();

    void onVideoComplete();
}

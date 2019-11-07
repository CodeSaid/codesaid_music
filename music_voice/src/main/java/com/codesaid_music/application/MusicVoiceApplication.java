package com.codesaid_music.application;

import android.app.Application;

import com.codesaid.lib_audio.app.AudioHelper;

public class MusicVoiceApplication extends Application {

    private static MusicVoiceApplication mApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        //视频SDK初始化
        //        VideoHelper.init(this);
        //音频SDK初始化
        AudioHelper.init(this);
        //分享SDK初始化
        //        ShareManager.initSDK(this);
        //更新组件下载
        //        UpdateHelper.init(this);
        //ARouter初始化
        //        ARouter.init(this);
    }

    public static MusicVoiceApplication getInstance() {
        return mApplication;
    }
}

package com.codesaid_music.application;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.codesaid.lib_audio.app.AudioHelper;
import com.codesaid.lib_share.ShareManager;
import com.codesaid.lib_update.app.UpdateHelper;
import com.codesaid.video.app.VideoHelper;

public class MusicVoiceApplication extends Application {

    private static MusicVoiceApplication mApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        //视频SDK初始化
        VideoHelper.init(this);
        //音频SDK初始化
        AudioHelper.init(this);
        //分享SDK初始化
        ShareManager.init(this);
        //更新组件下载
        UpdateHelper.init(this);
        //ARouter初始化
        ARouter.init(this);
    }

    public static MusicVoiceApplication getInstance() {
        return mApplication;
    }
}

package com.codesaid.lib_audio.app;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.codesaid.lib_audio.mediaplayer.core.AudioController;
import com.codesaid.lib_base.audio.AudioService;

/**
 * Created By codesaid
 * On :2019-11-22
 * Package Name: com.codesaid.lib_audio.app
 * desc : AudioService 实现类
 */
@Route(path = "/audio/audio_service")
public class AudioServiceImpl implements AudioService {

    @Override
    public void init(Context context) {

    }

    @Override
    public void pauseAudio() {
        AudioController.getInstance().pause();
    }

    @Override
    public void resumeAudio() {
        AudioController.getInstance().resume();
    }

}

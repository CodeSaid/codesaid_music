package com.codesaid.lib_base.audio;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Created By codesaid
 * On :2019-11-22
 * Package Name: com.codesaid.lib_base.audio
 * desc: 音频基础库对外
 */
public interface AudioService extends IProvider {
    void pauseAudio();

    void resumeAudio();
}

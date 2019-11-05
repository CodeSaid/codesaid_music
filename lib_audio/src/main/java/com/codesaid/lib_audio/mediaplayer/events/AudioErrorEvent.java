package com.codesaid.lib_audio.mediaplayer.events;

public class AudioErrorEvent {

    // 标识错误的来源
    private int errorCode;

    public AudioErrorEvent(int errorCode) {
        this.errorCode = errorCode;
    }
}

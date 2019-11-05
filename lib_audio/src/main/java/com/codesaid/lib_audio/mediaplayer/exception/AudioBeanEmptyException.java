package com.codesaid.lib_audio.mediaplayer.exception;

/**
 * 播放队列为空异常
 */
public class AudioBeanEmptyException extends RuntimeException {

    public AudioBeanEmptyException(String error) {
        super(error);
    }
}

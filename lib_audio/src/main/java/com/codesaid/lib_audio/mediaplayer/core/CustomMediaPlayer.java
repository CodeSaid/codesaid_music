package com.codesaid.lib_audio.mediaplayer.core;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created By codesaid
 * On :2019-11-04
 * Package Name: com.codesaid.lib_audio.mediaplayer.core
 * desc: 带状态的 MediaPlayer
 */
public class CustomMediaPlayer extends MediaPlayer implements MediaPlayer.OnCompletionListener {

    public enum Status {
        IDLE, INITIALIZED, STARTED, PAUSED, STOPPED, COMPLETED
    }

    private OnCompletionListener mListener;

    private Status mStatus;

    public CustomMediaPlayer() {
        super();
        mStatus = Status.IDLE;
        super.setOnCompletionListener(this);
    }

    @Override
    public void reset() {
        super.reset();
        mStatus = Status.IDLE;
    }

    @Override
    public void setDataSource(String path) throws IOException, IllegalArgumentException,
            IllegalStateException, SecurityException {
        super.setDataSource(path);
        mStatus = Status.INITIALIZED;
    }

    @Override
    public void start() throws IllegalStateException {
        super.start();
        mStatus = Status.STARTED;
    }

    @Override
    public void pause() throws IllegalStateException {
        super.pause();
        mStatus = Status.PAUSED;
    }

    @Override
    public void stop() throws IllegalStateException {
        super.stop();
        mStatus = Status.STOPPED;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mStatus = Status.COMPLETED;
    }

    /**
     * 获取当前状态
     *
     * @return 状态值
     */
    public Status getStatus() {
        return mStatus;
    }

    /**
     * 是否播放完成
     *
     * @return true 完成
     */
    public boolean isComplete() {
        return mStatus == Status.COMPLETED;
    }

    /**
     * 设置完成事件
     *
     * @param listener listener
     */
    public void setCompleteListener(OnCompletionListener listener) {
        mListener = listener;
    }
}

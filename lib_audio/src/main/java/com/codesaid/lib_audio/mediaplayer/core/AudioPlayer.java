package com.codesaid.lib_audio.mediaplayer.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.codesaid.lib_audio.app.AudioHelper;
import com.codesaid.lib_audio.mediaplayer.model.AudioBean;


/**
 * Created By codesaid
 * On :2019-11-04
 * Package Name: com.codesaid.lib_audio.mediaplayer.core
 * desc: 1. 播放音频
 * 2. 对外发送各种类型的事件
 */
public class AudioPlayer implements MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        AudioFocusManager.AudioFocusListener {

    private static final String TAG = "AudioPlayer";
    private static final int TIME_MSG = 0x01;
    private static final int TIME_INVAL = 100;
    //真正负责播放的核心MediaPlayer子类
    private CustomMediaPlayer mMediaPlayer;
    private WifiManager.WifiLock mWifiLock;

    // 音频焦点监听器
    private AudioFocusManager mAudioFocusManager;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_MSG:
                    break;
            }
        }
    };

    public AudioPlayer() {
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mMediaPlayer = new CustomMediaPlayer();
        // 使用唤醒锁
        mMediaPlayer.setWakeMode(AudioHelper.getContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnErrorListener(this);

        // 初始化wifi锁
        mWifiLock = ((WifiManager) AudioHelper.getContext()
                .getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL, TAG);
        // 初始化音频焦点管理器
        mAudioFocusManager = new AudioFocusManager(AudioHelper.getContext(), this);
    }

    /**
     * 对外提供的加载方法
     *
     * @param audioBean AudioBean
     */
    public void load(AudioBean audioBean) {
        try {
            // 正常加载逻辑
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(audioBean.mUrl);
            mMediaPlayer.prepareAsync();
            // TODO 对外发送 load 事件
        } catch (Exception e) {
            // TODO 对外发送 error 事件
            e.printStackTrace();
        }
    }

    /**
     * 对外提供的 start 方法
     */
    @SuppressLint("ShowToast")
    public void start() {
        if (!mAudioFocusManager.requestAudioFocus()) {
            Log.e(TAG, "获取音频焦点失败");
            Toast.makeText(AudioHelper.getContext(), "获取音频焦点失败", Toast.LENGTH_SHORT);
        }
        mMediaPlayer.start();
        mWifiLock.acquire();

        // TODO 对外发送 start 事件
    }

    /**
     * 对外提供 暂停  方法
     */
    public void pause() {
        if (getStatus() == CustomMediaPlayer.Status.STARTED) {
            mMediaPlayer.pause();

            // 释放音频焦点 wifiLock
            if (mWifiLock.isHeld()) {
                mWifiLock.release();
            }

            // 释放音频焦点
            if (mAudioFocusManager != null) {
                mAudioFocusManager.abandonAudioFocus();
            }

            // TODO 发送暂停事件
        }
    }

    /**
     * 对外提供 恢复 方法
     */
    public void resume() {
        if (getStatus() == CustomMediaPlayer.Status.STOPPED) {
            start();
        }
    }

    /**
     * 清空播放器所暂用的资源
     */
    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            if (mAudioFocusManager != null) {
                mAudioFocusManager.abandonAudioFocus();
            }
            if (mWifiLock.isHeld()) {
                mWifiLock.release();
            }
            mAudioFocusManager = null;
            mWifiLock = null;
        }

        // TODO 发送 release 销毁事件
    }

    /**
     * 对外提供的  获取 CustomMediaPlayer.Status 状态的方法
     *
     * @return 返回 CustomMediaPlayer.Status 状态
     */
    public CustomMediaPlayer.Status getStatus() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getStatus();
        }
        return CustomMediaPlayer.Status.STOPPED;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    @Override
    public void audioFocusGrant() {

    }

    @Override
    public void audioFocusLoss() {

    }

    @Override
    public void audioFocusLossTransient() {

    }

    @Override
    public void audioFocusLossDuck() {

    }
}

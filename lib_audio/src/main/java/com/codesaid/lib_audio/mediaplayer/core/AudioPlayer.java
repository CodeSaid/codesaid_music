package com.codesaid.lib_audio.mediaplayer.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;

import com.codesaid.lib_audio.app.AudioHelper;


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

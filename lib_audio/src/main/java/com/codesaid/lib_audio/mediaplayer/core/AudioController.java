package com.codesaid.lib_audio.mediaplayer.core;

import com.codesaid.lib_audio.mediaplayer.exception.AudioBeanEmptyException;
import com.codesaid.lib_audio.mediaplayer.model.AudioBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created By codesaid
 * On :2019-11-05
 * Package Name: com.codesaid.lib_audio.mediaplayer.core
 * desc: 控制播放逻辑类
 */
public class AudioController {

    private static AudioController mInstance;

    /**
     * 播放方式
     */
    public enum PlayMode {
        /**
         * 列表循环
         */
        LOOP,
        /**
         * 随机
         */
        RANDOM,
        /**
         * 单曲循环
         */
        REPEAT
    }

    private AudioPlayer mAudioPlayer;
    // 歌曲队列
    private ArrayList<AudioBean> mAudioBeans;
    // 循环模式
    private PlayMode mPlayMode;
    // 当前播放歌曲索引
    private int mAudioBeanIndex;

    private AudioController() {
        EventBus.getDefault().register(this);
        mAudioPlayer = new AudioPlayer();
        mAudioBeans = new ArrayList<>();
        mAudioBeanIndex = 0;
        mPlayMode = PlayMode.LOOP;
    }

    public static AudioController getInstance() {
        if (mInstance == null) {
            synchronized (AudioController.class) { // 保证单例的唯一性
                if (mInstance == null) {
                    mInstance = new AudioController();
                }
            }
        }
        return mInstance;
    }

    public ArrayList<AudioBean> getAudioBeans() {
        return mAudioBeans == null ? new ArrayList<AudioBean>() : mAudioBeans;
    }

    /**
     * 设置播放队列
     *
     * @param audioBeans AudioBean
     */
    public void setAudioBeans(ArrayList<AudioBean> audioBeans) {
        setAudioBeans(audioBeans, 0);
    }

    /**
     * 设置播放队列 并且 指定要播放的歌曲 index
     *
     * @param audioBeans AudioBean
     * @param index      指定要播放的歌曲 index
     */
    public void setAudioBeans(ArrayList<AudioBean> audioBeans, int index) {
        mAudioBeans.addAll(audioBeans);
        this.mAudioBeanIndex = index;
    }

    public PlayMode getPlayMode() {
        return mPlayMode;
    }

    /**
     * 设置播放模式
     *
     * @param playMode 播放模式
     */
    public void setPlayMode(PlayMode playMode) {
        mPlayMode = playMode;
    }

    public int getAudioBeanIndex() {
        return mAudioBeanIndex;
    }

    /**
     * 设置要播放的歌曲索引
     *
     * @param audioBeanIndex 歌曲索引
     */
    public void setAudioBeanIndex(int audioBeanIndex) {
        if (mAudioBeans == null) {
            throw new AudioBeanEmptyException("当前播放队列为空，请先设置播放队列");
        }
        mAudioBeanIndex = audioBeanIndex;
        play();
    }

    /**
     * 获取当前正在播放的 歌曲
     *
     * @return AudioBean
     */
    private AudioBean getNowPlaying() {
        return null;
    }

    /**
     * 获取上一首要播放的歌曲
     *
     * @return AudioBean
     */
    private AudioBean getNextPlaying() {
        return null;
    }

    /**
     * 获取下一首要播放的歌曲
     *
     * @return AudioBean
     */
    private AudioBean getPreviousPlaying() {
        return null;
    }

    /**
     * 对外提供播放方法
     */
    private void play() {
        AudioBean audioBean = getNowPlaying();
        mAudioPlayer.load(audioBean);
    }

    /**
     * 对外提供暂停方法
     */
    private void pause() {
        mAudioPlayer.pause();
    }

    /**
     * 对外提供恢复播放方法
     */
    private void resume() {
        mAudioPlayer.resume();
    }

    /**
     * 对外提供销毁资源方法
     */
    private void release() {
        mAudioPlayer.release();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 播放下一首歌曲
     */
    private void next() {
        AudioBean audioBean = getNextPlaying();
        mAudioPlayer.load(audioBean);
    }

    /**
     * 播放上一首歌曲
     */
    public void previous() {
        AudioBean audioBean = getPreviousPlaying();
        mAudioPlayer.load(audioBean);
    }

    /**
     * 自动切换播放 / 暂停
     */
    private void playOrPause() {
        if (isStartState()) {
            pause();
        } else if (isPauseStatus()) {
            resume();
        }
    }

    /**
     * 获取播放器当前状态
     */
    private CustomMediaPlayer.Status getStatus() {
        return mAudioPlayer.getStatus();
    }

    /**
     * 当前播放器是否正在播放
     *
     * @return true 正在播放
     */
    public boolean isStartState() {
        return CustomMediaPlayer.Status.STARTED == getStatus();
    }

    /**
     * 当前播放器是暂停状态
     *
     * @return true 暂停状态
     */
    public boolean isPauseStatus() {
        return CustomMediaPlayer.Status.STOPPED == getStatus();
    }
}

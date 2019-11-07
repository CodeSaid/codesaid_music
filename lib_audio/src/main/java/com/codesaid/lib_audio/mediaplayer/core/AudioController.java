package com.codesaid.lib_audio.mediaplayer.core;

import com.codesaid.lib_audio.mediaplayer.events.AudioCompleteEvent;
import com.codesaid.lib_audio.mediaplayer.events.AudioErrorEvent;
import com.codesaid.lib_audio.mediaplayer.exception.AudioBeanEmptyException;
import com.codesaid.lib_audio.mediaplayer.model.AudioBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Random;

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
     * 添加歌曲到头部
     *
     * @param audioBean 歌曲 bean
     */
    public void addAudio(AudioBean audioBean) {
        this.addAudio(0, audioBean);
    }

    /**
     * 添加歌曲到指定位置
     *
     * @param index     索引
     * @param audioBean 歌曲 bean
     */
    public void addAudio(int index, AudioBean audioBean) {
        if (mAudioBeans == null) {
            throw new AudioBeanEmptyException("当前播放队列为空，请先设置播放队列");
        }
        int query = queryAudio(audioBean);
        if (query <= -1) {
            // 没有添加过
            addCustomAudio(index, audioBean);
        } else {
            AudioBean bean = getNowPlaying();
            if (!bean.id.equals(audioBean.id)) {
                // 已经在队列并且不在播放中
                setAudioBeanIndex(query);
            }
        }
    }

    private void addCustomAudio(int index, AudioBean audioBean) {
        if (mAudioBeans == null) {
            throw new AudioBeanEmptyException("当前播放队列为空,请先设置播放队列.");
        }
        mAudioBeans.add(index, audioBean);
    }

    /**
     * 查询歌曲是否在当前队列中
     *
     * @param audioBean AudioBean
     * @return -1: 不存在
     */
    private int queryAudio(AudioBean audioBean) {
        return mAudioBeans.indexOf(audioBean);
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

    public AudioBean getPlaying(int index) {
        if (mAudioBeans != null && !mAudioBeans.isEmpty()
                && index >= 0 && index < mAudioBeans.size()) {
            return mAudioBeans.get(mAudioBeanIndex);
        } else {
            throw new AudioBeanEmptyException("当前播放队列为空，请先设置播放队列");
        }
    }

    /**
     * 获取当前正在播放的 歌曲
     *
     * @return AudioBean
     */
    private AudioBean getNowPlaying() {
        return getPlaying(mAudioBeanIndex);
    }

    /**
     * 获取上一首要播放的歌曲
     *
     * @return AudioBean
     */
    private AudioBean getNextPlaying() {
        switch (mPlayMode) {
            case LOOP:
                mAudioBeanIndex = (mAudioBeanIndex + mAudioBeans.size() - 1) % mAudioBeans.size();
                return getPlaying(mAudioBeanIndex);
            case RANDOM:
                mAudioBeanIndex = new Random().nextInt(mAudioBeans.size()) % mAudioBeans.size();
                return getPlaying(mAudioBeanIndex);
            case REPEAT:
                return getPlaying(mAudioBeanIndex);
        }
        return null;
    }

    /**
     * 获取下一首要播放的歌曲
     *
     * @return AudioBean
     */
    private AudioBean getPreviousPlaying() {
        switch (mPlayMode) {
            case LOOP:
                mAudioBeanIndex = (mAudioBeanIndex + mAudioBeans.size() - 1) % mAudioBeans.size();
                return getPlaying(mAudioBeanIndex);
            case RANDOM:
                mAudioBeanIndex = new Random().nextInt(mAudioBeans.size()) % mAudioBeans.size();
                return getPlaying(mAudioBeanIndex);
            case REPEAT:
                return getPlaying(mAudioBeanIndex);
        }
        return null;
    }

    /**
     * 对外提供播放方法
     */
    public void play() {
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
    public void playOrPause() {
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

    //插放完毕事件处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioCompleteEvent(
            AudioCompleteEvent event) {
        next();
    }

    //播放出错事件处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioErrorEvent(AudioErrorEvent event) {
        next();
    }
}

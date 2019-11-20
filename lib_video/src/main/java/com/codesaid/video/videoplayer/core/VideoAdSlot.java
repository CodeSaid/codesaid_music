package com.codesaid.video.videoplayer.core;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.codesaid.video.videoplayer.core.view.CustomVideoView;
import com.codesaid.video.videoplayer.core.view.VideoFullDialog;

/**
 * Created By codesaid
 * On :2019-11-19
 * Package Name: com.codesaid.video.videoplayer.core
 * desc: 视频业务逻辑层
 */
public class VideoAdSlot implements CustomVideoView.ADVideoPlayerListener {

    private Context mContext;

    /**
     * UI
     */
    private CustomVideoView mCustomVideoView;
    private ViewGroup mParentView;

    /**
     * Data
     */
    private String mVideoUrl;
    private SDKSlotListener mSlotListener;

    public VideoAdSlot(String videoUrl, SDKSlotListener slotListener) {
        mVideoUrl = videoUrl;
        mSlotListener = slotListener;
        mParentView = slotListener.getAdParent();
        mContext = mParentView.getContext();

        initVideoView();
    }

    private void initVideoView() {
        mCustomVideoView = new CustomVideoView(mContext);
        if (mVideoUrl != null) {
            mCustomVideoView.setDataResourse(mVideoUrl);
            mCustomVideoView.setListener(this);
        }
        RelativeLayout paddingView = new RelativeLayout(mContext);
        paddingView.setBackgroundColor(mContext.getResources().getColor(android.R.color.black));
        paddingView.setLayoutParams(mCustomVideoView.getLayoutParams());
        // 添加布局到容器中
        mParentView.addView(paddingView);
        mParentView.addView(mCustomVideoView);
    }

    private boolean isRealPause() {
        if (mCustomVideoView != null) {
            return mCustomVideoView.isRealPause();
        }
        return false;
    }

    private boolean isComplete() {
        if (mCustomVideoView != null) {
            return mCustomVideoView.isComplete();
        }
        return false;
    }

    /**
     * pause the video
     */
    private void pauseVideo() {
        if (mCustomVideoView != null) {
            mCustomVideoView.seekAndPause(0);
        }
    }

    /**
     * resume the video
     */
    private void resumeVideo() {
        if (mCustomVideoView != null) {
            mCustomVideoView.resume();
        }
    }

    /**
     * destroy the video
     */
    public void destroy() {
        mCustomVideoView.destroy();
        mCustomVideoView = null;
        mContext = null;
        mVideoUrl = null;
    }

    @Override
    public void onBufferUpdate(int time) {

    }

    @Override
    public void onClickFullScreenBtn() {
        //获取 VideoView 在当前界面的属性
        mParentView.removeView(mCustomVideoView);
        VideoFullDialog dialog = new VideoFullDialog(mContext, mCustomVideoView,
                mVideoUrl, mCustomVideoView.getCurrentPosition());
        dialog.setListener(new VideoFullDialog.FullToSmallListener() {
            @Override
            public void getCurrentPlayPosition(int position) {
                backToSmallMode(position);
            }

            @Override
            public void playComplete() {
                bigPlayComplete();
            }
        });
        dialog.setSlotListener(mSlotListener);
        dialog.show();

    }

    /**
     * 全屏回到小屏继续播放事件
     *
     * @param position 播放的位置
     */
    private void backToSmallMode(int position) {
        if (mCustomVideoView.getParent() == null) {
            mParentView.addView(mCustomVideoView);
        }
        mCustomVideoView.isShowFullBtn(true);
        mCustomVideoView.mute(true);
        mCustomVideoView.setListener(this);
        // 继续从全屏播放的位置 播放
        mCustomVideoView.seekAndResume(position);
    }

    /**
     * 全屏播放完毕回到小屏事件
     */
    private void bigPlayComplete() {
        if (mCustomVideoView.getParent() == null) {
            mParentView.addView(mCustomVideoView);
        }
        mCustomVideoView.isShowFullBtn(true);
        mCustomVideoView.mute(true);
        mCustomVideoView.setListener(this);
        mCustomVideoView.seekAndPause(0);
    }

    @Override
    public void onClickVideo() {

    }

    @Override
    public void onClickBackBtn() {

    }

    @Override
    public void onClickPlay() {

    }

    @Override
    public void onAdVideoLoadSuccess() {
        if (mSlotListener != null) {
            mSlotListener.onVideoLoadSuccess();
        }
    }

    @Override
    public void onAdVideoLoadFailed() {
        if (mSlotListener != null) {
            mSlotListener.onVideoFailed();
        }
    }

    @Override
    public void onAdVideoComplete() {
        if (mSlotListener != null) {
            mSlotListener.onVideoComplete();
        }
        mCustomVideoView.setIsRealPause(true);
    }

    //传递消息到 appcontext 层
    public interface SDKSlotListener {

        ViewGroup getAdParent();

        void onVideoLoadSuccess();

        void onVideoFailed();

        void onVideoComplete();
    }
}

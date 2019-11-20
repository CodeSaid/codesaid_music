package com.codesaid.video.videoplayer.core;

import android.view.ViewGroup;

import com.codesaid.video.videoplayer.VideoContextInterface;

/**
 * Created By codesaid
 * On :2019-11-18
 * Package Name: com.codesaid.video.videoplayer.core
 * desc: 与外界进行通信
 */
public class VideoAdContext implements VideoAdSlot.SDKSlotListener {

    private ViewGroup mParentView;

    private VideoAdSlot mAdSlot;
    private String mVideoUrl;

    private VideoContextInterface mListener;

    public VideoAdContext(ViewGroup parentView, String videoUrl) {
        this.mParentView = parentView;
        this.mVideoUrl = videoUrl;
        init();
    }

    /**
     * init the VideoView
     */
    private void init() {
        if (mVideoUrl != null) {
            mAdSlot = new VideoAdSlot(mVideoUrl, this);
        } else {
            mAdSlot = new VideoAdSlot(null, this);
            if (mListener != null) {
                mListener.onVideoFailed();
            }
        }
    }

    public void destroy() {
        mAdSlot.destroy();
    }

    public void setResultListener(VideoContextInterface listener) {
        this.mListener = listener;
    }

    @Override
    public ViewGroup getAdParent() {
        return mParentView;
    }

    @Override
    public void onVideoLoadSuccess() {
        if (mListener != null) {
            mListener.onVideoSuccess();
        }
    }

    @Override
    public void onVideoFailed() {
        if (mListener != null) {
            mListener.onVideoFailed();
        }
    }

    @Override
    public void onVideoComplete() {
        if (mListener != null) {
            mListener.onVideoComplete();
        }
    }
}

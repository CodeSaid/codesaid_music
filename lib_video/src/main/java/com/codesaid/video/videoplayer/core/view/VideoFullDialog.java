package com.codesaid.video.videoplayer.core.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import com.c.lib_video.R;
import com.codesaid.video.videoplayer.core.VideoAdSlot;
import com.codesaid.video.videoplayer.utils.Utils;

/**
 * Created By codesaid
 * On :2019-11-19
 * Package Name: com.codesaid.video.videoplayer.core.view
 * desc: 全屏显示视频 dialog
 */
public class VideoFullDialog extends Dialog implements CustomVideoView.ADVideoPlayerListener {

    private static final String TAG = VideoFullDialog.class.getSimpleName();
    private CustomVideoView mCustomVideoView;

    private RelativeLayout mRootView;
    private ViewGroup mParentView;

    private int position;
    private FullToSmallListener mListener;
    private boolean isFirst = true;
    //动画要执行的平移值
    private int deltaY;
    private VideoAdSlot.SDKSlotListener mSlotListener;
    private Bundle mStartBundle;
    private Bundle mEndBundle; //用于Dialog出入场动画

    public VideoFullDialog(Context context, CustomVideoView customVideoView,
                           String videoUrl, int position) {
        super(context, R.style.dialog_full_screen);
        this.position = position;
        this.mCustomVideoView = customVideoView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_video_layout);
        initVideoView();
    }

    public void setViewBundle(Bundle bundle) {
        mStartBundle = bundle;
    }

    public void setListener(FullToSmallListener listener) {
        this.mListener = listener;
    }

    public void setSlotListener(VideoAdSlot.SDKSlotListener slotListener) {
        this.mSlotListener = slotListener;
    }

    private void initVideoView() {
        mParentView = (RelativeLayout) findViewById(R.id.content_layout);
        mRootView = findViewById(R.id.root_view);
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickVideo();
            }
        });
        mRootView.setVisibility(View.INVISIBLE);

        mCustomVideoView.setListener(this);
        mCustomVideoView.mute(false);
        mParentView.addView(mCustomVideoView);
        mParentView.getViewTreeObserver()
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        mParentView.getViewTreeObserver().removeOnPreDrawListener(this);
                        prepareScene();
                        runEnterAnimation();
                        return true;
                    }
                });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        //防止第一次，有些手机仍显示全屏按钮
        mCustomVideoView.isShowFullBtn(false);

        if (!hasFocus) {
            position = mCustomVideoView.getCurrentPosition();
            mCustomVideoView.pauseForFullScreen();
        } else {
            if (isFirst) {
                //为了适配某些手机不执行seekandresume中的播放方法
                mCustomVideoView.seekAndResume(position);
            } else {
                mCustomVideoView.resume();
            }
        }
        isFirst = false;
    }

    @Override
    public void dismiss() {
        mParentView.removeView(mCustomVideoView);
        super.dismiss();
    }

    @Override
    public void onBackPressed() {
        onClickBackBtn();
        //super.onBackPressed(); 禁止掉返回键本身的关闭功能,转为自己的关闭效果
    }

    @Override
    public void onBufferUpdate(int time) {

    }

    @Override
    public void onClickFullScreenBtn() {
        onClickVideo();
    }

    @Override
    public void onClickVideo() {

    }

    @Override
    public void onClickBackBtn() {
        runExitAnimator();
    }

    @Override
    public void onClickPlay() {

    }

    @Override
    public void onAdVideoLoadSuccess() {
        if (mCustomVideoView != null) {
            mCustomVideoView.resume();
        }
    }

    @Override
    public void onAdVideoLoadFailed() {

    }

    @Override
    public void onAdVideoComplete() {
        dismiss();
        if (mCustomVideoView != null) {
            // 通知外部播放完成
            mListener.playComplete();
        }
    }

    /**
     * 准备动画所需数据
     */
    private void prepareScene() {
        mEndBundle = Utils.getViewProperty(mCustomVideoView);

        // 将desationview移到originalview位置处
        deltaY = (mStartBundle.getInt(Utils.PROPNAME_SCREENLOCATION_TOP) - mEndBundle.getInt(
                Utils.PROPNAME_SCREENLOCATION_TOP));
        mCustomVideoView.setTranslationY(deltaY);
    }

    /**
     * 入场动画
     */
    private void runEnterAnimation() {
        mCustomVideoView.animate()
                .setDuration(200)
                .setInterpolator(new LinearInterpolator())
                .translationY(0)
                .withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        mRootView.setVisibility(View.VISIBLE);
                    }
                })
                .start();
    }

    /**
     * 出场动画
     */
    private void runExitAnimator() {
        mCustomVideoView.animate()
                .setDuration(200)
                .setInterpolator(new LinearInterpolator())
                .translationY(deltaY)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                        if (mListener != null) {
                            mListener.getCurrentPlayPosition(mCustomVideoView.getCurrentPosition());
                        }
                    }
                })
                .start();
    }

    public interface FullToSmallListener {
        void getCurrentPlayPosition(int position);

        void playComplete();//全屏播放结束时回调
    }
}

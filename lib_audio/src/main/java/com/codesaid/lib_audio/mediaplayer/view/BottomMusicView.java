package com.codesaid.lib_audio.mediaplayer.view;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codesaid.lib_audio.R;
import com.codesaid.lib_audio.mediaplayer.core.AudioController;
import com.codesaid.lib_audio.mediaplayer.events.AudioLoadEvent;
import com.codesaid.lib_audio.mediaplayer.events.AudioPauseEvent;
import com.codesaid.lib_audio.mediaplayer.events.AudioProgressEvent;
import com.codesaid.lib_audio.mediaplayer.events.AudioStartEvent;
import com.codesaid.lib_audio.mediaplayer.model.AudioBean;
import com.codesaid.lib_image_loader.api.ImageLoaderManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created By codesaid
 * On :2019-11-07
 * Package Name: com.codesaid.lib_audio.mediaplayer.view
 * desc: 播放器底部 View
 */
public class BottomMusicView extends RelativeLayout {

    private Context mContext;

    /*
     * View
     */
    private ImageView mLeftView;
    private TextView mTitleView;
    private TextView mAlbumView;
    private ImageView mPlayView;
    private ImageView mRightView;
    /*
     * data
     */
    private AudioBean mAudioBean;

    public BottomMusicView(Context context) {
        this(context, null);
    }

    public BottomMusicView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomMusicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.bottom_view, this);
        rootView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳到音乐播放Activitity
                MusicPlayerActivity.start((Activity) mContext);
            }
        });
        mLeftView = rootView.findViewById(R.id.album_view);
        ObjectAnimator animator = ObjectAnimator.ofFloat(mLeftView, View.ROTATION.getName(), 0f, 360);
        animator.setDuration(10000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);
        animator.start();

        mTitleView = rootView.findViewById(R.id.audio_name_view);
        mAlbumView = rootView.findViewById(R.id.audio_album_view);
        mPlayView = rootView.findViewById(R.id.play_view);
        mPlayView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //处理播放暂停事件
                AudioController.getInstance().play();
            }
        });
        mRightView = rootView.findViewById(R.id.show_list_view);
        mRightView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示音乐列表对话框
                //MusicListDialog dialog = new MusicListDialog(mContext);
                //dialog.show();
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioLoadEvent(AudioLoadEvent event) {
        // 监听加载事件
        //更新当前 view 为 load 状态
        mAudioBean = event.mAudioBean;
        showLoadingView();
    }

    private void showLoadingView() {
        if (mAudioBean != null) {
            ImageLoaderManager.getInstance()
                    .displayImageForCircle(mLeftView, mAudioBean.albumPic);
            mTitleView.setText(mAudioBean.name);
            mAlbumView.setText(mAudioBean.album);
            mPlayView.setImageResource(R.mipmap.note_btn_pause_white);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioStartEvent(AudioStartEvent event) {
        //更新当前 view 为播放状态
        showPlayView();
    }

    private void showPlayView() {
        if (mAudioBean != null) {
            mPlayView.setImageResource(R.mipmap.note_btn_play_white);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioPauseEvent(AudioPauseEvent event) {
        //更新当前 view 为暂停状态
        showPauseView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioProgrssEvent(AudioProgressEvent event) {
        //更新当前view的播放进度
    }

    private void showPauseView() {
        if (mAudioBean != null) {
            mPlayView.setImageResource(R.mipmap.note_btn_pause_white);
        }
    }
}

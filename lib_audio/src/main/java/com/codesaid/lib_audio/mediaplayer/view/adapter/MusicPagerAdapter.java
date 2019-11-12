package com.codesaid.lib_audio.mediaplayer.view.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.codesaid.lib_audio.R;
import com.codesaid.lib_audio.mediaplayer.core.AudioController;
import com.codesaid.lib_audio.mediaplayer.model.AudioBean;
import com.codesaid.lib_image_loader.api.ImageLoaderManager;

import java.util.ArrayList;

/**
 * Created By codesaid
 * On :2019-11-12
 * Package Name: com.codesaid.lib_audio.mediaplayer.view.adapter
 */
public class MusicPagerAdapter extends PagerAdapter {

    private Context mContext;

    private ArrayList<AudioBean> mAudioBeans;
    private SparseArray<ObjectAnimator> mAnims = new SparseArray<>();

    public MusicPagerAdapter(Context context, ArrayList<AudioBean> audioBeans) {
        mContext = context;
        mAudioBeans = audioBeans;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.indictor_item_view, null);
        ImageView imageView = rootView.findViewById(R.id.circle_view);
        container.addView(rootView);
        ImageLoaderManager.getInstance()
                .displayImageForCircle(imageView, mAudioBeans.get(position).albumPic);
        //只在无动化时创建
        mAnims.put(position, createAnim(rootView));// 将动画缓存起来
        return rootView;
    }

    @Override
    public int getCount() {
        return mAudioBeans == null ? 0 : mAudioBeans.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    /**
     * 创建动画
     * @param view view
     * @return Animator
     */
    private ObjectAnimator createAnim(View view) {
        view.setRotation(0);
        ObjectAnimator animator = ObjectAnimator
                .ofFloat(view, View.ROTATION.getName(), 0, 360);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);
        if (AudioController.getInstance().isStartState()) {
            animator.start();
        }
        return animator;
    }

    public ObjectAnimator getAnim(int pos) {
        return mAnims.get(pos);
    }
}

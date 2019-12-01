package com.codesaid_music.view.discory.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.codesaid.lib_image_loader.api.ImageLoaderManager;

import java.util.ArrayList;

/**
 * Created By codesaid
 * On :2019-11-30
 * Package Name: com.codesaid_music.view.discory.adapter
 * desc:发现 Fragment ---->  RecyclerView 的 头部 View 的 Adapter
 */
public class BannerPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<String> mData;

    public BannerPagerAdapter(Context context, ArrayList<String> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView photoView = new ImageView(mContext);
        photoView.setScaleType(ImageView.ScaleType.FIT_XY);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(mContext, CourseDetailActivity.class);
                //mContext.startActivity(intent);
            }
        });

        ImageLoaderManager.getInstance().displayImageForView(photoView, mData.get(position));
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return photoView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}

package com.codesaid.lib_image_loader.api;

import android.annotation.SuppressLint;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.codesaid.lib_image_loader.R;

/**
 * Created By codesaid
 * On :2019-11-02
 * Package Name: com.codesaid.lib_image_loader.api
 * desc: 图片加载管理类，与外界的唯一通信入口
 * 支持为各种 View notification    appwidget   ViewGroup 加载图片
 */
public class ImageLoaderManager {

    private ImageLoaderManager() {

    }

    private static class SingletonHolder {
        private static ImageLoaderManager instance = new ImageLoaderManager();
    }

    public static ImageLoaderManager getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 为ImageView 加载图片
     *
     * @param imageView 需要加载图片的 ImageView
     * @param url       地址
     */
    public void displayImageForView(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .transition(BitmapTransitionOptions.withCrossFade())
                .into(imageView);
    }

    @SuppressLint("CheckResult")
    private RequestOptions initCommonRequestOption() {
        RequestOptions options = new RequestOptions();

        options.placeholder(R.mipmap.b4y) // 占位图
                .error(R.mipmap.b4y) // 错误图
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) // 缓存策略  Glide自己决定
                .skipMemoryCache(false) // 启动内存缓存
                .priority(Priority.NORMAL); // 图片下载优先级

        return options;
    }
}

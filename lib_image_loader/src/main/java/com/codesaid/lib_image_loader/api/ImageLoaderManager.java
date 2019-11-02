package com.codesaid.lib_image_loader.api;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.codesaid.lib_image_loader.R;
import com.codesaid.lib_image_loader.image.Utils;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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

    /**
     * 为 ImageView 加载圆形图片
     *
     * @param imageView 需要加载图片的 ImageView
     * @param url       地址
     */
    public void displayImageForCircle(final ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory
                                .create(imageView.getResources(), resource);
                        drawable.setCircular(true);
                        imageView.setImageDrawable(drawable);
                    }
                });
    }

    /**
     * 为 ViewGroup 加载图片设置为背景    并 判断是否需要模糊处理
     *
     * @param viewGroup 需要加载图片的 ViewGroup
     * @param url       地址
     * @param isBlur    是否需要模糊处理
     */
    public void displayImageForViewGroup(final ViewGroup viewGroup, String url, final boolean isBlur) {
        Glide.with(viewGroup.getContext())
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .into(new SimpleTarget<Bitmap>() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Observable.just(resource)
                                .map(new Function<Bitmap, Drawable>() {
                                    @Override
                                    public Drawable apply(Bitmap bitmap) throws Exception {
                                        Drawable drawable;
                                        if (isBlur) { // 是否需要模糊处理
                                            drawable = new BitmapDrawable(Utils
                                                    .doBlur(bitmap, 100, true));
                                        } else {
                                            drawable = new BitmapDrawable(bitmap);
                                        }

                                        return drawable;
                                    }
                                }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Drawable>() {
                                    @Override
                                    public void accept(Drawable drawable) throws Exception {
                                        viewGroup.setBackground(drawable);
                                    }
                                });
                    }
                });
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

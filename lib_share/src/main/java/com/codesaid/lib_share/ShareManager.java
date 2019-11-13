package com.codesaid.lib_share;

import android.content.Context;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created By codesaid
 * On :2019-11-13
 * Package Name: com.codesaid.lib_share
 * desc : 分享功能单例管理类
 */
public class ShareManager implements PlatformActionListener {

    /**
     * @author 应用程序需要的平台
     */
    public enum PlatformType {
        QQ, QZone, WeChat, WechatMoments
    }

    private static ShareManager mInstance;

    private PlatformShareListener mListener;

    /**
     * 要分享的平台
     */
    private Platform mPlatform;

    private ShareManager() {

    }

    /**
     * 初始化 ShareSDK
     *
     * @param context context
     */
    public void init(Context context) {
        ShareSDK.initSDK(context);
    }

    public void ShareData(ShareData shareData, PlatformShareListener listener) {
        mListener = listener;
        switch (shareData.mPlatofrmType) {
            case QQ:
                mPlatform = ShareSDK.getPlatform(QQ.NAME);
                break;
            case QZone:
                mPlatform = ShareSDK.getPlatform(QZone.NAME);
                break;
            case WeChat:
                mPlatform = ShareSDK.getPlatform(Wechat.NAME);
                break;
            case WechatMoments:
                mPlatform = ShareSDK.getPlatform(WechatMoments.NAME);
                break;

        }
        mPlatform.setPlatformActionListener(this);
        mPlatform.share(shareData.mShareParams);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (mListener != null) {
            mListener.onComplete(i, hashMap);
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        if (mListener != null) {
            mListener.onError(i, throwable);
        }
    }

    @Override
    public void onCancel(Platform platform, int i) {
        if (mListener != null) {
            mListener.onCancel(i);
        }
    }

    /**
     * 自己包装一层 PlatFormActionListener
     */
    public interface PlatformShareListener {
        void onComplete(int var2, HashMap<String, Object> var3);

        void onError(int var2, Throwable var3);

        void onCancel(int var2);
    }

    public static ShareManager getInstance() {
        if (mInstance == null) {
            synchronized (ShareManager.class) {
                if (mInstance == null) {
                    mInstance = new ShareManager();
                }
            }
        }
        return mInstance;
    }
}

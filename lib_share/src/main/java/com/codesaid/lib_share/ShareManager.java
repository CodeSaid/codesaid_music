package com.codesaid.lib_share;

import android.content.Context;

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
public class ShareManager {

    /**
     * @author 应用程序需要的平台
     */
    public enum PlatformType {
        QQ, QZone, WeChat, WechatMoments
    }

    private static ShareManager mInstance;

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
    public static void init(Context context) {
        ShareSDK.initSDK(context);
    }

    void shareData(ShareData shareData, PlatformActionListener listener) {
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
        mPlatform.setPlatformActionListener(listener);
        mPlatform.share(shareData.mShareParams);
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

package com.codesaid.lib_share;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.codesaid.lib_share.bean.Share;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * Created By codesaid
 * On :2019-11-13
 * Package Name: com.codesaid.lib_share
 */
public class ShareDialog extends Dialog {

    private Context mContext;
    private DisplayMetrics dm;

    /**
     * UI
     */
    private RelativeLayout mWeixinLayout;
    private RelativeLayout mWeixinMomentLayout;
    private RelativeLayout mQQLayout;
    private RelativeLayout mQZoneLayout;

    private Share share;

    public ShareDialog(Context context, Share share) {
        super(context, R.style.SheetDialogStyle);
        mContext = context;
        this.share = share;
        dm = mContext.getResources().getDisplayMetrics();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share_layout);
        initView();
    }

    private void initView() {
        /**
         * 通过获取到dialog的window来控制dialog的宽高及位置
         */
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = dm.widthPixels; //设置宽度
        dialogWindow.setAttributes(lp);
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);

        mWeixinLayout = findViewById(R.id.weixin_layout);
        mWeixinLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareData(ShareManager.PlatformType.WeChat);
            }
        });
        mWeixinMomentLayout = findViewById(R.id.moment_layout);
        mWeixinMomentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareData(ShareManager.PlatformType.WechatMoments);
            }
        });
        mQQLayout = findViewById(R.id.qq_layout);
        mQQLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareData(ShareManager.PlatformType.QQ);
            }
        });
        mQZoneLayout = findViewById(R.id.qzone_layout);
        mQZoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareData(ShareManager.PlatformType.QZone);
            }
        });
    }

    /**
     * 处理分享结果
     */
    private PlatformActionListener mListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(Platform platform, int i) {

        }
    };


    private void shareData(ShareManager.PlatformType platform) {
        ShareData mData = new ShareData();
        Platform.ShareParams params = new Platform.ShareParams();
        params.setShareType(share.getShareType());
        params.setTitle(share.getShareTitle());
        params.setTitleUrl(share.getShareTitleUrl());
        params.setSite(share.getShareSite());
        params.setSiteUrl(share.getShareSiteUrl());
        params.setText(share.getShareText());
        params.setImagePath(share.getSharePhoto());
        params.setUrl(share.getUrl());
        mData.mPlatofrmType = platform;
        mData.mShareParams = params;
        ShareManager.getInstance().shareData(mData, mListener);
    }
}

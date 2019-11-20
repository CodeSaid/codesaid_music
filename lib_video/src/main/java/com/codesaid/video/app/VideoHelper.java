package com.codesaid.video.app;

import android.content.Context;

/**
 * Created By codesaid
 * On :2019-11-20
 * Package Name: com.codesaid.video.app
 * desc : 用来为调用者创建视频
 */
public final class VideoHelper {
    public static Context mContext;

    public static void init(Context context) {
        mContext = context;
        //初始化SDK的时候，初始化Realm数据库
    }

    public static Context getContext() {
        return mContext;
    }
}

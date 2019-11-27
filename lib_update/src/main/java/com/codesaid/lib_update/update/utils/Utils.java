package com.codesaid.lib_update.update.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created By codesaid
 * On :2019-11-27
 * Package Name: com.codesaid.lib_update.update.constant
 * desc : 工具类
 */

public class Utils {

    /**
     * 获取版本号
     *
     * @param context context
     * @return 版本号
     */
    public static int getVersionCode(Context context) {
        int versionCode = 1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}



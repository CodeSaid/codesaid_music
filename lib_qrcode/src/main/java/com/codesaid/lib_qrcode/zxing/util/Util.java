package com.codesaid.lib_qrcode.zxing.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;

public class Util {

  public static Activity currentActivity = null;

  /**
   * 获得屏幕宽度
   */
  @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2) public static int getWindowWidthPix() {
    int ver = Build.VERSION.SDK_INT;
    Display display = currentActivity.getWindowManager().getDefaultDisplay();
    int width = 0;
    if (ver < 13) {
      DisplayMetrics dm = new DisplayMetrics();
      display.getMetrics(dm);
      width = dm.widthPixels;
    } else {
      Point point = new Point();
      display.getSize(point);
      width = point.x;
    }
    return width;
  }

  /**
   * 获得屏幕高度
   */
  @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2) public static int getWindowHeightPix() {
    int ver = Build.VERSION.SDK_INT;
    Display display = currentActivity.getWindowManager().getDefaultDisplay();
    int height = 0;
    if (ver < 13) {
      DisplayMetrics dm = new DisplayMetrics();
      display.getMetrics(dm);
      height = dm.heightPixels;
    } else {
      Point point = new Point();
      display.getSize(point);
      height = point.y;
    }
    return height;
  }

    @SuppressLint("MissingPermission")
  public static String getIMEI(Context context) {
    try {
      TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
      String imeiCode = tm.getDeviceId();
      return imeiCode;
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  public static int dip2px(Context context, float dpValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale);
  }

  public static int px2dip(Context context, float pxValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale);
  }
}

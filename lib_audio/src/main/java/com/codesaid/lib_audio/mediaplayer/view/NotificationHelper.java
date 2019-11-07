package com.codesaid.lib_audio.mediaplayer.view;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.codesaid.lib_audio.R;
import com.codesaid.lib_audio.app.AudioHelper;
import com.codesaid.lib_audio.mediaplayer.core.AudioController;
import com.codesaid.lib_audio.mediaplayer.core.MusicService;
import com.codesaid.lib_audio.mediaplayer.model.AudioBean;

/**
 * Created By codesaid
 * On :2019-11-07
 * Package Name: com.codesaid.lib_audio.mediaplayer.view
 * desc: 音乐 Notification 帮助类
 * 1.完成 Notification 的创建和初始化
 * 2.对外提供表更新 Notification 的方法
 */
public class NotificationHelper {

    public static String TAG = "NotificationHelpwe";

    public static final String CHANNEL_ID = "channel_id_audio";
    public static final String CHANNEL_NAME = "channel_name_audio";
    public static final int NOTIFICATION_ID = 0x111;

    //最终的Notification显示类
    private Notification mNotification;
    private RemoteViews mRemoteViews; // 大布局
    private RemoteViews mSmallRemoteViews; //小布局
    private NotificationManager mNotificationManager;
    private NotificationHelperListener mListener;
    private String packageName;
    //当前要播的歌曲Bean
    private AudioBean mAudioBean;

    private static NotificationHelper mInstance;

    private NotificationHelper() {

    }

    public void init(NotificationHelperListener listener) {
        mNotificationManager = (NotificationManager) AudioHelper
                .getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        packageName = AudioHelper.getContext().getPackageName();
        mAudioBean = AudioController.getInstance().getNowPlaying();
        initNotification();

        mListener = listener;
        if (mListener != null) {
            mListener.onNotificationInit();
        }
    }

    /**
     * 创建 Notification
     */
    private void initNotification() {
        if (mNotification == null) {
            // 创建布局
            initRemoteViews();

            //再构建Notification
            //            Intent intent = new Intent(AudioHelper.getContext(), MusicPlayerActivity.class);
            //            PendingIntent pendingIntent = PendingIntent.getActivity(AudioHelper.getContext(), 0, intent,
            //                    PendingIntent.FLAG_UPDATE_CURRENT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel =
                        new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                channel.enableLights(false);
                channel.enableVibration(false);
                mNotificationManager.createNotificationChannel(channel);
            }
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(AudioHelper.getContext(), CHANNEL_ID)
                            //.setContentIntent(pendingIntent)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setCustomBigContentView(mRemoteViews) //大布局
                            .setContent(mSmallRemoteViews); //正常布局，两个布局可以切换
            mNotification = builder.build();

            //            showLoadStatus(mAudioBean);
        }
    }

    /**
     * 初始化布局
     */
    private void initRemoteViews() {
        int layoutId = R.layout.notification_big_layout;
        mRemoteViews = new RemoteViews(packageName, layoutId);
        mRemoteViews.setTextViewText(R.id.title_view, mAudioBean.name);
        mRemoteViews.setTextViewText(R.id.tip_view, mAudioBean.album);

        int smallLayoutId = R.layout.notification_small_layout;
        mSmallRemoteViews = new RemoteViews(packageName, smallLayoutId);
        mSmallRemoteViews.setTextViewText(R.id.title_view, mAudioBean.name);
        mSmallRemoteViews.setTextViewText(R.id.tip_view, mAudioBean.album);

        // 点击播放按钮要发送的广播
        Intent playIntent = new Intent(MusicService.NotificationReceiver.ACTION_STATUS_BAR);
        playIntent.putExtra(MusicService.NotificationReceiver.EXTRA,
                MusicService.NotificationReceiver.EXTRA_PLAY);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(AudioHelper.getContext(),
                1, playIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.play_view, playPendingIntent);
        mSmallRemoteViews.setOnClickPendingIntent(R.id.play_view, playPendingIntent);


        // 点击上一首按钮需要发送的广播
        Intent previousIntent = new Intent(MusicService.NotificationReceiver.ACTION_STATUS_BAR);
        playIntent.putExtra(MusicService.NotificationReceiver.EXTRA,
                MusicService.NotificationReceiver.EXTRA_PRE);
        PendingIntent previousPendingIntent = PendingIntent.getBroadcast(AudioHelper.getContext(),
                2, previousIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.previous_view, previousPendingIntent);

        // 点击下一首按钮需要发送的广播
        Intent nextIntent = new Intent(MusicService.NotificationReceiver.ACTION_STATUS_BAR);
        playIntent.putExtra(MusicService.NotificationReceiver.EXTRA,
                MusicService.NotificationReceiver.EXTRA_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(AudioHelper.getContext(),
                3, nextIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.next_view, nextPendingIntent);
        mSmallRemoteViews.setOnClickPendingIntent(R.id.next_view, nextPendingIntent);

        // 点击收藏按钮需要发送的广播
        Intent favoriteIntent = new Intent(MusicService.NotificationReceiver.ACTION_STATUS_BAR);
        playIntent.putExtra(MusicService.NotificationReceiver.EXTRA,
                MusicService.NotificationReceiver.EXTRA_FAV);
        PendingIntent favoritePendingIntent = PendingIntent.getBroadcast(AudioHelper.getContext(),
                4, favoriteIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.favourite_view, favoritePendingIntent);
    }

    public static NotificationHelper getInstance() {
        if (mInstance == null) {
            synchronized (NotificationHelper.class) {
                if (mInstance != null) {
                    mInstance = new NotificationHelper();
                }
            }
        }
        return mInstance;
    }

    public Notification getNotification() {
        return mNotification;
    }

    /**
     * 与音乐service的回调通信
     */
    public interface NotificationHelperListener {
        void onNotificationInit();
    }
}

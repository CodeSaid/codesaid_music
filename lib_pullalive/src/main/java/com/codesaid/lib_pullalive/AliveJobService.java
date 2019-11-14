package com.codesaid.lib_pullalive;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * Created By codesaid
 * On :2019-11-14
 * Package Name: com.codesaid.lib_pullalive
 * desc: 一个轻量级的 job service 。利用空闲时间执行一些小事情，提高进程不被回收的概率
 */

@TargetApi(value = Build.VERSION_CODES.LOLLIPOP)
public class AliveJobService extends JobService {

    private static final String TAG = AliveJobService.class.getName();

    private JobScheduler mJobScheduler;

    private static final int PULL_ALIVE = 0x01;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PULL_ALIVE:
                    Log.e(TAG, "pull alive");
                    jobFinished((JobParameters) msg.obj, true);
                    break;
                case 0x02:
                    break;
            }
        }
    };

    public static void start(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(context, AliveJobService.class);
            context.startService(intent);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        JobInfo jobInfo = initJobInfo(startId);
        if (mJobScheduler.schedule(jobInfo) <= 0) {
            Log.d(TAG, "AliveJobService failed");
        } else {
            Log.d(TAG, "AliveJobService success");
        }

        return START_STICKY;
    }

    private JobInfo initJobInfo(int startId) {
        JobInfo.Builder builder = new JobInfo.Builder(startId,
                new ComponentName(getPackageName(), AliveJobService.class.getName()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setMinimumLatency(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS); //执行的最小延迟时间
            builder.setOverrideDeadline(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);  //执行的最长延时时间
            builder.setBackoffCriteria(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS,
                    JobInfo.BACKOFF_POLICY_LINEAR);//线性重试方案
        } else {
            builder.setPeriodic(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);
        }
        builder.setPersisted(false);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE);
        builder.setRequiresCharging(false);
        return builder.build();
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        mHandler.sendMessage(Message.obtain(mHandler, PULL_ALIVE, params));
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mHandler.removeCallbacksAndMessages(null);
        return false;
    }
}

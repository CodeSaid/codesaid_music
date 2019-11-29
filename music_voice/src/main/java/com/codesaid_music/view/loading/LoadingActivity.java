package com.codesaid_music.view.loading;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.codesaid.lib_commin_ui.base.BaseActivity;
import com.codesaid.lib_commin_ui.base.constant.Constant;
import com.codesaid.lib_pullalive.AliveJobService;
import com.codesaid_music.R;
import com.codesaid_music.view.home.HomeActivity;

/**
 * Created By codesaid
 * On :2019-11-29
 * Package Name: com.codesaid_music.view.loading
 */
public class LoadingActivity extends BaseActivity {

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            startActivity(new Intent(LoadingActivity.this, HomeActivity.class));
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        avoidLauncherAgain();
        setContentView(R.layout.activity_loading_layout);
        pullAliveService();

        if (hasPermission(Constant.WRITE_READ_EXTERNAL_PERMISSION)) {
            doSDCardPermission();
        } else {
            requestPermission(Constant.WRITE_READ_EXTERNAL_CODE, Constant.WRITE_READ_EXTERNAL_PERMISSION);
        }
    }

    /**
     * 避免从桌面启动程序后，会重新实例化入口类的activity
     */
    private void avoidLauncherAgain() {
        // 判断当前activity是不是所在任务栈的根
        if (!this.isTaskRoot()) {
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                        && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                }
            }
        }
    }

    /**
     * 启动 APP 保活 Service
     */
    private void pullAliveService() {
        AliveJobService.start(this);
    }

    @Override
    public void doSDCardPermission() {
        mHandler.sendEmptyMessageDelayed(0, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}

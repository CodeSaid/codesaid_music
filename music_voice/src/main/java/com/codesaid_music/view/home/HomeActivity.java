package com.codesaid_music.view.home;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.codesaid.lib_audio.app.AudioHelper;
import com.codesaid.lib_audio.mediaplayer.model.AudioBean;
import com.codesaid.lib_commin_ui.base.BaseActivity;
import com.codesaid.lib_image_loader.api.ImageLoaderManager;
import com.codesaid.lib_update.app.UpdateHelper;
import com.codesaid_music.R;
import com.codesaid_music.constant.Constant;
import com.codesaid_music.model.CHANNEL;
import com.codesaid_music.model.login.LoginEvent;
import com.codesaid_music.utils.UserManager;
import com.codesaid_music.utils.Utils;
import com.codesaid_music.view.home.adapter.HomePagerAdapter;
import com.codesaid_music.view.login.LoginActivity;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Created By codesaid
 * On :2019-10-31
 * Package Name: com.codesaid_music
 * desc: 首页
 *
 * @author codesaid
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {

    // 指定首页要出现的卡片
    protected static final CHANNEL[] CHANNELS = new
            CHANNEL[]{CHANNEL.MY, CHANNEL.DISCORY, CHANNEL.FRIEND};

    /**
     * View
     */
    private DrawerLayout mDrawerLayout;
    private TextView mToggleView;
    private TextView mSearchView;
    private ViewPager mViewPager;

    private LinearLayout mUnLoginLayout;
    private ImageView mPhotoView;

    private HomePagerAdapter mHomePagerAdapter;

    // 相机
    private View mDrawerQrCodeView;

    // 分享
    private View mDrawerShareView;

    /*
     * data
     */
    private ArrayList<AudioBean> mLists = new ArrayList<>();

    private UpdateReceiver mReceiver = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        registerBroadcastReceiver();
        setContentView(R.layout.activity_home_layout);
        initView();
        initData();
    }

    private void initView() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggleView = findViewById(R.id.toggle_view);
        mSearchView = findViewById(R.id.search_view);
        mViewPager = findViewById(R.id.view_pager);

        mHomePagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), CHANNELS);
        mViewPager.setAdapter(mHomePagerAdapter);

        mToggleView.setOnClickListener(this);
        mSearchView.setOnClickListener(this);

        initMagicIndicator();


        mDrawerQrCodeView = findViewById(R.id.home_qrcode);
        mDrawerQrCodeView.setOnClickListener(this);
        mDrawerShareView = findViewById(R.id.home_music);
        mDrawerShareView.setOnClickListener(this);
        findViewById(R.id.online_music_view).setOnClickListener(this);
        findViewById(R.id.check_update_view).setOnClickListener(this);

        // 登录相关的 UI
        mUnLoginLayout = findViewById(R.id.unloggin_layout);
        mUnLoginLayout.setOnClickListener(this);
        mPhotoView = findViewById(R.id.avatr_view);

    }

    /**
     * 初始化 ViewPager 的指示器
     */
    private void initMagicIndicator() {
        MagicIndicator magicIndicator = findViewById(R.id.magic_indicator);
        magicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public int getCount() {
                return CHANNELS == null ? 0 : CHANNELS.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new
                        SimplePagerTitleView(context);
                simplePagerTitleView.setText(CHANNELS[index].getKey());
                simplePagerTitleView.setTextSize(20);
                simplePagerTitleView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                simplePagerTitleView.setNormalColor(Color.parseColor("#999999"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#333333"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public float getTitleWeight(Context context, int index) {
                return 1.0f;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    private void initData() {
        mLists.add(new AudioBean("100001", "http://sp-sycdn.kuwo.cn/resource/n2/85/58/433900159.mp3",
                "以你的名字喊我", "周杰伦", "七里香", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698076304&di=e6e99aa943b72ef57b97f0be3e0d2446&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fblog%2F201401%2F04%2F20140104170315_XdG38.jpeg",
                "4:30"));
        mLists.add(
                new AudioBean("100002", "http://sq-sycdn.kuwo.cn/resource/n1/98/51/3777061809.mp3", "勇气",
                        "梁静茹", "勇气", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典",
                        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698193627&di=711751f16fefddbf4cbf71da7d8e6d66&imgtype=jpg&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D213168965%2C1040740194%26fm%3D214%26gp%3D0.jpg",
                        "4:40"));
        mLists.add(
                new AudioBean("100003", "http://sp-sycdn.kuwo.cn/resource/n2/52/80/2933081485.mp3", "灿烂如你",
                        "汪峰", "春天里", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典",
                        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698239736&di=3433a1d95c589e31a36dd7b4c176d13a&imgtype=0&src=http%3A%2F%2Fpic.zdface.com%2Fupload%2F201051814737725.jpg",
                        "3:20"));
        mLists.add(
                new AudioBean("100004", "http://sr-sycdn.kuwo.cn/resource/n2/33/25/2629654819.mp3", "小情歌",
                        "五月天", "小幸运", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典",
                        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698289780&di=5146d48002250bf38acfb4c9b4bb6e4e&imgtype=0&src=http%3A%2F%2Fpic.baike.soso.com%2Fp%2F20131220%2Fbki-20131220170401-1254350944.jpg",
                        "2:45"));

        AudioHelper.startMusicService(mLists);
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    @SuppressLint("RtlHardcoded")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.unloggin_layout:
                // 判断当前是否已经登录
                if (!UserManager.getInstance().hasLogined()) {
                    // 未登录，直接跳转到登录页面
                    LoginActivity.start(this);
                } else {
                    // 已经登录 关闭侧滑栏，并且刷新主页内容区
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
                break;
            case R.id.toggle_view:
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.home_qrcode:
                if (hasPermission(Constant.HARDWEAR_CAMERA_PERMISSION)) {
                    doCameraPermission();
                } else {
                    requestPermission(Constant.HARDWEAR_CAMERA_CODE, Constant.HARDWEAR_CAMERA_PERMISSION);
                }
                break;
            case R.id.home_music:
                //shareFriend();
                goToMusic();
                break;
            case R.id.online_music_view:
                //跳到指定webactivity
                gotoWebView("https://www.codesaid.com");
                break;
            case R.id.check_update_view:
                checkUpdate();
                break;
        }
    }

    @Override
    public void doCameraPermission() {
        ARouter.getInstance().build(Constant.Router.ROUTER_CAPTURE_ACTIVIYT).navigation();
    }

    private void goToMusic() {
        ARouter.getInstance().build(Constant.Router.ROUTER_MUSIC_ACTIVIYT).navigation();
    }

    private void gotoWebView(String url) {
        ARouter.getInstance()
                .build(Constant.Router.ROUTER_WEB_ACTIVIYT)
                .withString("url", url)
                .navigation();
    }

    private void checkUpdate() {
        UpdateHelper.checkUpdate(this);
    }

    private void registerBroadcastReceiver() {
        if (mReceiver == null) {
            mReceiver = new UpdateReceiver();
            LocalBroadcastManager
                    .getInstance(this)
                    .registerReceiver(mReceiver, new IntentFilter(UpdateHelper.UPDATE_ACTION));
        }
    }

    private void unRegisterBroadcastReceiver() {
        if (mReceiver != null) {
            LocalBroadcastManager
                    .getInstance(this)
                    .unregisterReceiver(mReceiver);
        }
    }

    /**
     * 接收Update发送的广播
     */
    public class UpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            context.startActivity(Utils
                    .getInstallApkIntent(context,
                            intent.getStringExtra(UpdateHelper.UPDATE_FILE_KEY)));
        }
    }

    /**
     * 处理登陆事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent event) {
        mUnLoginLayout.setVisibility(View.GONE);
        mPhotoView.setVisibility(View.VISIBLE);
        ImageLoaderManager.getInstance()
                .displayImageForCircle(mPhotoView, UserManager.getInstance().getUser().photoUrl);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //退出不销毁task中activity
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unRegisterBroadcastReceiver();
    }
}

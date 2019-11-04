package com.codesaid_music.view.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codesaid.lib_commin_ui.base.BaseActivity;
import com.codesaid.lib_image_loader.api.ImageLoaderManager;
import com.codesaid_music.R;
import com.codesaid_music.model.login.LoginEvent;
import com.codesaid_music.utils.UserManager;
import com.codesaid_music.view.home.adapter.HomePagerAdapter;
import com.codesaid_music.model.CHANNEL;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

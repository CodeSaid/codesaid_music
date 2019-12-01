package com.codesaid_music.view.discory;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.codesaid.lib_commin_ui.banner.AutoScrollViewPager;
import com.codesaid.lib_commin_ui.pager_indictor.CirclePageIndicator;
import com.codesaid_music.R;
import com.codesaid_music.model.discory.RecommendHeadValue;
import com.codesaid_music.view.discory.adapter.BannerPagerAdapter;

/**
 * Created By codesaid
 * On :2019-11-30
 * Package Name: com.codesaid_music.view.discory
 * desc : 发现 Fragment ---->  RecyclerView 的 头部 View
 */
public class DiscoverBannerView extends RelativeLayout {
    private Context mContext;

    /**
     * UI
     */
    private AutoScrollViewPager mViewPager;
    private BannerPagerAdapter mAdapter;
    private CirclePageIndicator mPagerIndictor;
    /**
     * Data
     */
    private RecommendHeadValue mHeaderValue;

    public DiscoverBannerView(Context context, RecommendHeadValue headerValue) {
        this(context, null, headerValue);
    }

    public DiscoverBannerView(Context context, AttributeSet attrs, RecommendHeadValue headerValue) {
        super(context, attrs);
        mContext = context;
        mHeaderValue = headerValue;
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater
                .from(mContext).inflate(R.layout.discover_header_banner_layout, this);
        mViewPager = rootView.findViewById(R.id.view_pager);
        mPagerIndictor = rootView.findViewById(R.id.pager_indictor_view);

        mAdapter = new BannerPagerAdapter(mContext, mHeaderValue.ads);
        mViewPager.setAdapter(mAdapter);
        mViewPager.startAutoScroll(3000);
        mViewPager.setInterval(3000);
        mPagerIndictor.setViewPager(mViewPager);
    }
}

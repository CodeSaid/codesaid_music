package com.codesaid_music.view.discory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.codesaid.lib_commin_ui.recyclerview.CommonAdapter;
import com.codesaid.lib_commin_ui.recyclerview.base.ViewHolder;
import com.codesaid.lib_commin_ui.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.codesaid.lib_commin_ui.recyclerview.wrapper.LoadMoreWrapper;
import com.codesaid.lib_image_loader.api.ImageLoaderManager;
import com.codesaid.lib_network.okhttp.listener.DisposeDataListener;
import com.codesaid.lib_network.okhttp.utils.ResponseEntityToModule;
import com.codesaid_music.R;
import com.codesaid_music.api.MockData;
import com.codesaid_music.api.RequestCenter;
import com.codesaid_music.model.discory.BaseRecommendModel;
import com.codesaid_music.model.discory.BaseRecommendMoreModel;
import com.codesaid_music.model.discory.RecommendBodyValue;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页发现fragment
 */
public class DiscoverFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, LoadMoreWrapper.OnLoadMoreListener {

    private Context mContext;

    /*
     *  UI
     */
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private CommonAdapter mAdapter;
    private HeaderAndFooterWrapper mHeaderWrapper;
    private LoadMoreWrapper mLoadMoreWrapper;

    /*
     * Data
     */
    private BaseRecommendModel mRecommandData;
    private List<RecommendBodyValue> mDatas = new ArrayList<>();

    public static Fragment newInstance() {
        return new DiscoverFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams")
        View rootView = inflater.inflate(R.layout.fragment_discory_layout, null);
        mSwipeRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_red_light));
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //发请求更新UI
        requestData();
    }

    /**
     * 请求网络数据
     */
    private void requestData() {
        RequestCenter.getRecommendData(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                mRecommandData = (BaseRecommendModel) responseObj;
                // 更新页面
                updateUI();
            }

            @Override
            public void onFailure(Object reasonObj) {
                onSuccess(ResponseEntityToModule.parseJsonToModule(MockData.HOME_DATA,
                        BaseRecommendModel.class));
            }
        });
    }

    /**
     * 加载更多数据
     */
    private void loadMore() {
        RequestCenter.getRecommendMoreData(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                BaseRecommendMoreModel moreData = (BaseRecommendMoreModel) responseObj;
                // 追加数据到 Adapter 中
                mDatas.addAll(moreData.data.list);
                mLoadMoreWrapper.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {
                //显示请求失败View,显示mock数据
                onSuccess(ResponseEntityToModule.parseJsonToModule(MockData.HOME_MORE_DATA,
                        BaseRecommendMoreModel.class));
            }
        });
    }

    /**
     * 更新页面
     */
    private void updateUI() {
        // 停止刷新
        mSwipeRefreshLayout.setRefreshing(false);
        mDatas = mRecommandData.data.list;
        mAdapter = new CommonAdapter<RecommendBodyValue>(mContext,
                R.layout.item_discory_list_picture_layout, mDatas) {
            @Override
            protected void convert(ViewHolder holder, RecommendBodyValue recommendBodyValue, int position) {
                TextView title = holder.getView(R.id.title_view);
                if (TextUtils.isEmpty(recommendBodyValue.title)) {
                    title.setVisibility(View.GONE);
                } else {
                    title.setVisibility(View.VISIBLE);
                    title.setText(recommendBodyValue.title);
                }
                holder.setText(R.id.name_view, recommendBodyValue.text);
                holder.setText(R.id.play_view, recommendBodyValue.play);
                holder.setText(R.id.time_view, recommendBodyValue.time);
                holder.setText(R.id.zan_view, recommendBodyValue.zan);
                holder.setText(R.id.message_view, recommendBodyValue.msg);
                ImageView logo = holder.getView(R.id.login_view);
                ImageLoaderManager.getInstance().displayImageForView(logo, recommendBodyValue.logo);
                ImageView avatar = holder.getView(R.id.avatr_view);
                ImageLoaderManager.getInstance().displayImageForCircle(avatar, recommendBodyValue.avatr);
            }
        };

        //头部view初始化
        mHeaderWrapper = new HeaderAndFooterWrapper(mAdapter);
        DiscoverBannerView bannerView = new DiscoverBannerView(mContext, mRecommandData.data.head);
        mHeaderWrapper.addHeaderView(bannerView);

    }

    @Override
    public void onRefresh() {
        // 下拉刷新
        requestData();
    }

    @Override
    public void onLoadMoreRequested() {
        loadMore();
    }
}

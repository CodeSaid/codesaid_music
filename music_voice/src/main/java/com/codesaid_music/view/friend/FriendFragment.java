package com.codesaid_music.view.friend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codesaid.lib_commin_ui.recyclerview.wrapper.LoadMoreWrapper;
import com.codesaid.lib_network.okhttp.listener.DisposeDataListener;
import com.codesaid_music.R;
import com.codesaid_music.api.RequestCenter;
import com.codesaid_music.model.friend.FriendBodyValue;
import com.codesaid_music.model.friend.FriendModel;
import com.codesaid_music.view.friend.adapter.FriendAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By codesaid
 * On :2019-10-31
 * Package Name: com.codesaid_music.view.friend
 * desc: 首页 Friend Fragment
 */
public class FriendFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, LoadMoreWrapper.OnLoadMoreListener {

    private Context mContext;

    /**
     * UI
     */
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private FriendAdapter mAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;

    /**
     * data
     */
    private FriendModel mRecommandData;
    private List<FriendBodyValue> mDatas = new ArrayList<>();

    public static Fragment newInstance() {
        return new FriendFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_friend_layout, null);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_red_light));
        // 设置滑动监听事件
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

    @Override
    public void onRefresh() {

    }

    /**
     * 请求数据
     */
    private void requestData() {
        RequestCenter.getFriendData(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                mRecommandData = (FriendModel) responseObj;
                updateUI();
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    /**
     * 更新界面显示
     */
    private void updateUI() {
        mSwipeRefreshLayout.setRefreshing(false);
        mDatas = mRecommandData.list;
        mAdapter = new FriendAdapter(mContext, mDatas);

        mLoadMoreWrapper = new LoadMoreWrapper(mAdapter);
        mLoadMoreWrapper.setLoadMoreView(R.layout.default_loading);
        mLoadMoreWrapper.setOnLoadMoreListener(this);

        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onLoadMoreRequested() {
        loadMore();
    }

    /**
     * 加载更多数据
     */
    private void loadMore() {
        RequestCenter.getFriendData(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                // 显示数据
                FriendModel moreData = (FriendModel) responseObj;
                // 追加数据到 adapter 中
                mDatas.addAll(moreData.list);
                mLoadMoreWrapper.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }
}

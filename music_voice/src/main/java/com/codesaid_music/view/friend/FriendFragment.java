package com.codesaid_music.view.friend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import androidx.core.app.Fragment;
import androidx.core.widget.SwipeRefreshLayout;
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
import com.codesaid_music.view.friend.adapter.FriendRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created By codesaid
 * On :2019-10-31
 * Package Name: com.codesaid_music.view.friend
 * desc: 首页 Friend Fragment
 */

public class FriendFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, LoadMoreWrapper.OnLoadMoreListener {

    private Context mContext;
    /*
     * UI
     */
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private FriendRecyclerAdapter mAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    /*
     * data
     */
    private FriendModel mRecommandData;
    private List<FriendBodyValue> mDatas = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            updateView();
        }
    };

    public static Fragment newInstance() {
        FriendFragment fragment = new FriendFragment();
        return fragment;
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
        View rootView = inflater.inflate(R.layout.fragment_friend_layout, null);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout);
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

    //下拉刷新接口
    @Override
    public void onRefresh() {
        requestData();
    }

    //加载更多接口
    @Override
    public void onLoadMoreRequested() {
        loadMore();
    }

    private void loadMore() {
        RequestCenter.getFriendData(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                FriendModel moreData = (FriendModel) responseObj;
                //追加数据到adapter中
                mDatas.addAll(moreData.list);
                mLoadMoreWrapper.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {
                //显示请求失败View,显示mock数据
                //                onSuccess(
                //                        ResponseEntityToModule.parseJsonToModule(MockData.FRIEND_DATA, BaseFriendModel.class));
            }
        });
    }

    //请求数据
    private void requestData() {
        RequestCenter.getFriendData(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                mRecommandData = (FriendModel) responseObj;
                //更新UI
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void onFailure(Object reasonObj) {
                //显示请求失败View,显示mock数据
                //                onSuccess(ResponseEntityToModule.parseJsonToModule(MockData.FRIEND_DATA,
                //                        BaseFriendModel.class));
            }
        });
    }

    //更新UI
    private void updateView() {
        mSwipeRefreshLayout.setRefreshing(false); //停止刷新
        mDatas = mRecommandData.list;
        mAdapter = new FriendRecyclerAdapter(mContext, mDatas);
        //加载更多初始化
        mLoadMoreWrapper = new LoadMoreWrapper(mAdapter);
        mLoadMoreWrapper.setLoadMoreView(R.layout.default_loading);
        mLoadMoreWrapper.setOnLoadMoreListener(this);
        mRecyclerView.setAdapter(mLoadMoreWrapper);
    }
}

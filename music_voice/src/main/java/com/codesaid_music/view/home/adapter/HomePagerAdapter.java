package com.codesaid_music.view.home.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codesaid_music.view.discory.DiscoryFragment;
import com.codesaid_music.view.friend.FriendFragment;
import com.codesaid_music.view.mine.MineFragment;
import com.codesaid_music.model.CHANNEL;

/**
 * Created By codesaid
 * On :2019-10-31
 * Package Name: com.codesaid_music.view.home.adapter
 * desc: 首页HomeActivity 的 Adapter
 */
public class HomePagerAdapter extends FragmentPagerAdapter {

    private CHANNEL[] mList;

    public HomePagerAdapter(FragmentManager fm, CHANNEL[] datas) {
        super(fm);
        mList = datas;
    }

    @Override
    public Fragment getItem(int position) {
        int type = mList[position].getValue();
        // 初始化对应的 Fragment
        switch (type) {
            case CHANNEL.MINE_ID:
                return MineFragment.newInstance();
            case CHANNEL.DISCORY_ID:
                return DiscoryFragment.newInstance();
            case CHANNEL.FRIEND_ID:
                return FriendFragment.newInstance();
            //            case CHANNEL.VIDEO_ID:
            //                return VideoFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.length;
    }
}

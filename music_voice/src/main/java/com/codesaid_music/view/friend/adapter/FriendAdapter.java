package com.codesaid_music.view.friend.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.codesaid.lib_audio.app.AudioHelper;
import com.codesaid.lib_commin_ui.MultiImageViewLayout;
import com.codesaid.lib_commin_ui.recyclerview.MultiItemTypeAdapter;
import com.codesaid.lib_commin_ui.recyclerview.base.ItemViewDelegate;
import com.codesaid.lib_commin_ui.recyclerview.base.ViewHolder;
import com.codesaid.lib_image_loader.api.ImageLoaderManager;
import com.codesaid_music.R;
import com.codesaid_music.model.friend.FriendBodyValue;
import com.codesaid_music.utils.UserManager;
import com.codesaid_music.view.login.LoginActivity;

import java.util.List;

/**
 * Created By codesaid
 * On :2019-11-15
 * Package Name: com.codesaid_music.view.friend.adapter
 */
public class FriendAdapter extends MultiItemTypeAdapter {

    public static final int MUSIC_TYPE = 0x01; // 音乐类型
    public static final int VIDEO_TYPE = 0x01; // 视频类型

    private Context mContext;

    public FriendAdapter(Context context, List<FriendBodyValue> datas) {
        super(context, datas);
        mContext = context;
        addItemViewDelegate(MUSIC_TYPE, new MusicItemDelegate());
    }

    /**
     * 图片类型 item
     */
    private class MusicItemDelegate implements ItemViewDelegate<FriendBodyValue> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_friend_list_picture_layout;
        }

        @Override
        public boolean isForViewType(FriendBodyValue item, int position) {
            return item.type == FriendAdapter.MUSIC_TYPE;
        }

        @Override
        public void convert(ViewHolder holder, final FriendBodyValue friendBodyValue, int position) {
            // 为 ViewHolder 绑定数据
            holder.setText(R.id.name_view, friendBodyValue.name + " 分享单曲:");
            holder.setText(R.id.fansi_view, friendBodyValue.fans + "粉丝");
            holder.setText(R.id.text_view, friendBodyValue.text);
            holder.setText(R.id.zan_view, friendBodyValue.zan);
            holder.setText(R.id.message_view, friendBodyValue.msg);
            holder.setText(R.id.audio_name_view, friendBodyValue.audioBean.name);
            holder.setText(R.id.audio_author_view, friendBodyValue.audioBean.album);
            holder.setOnClickListener(R.id.album_view, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //调用播放器装饰类
                    AudioHelper.addAudio((Activity) mContext, friendBodyValue.audioBean);
                }
            });
            holder.setOnClickListener(R.id.guanzhu_view, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!UserManager.getInstance().hasLogined()) {
                        // goto login
                        LoginActivity.start(mContext);
                    }
                }
            });

            ImageView avatar = holder.getView(R.id.photo_view);
            ImageLoaderManager.getInstance()
                    .displayImageForCircle(avatar, friendBodyValue.avatr);

            ImageView albumPicView = holder.getView(R.id.album_view);
            ImageLoaderManager.getInstance()
                    .displayImageForView(albumPicView, friendBodyValue.audioBean.albumPic);

            // 多图自动布局
            MultiImageViewLayout imageViewLayout = holder.getView(R.id.image_layout);
            imageViewLayout.setList(friendBodyValue.pics);
        }
    }
}

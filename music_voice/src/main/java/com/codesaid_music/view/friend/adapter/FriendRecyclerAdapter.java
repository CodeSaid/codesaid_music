package com.codesaid_music.view.friend.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.codesaid.lib_audio.app.AudioHelper;
import com.codesaid.lib_commin_ui.MultiImageViewLayout;
import com.codesaid.lib_commin_ui.recyclerview.MultiItemTypeAdapter;
import com.codesaid.lib_commin_ui.recyclerview.base.ItemViewDelegate;
import com.codesaid.lib_commin_ui.recyclerview.base.ViewHolder;
import com.codesaid.lib_image_loader.api.ImageLoaderManager;
import com.codesaid.video.videoplayer.VideoContextInterface;
import com.codesaid.video.videoplayer.core.VideoAdContext;
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
public class FriendRecyclerAdapter extends MultiItemTypeAdapter {

    public static final int MUSIC_TYPE = 0x01; //音乐类型
    public static final int VIDEO_TYPE = 0x02; //视频类型

    private Context mContext;

    public FriendRecyclerAdapter(Context context, List<FriendBodyValue> datas) {
        super(context, datas);
        mContext = context;
        addItemViewDelegate(MUSIC_TYPE, new MusicItemDelegate());
        addItemViewDelegate(VIDEO_TYPE, new VideoItemDelegate());
    }

    /**
     * 图片类型item
     */
    private class MusicItemDelegate implements ItemViewDelegate<FriendBodyValue> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_friend_list_picture_layout;
        }

        @Override
        public boolean isForViewType(FriendBodyValue item, int position) {
            return item.type == FriendRecyclerAdapter.MUSIC_TYPE;
        }

        @Override
        public void convert(ViewHolder holder, final FriendBodyValue recommandBodyValue, int position) {
            holder.setText(R.id.name_view, recommandBodyValue.name + " 分享单曲:");
            holder.setText(R.id.fansi_view, recommandBodyValue.fans + "粉丝");
            holder.setText(R.id.text_view, recommandBodyValue.text);
            holder.setText(R.id.zan_view, recommandBodyValue.zan);
            holder.setText(R.id.message_view, recommandBodyValue.msg);
            holder.setText(R.id.audio_name_view, recommandBodyValue.audioBean.name);
            holder.setText(R.id.audio_author_view, recommandBodyValue.audioBean.album);
            holder.setOnClickListener(R.id.album_layout, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //调用播放器装饰类
                    AudioHelper.addAudio((Activity) mContext, recommandBodyValue.audioBean);
                }
            });
            holder.setOnClickListener(R.id.guanzhu_view, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!UserManager.getInstance().hasLogined()) {
                        //goto login
                        LoginActivity.start(mContext);
                    }
                }
            });
            ImageView avatar = holder.getView(R.id.photo_view);
            ImageLoaderManager.getInstance().displayImageForCircle(avatar, recommandBodyValue.avatr);
            ImageView albumPicView = holder.getView(R.id.album_view);
            ImageLoaderManager.getInstance()
                    .displayImageForView(albumPicView, recommandBodyValue.audioBean.albumPic);

            MultiImageViewLayout imageViewLayout = holder.getView(R.id.image_layout);
            imageViewLayout.setList(recommandBodyValue.pics);
        }
    }

    private class VideoItemDelegate implements ItemViewDelegate<FriendBodyValue> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_friend_list_video_layout;
        }

        @Override
        public boolean isForViewType(FriendBodyValue item, int position) {
            return item.type == FriendRecyclerAdapter.VIDEO_TYPE;
        }

        @Override
        public void convert(ViewHolder holder, FriendBodyValue friendBodyValue, int position) {
            RelativeLayout relativeLayout = holder.getView(R.id.video_layout);
            VideoAdContext videoAdContext = new
                    VideoAdContext(relativeLayout, friendBodyValue.videoUrl);

            // 视频播放回调处理
            videoAdContext.setResultListener(new VideoContextInterface() {
                @Override
                public void onVideoSuccess() {

                }

                @Override
                public void onVideoFailed() {

                }

                @Override
                public void onVideoComplete() {

                }
            });

            holder.setText(R.id.fansi_view, friendBodyValue.fans + "粉丝");
            holder.setText(R.id.name_view, friendBodyValue.name + " 分享视频");
            holder.setText(R.id.text_view, friendBodyValue.text);
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
            ImageLoaderManager
                    .getInstance()
                    .displayImageForCircle(avatar, friendBodyValue.avatr);
        }
    }
}
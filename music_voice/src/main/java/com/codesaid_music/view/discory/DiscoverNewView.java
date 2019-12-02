package com.codesaid_music.view.discory;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codesaid.lib_commin_ui.recyclerview.CommonAdapter;
import com.codesaid.lib_commin_ui.recyclerview.base.ViewHolder;
import com.codesaid.lib_image_loader.api.ImageLoaderManager;
import com.codesaid_music.R;
import com.codesaid_music.model.discory.RecommendFooterValue;
import com.codesaid_music.model.discory.RecommendHeadValue;

/**
 * Created By codesaid
 * On :2019-12-02
 * Package Name: com.codesaid_music.view.discory
 */
public class DiscoverNewView extends RelativeLayout {

    private Context mContext;

    /*
     * UI
     */
    private RecyclerView mRecyclerView;
    /*
     * Data
     */
    private RecommendHeadValue mHeaderValue;

    public DiscoverNewView(Context context, RecommendHeadValue recommendHeadValue) {
        this(context, null, recommendHeadValue);
    }

    public DiscoverNewView(Context context, AttributeSet attrs, RecommendHeadValue recommendHeadValue) {
        super(context, attrs);
        mContext = context;
        mHeaderValue = recommendHeadValue;
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_discory_head_recommend_layout, this);
        TextView titleView = rootView.findViewById(R.id.title_view);
        titleView.setText("新歌");

        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRecyclerView.setAdapter(new CommonAdapter<RecommendFooterValue>(mContext,
                R.layout.item_discory_head_recommend_recycler_layout, mHeaderValue.footer) {

            @Override
            protected void convert(ViewHolder holder, RecommendFooterValue footerValue, int position) {
                holder.setText(R.id.text_view, footerValue.info);
                ImageView imageView = holder.getView(R.id.image_view);
                ImageLoaderManager.getInstance().displayImageForView(imageView, footerValue.imageUrl);
            }
        });
    }
}

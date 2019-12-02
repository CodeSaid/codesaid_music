package com.codesaid_music.view.discory;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codesaid.lib_commin_ui.recyclerview.CommonAdapter;
import com.codesaid.lib_commin_ui.recyclerview.base.ViewHolder;
import com.codesaid.lib_image_loader.api.ImageLoaderManager;
import com.codesaid_music.R;
import com.codesaid_music.model.discory.RecommendHeadValue;
import com.codesaid_music.model.discory.RecommendMiddleValue;

/**
 * Created By codesaid
 * On :2019-12-02
 * Package Name: com.codesaid_music.view.discory
 * desc : 推荐歌单 滚动页面
 */
public class DiscoverRecommendView extends RelativeLayout {

    private Context mContext;

    /*
     * UI
     */
    private RecyclerView mRecyclerView;
    /*
     * Data
     */
    private RecommendHeadValue mHeaderValue;

    public DiscoverRecommendView(Context context, RecommendHeadValue recommendHeadValue) {
        this(context, null, recommendHeadValue);
    }

    public DiscoverRecommendView(Context context, AttributeSet attrs, RecommendHeadValue recommendHeadValue) {
        super(context, attrs);
        mContext = context;
        mHeaderValue = recommendHeadValue;
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_discory_head_recommend_layout, this);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRecyclerView.setAdapter(new CommonAdapter<RecommendMiddleValue>(mContext,
                R.layout.item_discory_head_recommend_recycler_layout, mHeaderValue.middle) {

            @Override
            protected void convert(ViewHolder holder,
                                   RecommendMiddleValue value, int position) {
                holder.setText(R.id.text_view, value.info);
                ImageView imageView = holder.getView(R.id.image_view);
                ImageLoaderManager.getInstance().displayImageForView(imageView, value.imageUrl);
            }
        });
    }

}

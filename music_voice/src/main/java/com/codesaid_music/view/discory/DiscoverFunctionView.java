package com.codesaid_music.view.discory;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.codesaid_music.R;

/**
 * Created By codesaid
 * On :2019-11-30
 * Package Name: com.codesaid_music.view.discory
 */
public class DiscoverFunctionView extends RelativeLayout {
    private Context mContext;


    public DiscoverFunctionView(Context context) {
        this(context, null);
    }

    public DiscoverFunctionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_discover_header_function_layout, this);
    }
}

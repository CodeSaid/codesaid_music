package com.codesaid.lib_commin_ui.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.codesaid.lib_commin_ui.utils.StatusBarUtil;

/**
 * Created By codesaid
 * On :2019-10-31
 * Package Name: com.codesaid.lib_commin_ui.base
 */
public class BaseActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.statusBarLightMode(this);

    }
}

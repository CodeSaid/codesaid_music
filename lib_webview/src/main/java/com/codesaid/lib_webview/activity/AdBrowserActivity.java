package com.codesaid.lib_webview.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;

import com.codesaid.lib_webview.adbrowser.AdBrowserLayout;
import com.codesaid.lib_webview.adbrowser.AdBrowserWebViewClient;
import com.codesaid.lib_webview.adbrowser.Base64Drawables;
import com.codesaid.lib_webview.adbrowser.BrowserWebView;
import com.codesaid.lib_webview.utils.Utils;

/**
 * Created By codesaid
 * On :2019-11-21
 * Package Name: com.codesaid.lib_webview.activity
 * desc: WebView 页面
 */
public class AdBrowserActivity extends Activity {

    /**
     * 常量区
     */
    public static final String KEY_URL = "url";

    /**
     * UI
     */
    private BrowserWebView mAdBrowserWebview;
    private AdBrowserLayout mLayout;
    private View mProgress;
    private Button mBackButton;
    private Base64Drawables mBase64Drawables = new Base64Drawables();

    /**
     * Data
     */
    protected String mUrl;
    private boolean mIsBackFromMarket = false;
    private AdBrowserWebViewClient.Listener mWebClientListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mLayout = new AdBrowserLayout(this.getApplicationContext());
        mProgress = mLayout.getProgressBar();
        mBackButton = mLayout.getBackButton();
        mAdBrowserWebview = mLayout.getWebView();
        setContentView(mLayout);
        initWebView(mAdBrowserWebview);
        if (savedInstanceState != null) {
            mAdBrowserWebview.restoreState(savedInstanceState);
        } else {
            mAdBrowserWebview.loadUrl(mUrl);
        }
        initButtonListeners(mAdBrowserWebview);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdBrowserWebview != null) {
            mAdBrowserWebview.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        if (mAdBrowserWebview != null) {
            mAdBrowserWebview.clearCache(true);
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsBackFromMarket) {
            //            finish();
        }
        mIsBackFromMarket = true;
        mLayout.getProgressBar().setVisibility(View.INVISIBLE);
    }

    private void initWebView(BrowserWebView webview) {
        mWebClientListener = initAdBrowserClientListener();
        AdBrowserWebViewClient client = new AdBrowserWebViewClient(mWebClientListener);
        webview.setWebViewClient(client);
        webview.getSettings().setBuiltInZoomControls(false);
    }

    private AdBrowserWebViewClient.Listener initAdBrowserClientListener() {
        return new AdBrowserWebViewClient.Listener() {
            @Override
            public void onPageStarted() {
                mProgress.setVisibility(View.VISIBLE);
            }

            @Override
            @SuppressLint("NewApi")
            public void onPageFinished(boolean canGoBack) {
                mProgress.setVisibility(View.INVISIBLE);
                if (canGoBack) {
                    setImage(mBackButton, mBase64Drawables.getBackActive());
                } else {
                    setImage(mBackButton, mBase64Drawables.getBackInactive());
                }
            }

            @Override
            public void onReceiveError() {
                finish();
            }

            @Override
            public void onLeaveApp() {

            }
        };
    }

    @SuppressLint({"NewApi", "ObsoleteSdkInt"})
    private void setImage(Button button, String imageString) {
        if (Build.VERSION.SDK_INT < 16) {
            button.setBackgroundDrawable(Utils.decodeImage(imageString));
        } else {
            button.setBackground(Utils.decodeImage(imageString));
        }
    }

    private void initButtonListeners(final WebView webView) {
        mLayout.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    mLayout.getProgressBar().setVisibility(View.VISIBLE);
                    webView.goBack();
                }
            }
        });

        mLayout.getCloseButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLayout.getRefreshButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayout.getProgressBar().setVisibility(View.VISIBLE);
                webView.reload();
            }
        });

        mLayout.getNativeButton().setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("RedundantConditionalExpression")
            @Override
            public void onClick(View v) {
                String url = webView.getUrl();
                if (url != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                    boolean isActivityResolved =
                            getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
                                    != null ? true : false;
                    if (isActivityResolved) {
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mAdBrowserWebview.canGoBack()) {
                mAdBrowserWebview.goBack();
                return true;
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mIsBackFromMarket = false;
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mAdBrowserWebview.saveState(outState);
        super.onSaveInstanceState(outState);
    }
}

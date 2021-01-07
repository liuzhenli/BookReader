package com.liuzhenli.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.liuzhenli.reader.base.BaseActivity;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.view.webview.ZLWebChromeClient;
import com.liuzhenli.reader.view.webview.ZLWebViewClient;
import com.microedu.reader.R;

import butterknife.BindView;

/**
 * describe: web view
 *
 * @author Liuzhenli on 2019-08-17 19:27
 * @since 1.0.0
 */
public class WebViewActivity extends BaseActivity {
    public static final String INTENT_ID = "url";
    public ValueCallback<Uri[]> mUploadMessageForAndroid5;
    public ValueCallback<Uri> mUploadMessage;
    private String mUrl;
    @BindView(R.id.web_view)
    WebView mWebView;

    public static void start(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(INTENT_ID, url);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_webview;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initToolBar() {
        mTvRight.setText("关闭");
        mTvRight.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void initData() {

    }

    @Override
    public void configViews() {
        mUrl = getIntent().getStringExtra(INTENT_ID);
        mWebView.setWebChromeClient(new ZLWebChromeClient(mContext));
        mWebView.setWebViewClient(new ZLWebViewClient(mContext));
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        // mWebView.addJavascriptInterface(new JsReadBook(mHandler), "chineseallread");
        //解决没有声音的问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.setMediaPlaybackRequiresUserGesture(false);
        }
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);// 开启本地存储，某些功能才能触发（比如网站触摸式菜单）
        settings.setDatabaseEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + "/webcache";
        // String cacheDirPath =
        // getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME;
        // 设置数据库缓存路径
        settings.setDatabasePath(cacheDirPath);
        // 设置 Application Caches 缓存目录
        settings.setAppCachePath(cacheDirPath);
        settings.setAppCacheEnabled(true);
        // 设置可以访问文件
        settings.setAllowFileAccess(true);
        mWebView.loadUrl(mUrl);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    onBackPressed();
                }
            }
        });
    }

    public void setRefresh(boolean b) {

    }

    public void startRefresh() {

    }
}

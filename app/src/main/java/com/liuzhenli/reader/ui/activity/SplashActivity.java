
package com.liuzhenli.reader.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.liuzhenli.common.BaseApplication;
import com.liuzhenli.common.utils.GsonUtils;
import com.liuzhenli.common.utils.IOUtils;
import com.liuzhenli.reader.bean.Sayings;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.base.BaseActivity;
import com.liuzhenli.reader.utils.AppConfigManager;
import com.liuzhenli.reader.view.CountDownView;
import com.microedu.reader.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

/**
 * @author Liuzhenli
 * @since 2019-07-07 08:54
 */
public class SplashActivity extends BaseActivity {
    @BindView(R.id.countdown_view)
    CountDownView mCountDownView;
    @BindView(R.id.tv_saying)
    TextView mTvSaying;
    @BindView(R.id.tv_saying_author)
    TextView mTvAuthor;

    private boolean hasGoHome = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mIsFullScreen = true;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initData() {
    }

    @Override
    public void configViews() {
        mCountDownView.startProgress(4000);
        mCountDownView.setOnClickListener(v -> {
            mCountDownView.setHasClickClip(true);
            if (!hasGoHome) {
                hasGoHome = true;
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }
        });
        Sayings sayings = AppConfigManager.getInstance().getSayings();
        mTvSaying.setText(sayings.getSaying());
        mTvAuthor.setText(String.format("BY:%s", sayings.getAuthor()));
    }
}

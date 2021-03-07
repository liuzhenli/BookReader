
package com.liuzhenli.reader.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.liuzhenli.common.AppComponent;
import com.liuzhenli.reader.bean.Sayings;
import com.liuzhenli.common.base.BaseActivity;
import com.liuzhenli.reader.utils.AppConfigManager;
import com.microedu.reader.databinding.ActivitySplashBinding;


/**
 * @author Liuzhenli
 * @since 2019-07-07 08:54
 */
public class SplashActivity extends BaseActivity {
    private boolean hasGoHome = false;
    private ActivitySplashBinding inflate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mIsFullScreen = true;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View bindContentView() {
        inflate = ActivitySplashBinding.inflate(getLayoutInflater());
        return inflate.getRoot();
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
        inflate.countDownView.startProgress(4000);
        inflate.countDownView.setOnClickListener(v -> {
            inflate.countDownView.setHasClickClip(true);
            if (!hasGoHome) {
                hasGoHome = true;
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }
        });
        Sayings sayings = AppConfigManager.getInstance().getSayings();
        inflate.tvSaying.setText(sayings.getSaying());
        inflate.tvSayingAuthor.setText(String.format("BY:%s", sayings.getAuthor()));
    }
}

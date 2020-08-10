
package com.liuzhenli.reader.ui.activity;


import android.content.Intent;

import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.base.BaseActivity;
import com.liuzhenli.reader.view.CountDownView;
import com.microedu.reader.R;

import butterknife.BindView;

/**
 * @author Liuzhenli
 * @since 2019-07-07 08:54
 */
public class SplashActivity extends BaseActivity {
    @BindView(R.id.countdown_view)
    CountDownView mCountDownView;

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
        mCountDownView.startProgress(3000);
        mCountDownView.setOnClickListener(v -> {
            mCountDownView.setHasClickClip(true);
            SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
            SplashActivity.this.finish();
        });
    }
}

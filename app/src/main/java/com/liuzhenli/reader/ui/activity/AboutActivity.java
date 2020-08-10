package com.liuzhenli.reader.ui.activity;

import android.content.Context;
import android.content.Intent;

import com.liuzhenli.reader.base.BaseActivity;
import com.liuzhenli.reader.network.AppComponent;
import com.microedu.reader.R;

/**
 * Description:
 *
 * @author liuzhenli 2020/8/10
 * Email: 848808263@qq.com
 */
public class AboutActivity extends BaseActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void initToolBar() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void configViews() {

    }
}

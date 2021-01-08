package com.liuzhenli.reader.ui.activity;

import android.content.Context;
import android.content.Intent;

import com.liuzhenli.reader.base.BaseActivity;
import com.liuzhenli.reader.network.AppComponent;
import com.microedu.reader.R;

/**
 * Description:
 *
 * @author liuzhenli 2021/1/7
 * Email: 848808263@qq.com
 */
public class FilePathsListActivity extends BaseActivity {
    public static void start(Context context) {
        Intent intent = new Intent(context, FilePathsListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_filepathlist;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void initToolBar() {
        mTvTitle.setText("文件路径");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void configViews() {

    }
}

package com.liuzhenli.reader.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.liuzhenli.common.FileHelp;
import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.common.utils.FileUtils;
import com.liuzhenli.reader.ReaderApplication;
import com.liuzhenli.common.base.BaseActivity;
import com.liuzhenli.reader.ui.contract.SettingContract;
import com.liuzhenli.reader.ui.presenter.SettingPresenter;
import com.micoredu.readerlib.helper.BookshelfHelper;
import com.microedu.reader.R;
import com.microedu.reader.databinding.ActSettingBinding;

/**
 * Description:设置页面
 *
 * @author liuzhenli 2020/10/26
 * Email: 848808263@qq.com
 */
@SuppressLint("NonConstantResourceId")
public class SettingActivity extends BaseActivity<SettingPresenter> implements SettingContract.View {

    private ActSettingBinding inflate;

    public static void start(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected View bindContentView() {
        inflate = ActSettingBinding.inflate(getLayoutInflater());
        return inflate.getRoot();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    protected void initToolBar() {
        mTvTitle.setText(R.string.setting);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void configViews() {
        ClickUtils.click(inflate.mViewClearCache, o -> {
            showDialog();
            BookshelfHelper.clearCaches(true);
            FileUtils.deleteFileOrDirectory(mContext.getCacheDir());
            FileUtils.deleteFile(FileHelp.getCachePath());
            toast("缓存已清理");
            hideDialog();
            mPresenter.getCacheSize();
        });

        ClickUtils.click(inflate.viewSettingBackup, o -> {
            BackupSettingActivity.start(mContext);
        });

        ClickUtils.click(inflate.mVFilePath, o -> {
            FilePathsListActivity.start(mContext);
        });
        ClickUtils.click(inflate.mViewAppDatabase, o -> {
            DatabaseTableListActivity.start(mContext);
        });
        if (ReaderApplication.isDebug) {
            inflate.mViewAppDatabase.setVisibility(View.VISIBLE);
        }
        mPresenter.getCacheSize();
    }

    @Override
    public void showCacheSize(String size) {
        inflate.mTvCacheSize.setText(size);
    }

    @Override
    public void showError(Exception e) {

    }

    @Override
    public void complete() {

    }
}

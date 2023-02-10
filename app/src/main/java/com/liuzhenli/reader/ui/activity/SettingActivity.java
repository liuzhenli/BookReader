/*
package com.liuzhenli.reader.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.FileHelp;
import com.liuzhenli.common.base.BaseActivity;
import com.liuzhenli.common.utils.AppSharedPreferenceHelper;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.common.utils.FileUtils;
import com.liuzhenli.reader.DaggerReadBookComponent;
import com.liuzhenli.reader.ReaderApplication;
import com.liuzhenli.reader.event.OnWebDavSetEvent;
import com.liuzhenli.reader.ui.contract.SettingContract;
import com.liuzhenli.reader.ui.presenter.SettingPresenter;
import com.micoredu.reader.R;
import com.micoredu.reader.databinding.ActSettingBinding;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

*/
/**
 * Description:设置页面
 *
 * @author liuzhenli 2020/10/26
 * Email: 848808263@qq.com
 *//*

@SuppressLint("NonConstantResourceId")
public class SettingActivity extends BaseActivity<SettingPresenter, ActSettingBinding> implements SettingContract.View {

    public static void start(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected ActSettingBinding inflateView(LayoutInflater inflater) {
        return ActSettingBinding.inflate(inflater);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerReadBookComponent.builder().build().inject(this);
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
        ClickUtils.click(binding.mViewClearCache, o -> {
            showDialog();
            BookHelp.clearCaches(true);
            FileUtils.deleteFileOrDirectory(mContext.getCacheDir());
            FileUtils.deleteFile(FileHelp.getCachePath());
            toast("缓存已清理");
            hideDialog();
            mPresenter.getCacheSize();
        });

        //backup
        ClickUtils.click(binding.viewSettingBackup, o -> {
            BackupSettingActivity.start(mContext);
        });

        ClickUtils.click(binding.mVFilePath, o -> {
            FilePathsListActivity.start(mContext);
        });
        ClickUtils.click(binding.mViewAppDatabase, o -> {
            DatabaseTableListActivity.start(mContext);
        });
        if (ReaderApplication.isDebug) {
            binding.mViewAppDatabase.setVisibility(View.VISIBLE);
        }
        mPresenter.getCacheSize();
        //check if webdav is set
        onWebDavSet(null);
    }

    @Override
    public void showCacheSize(String size) {
        binding.mTvCacheSize.setText(size);
    }

    @Override
    public void showError(Exception e) {

    }

    @Override
    public void complete() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebDavSet(OnWebDavSetEvent event) {
        if (TextUtils.isEmpty(AppSharedPreferenceHelper.getWebDavAccountName())
                || TextUtils.isEmpty(AppSharedPreferenceHelper.getWebDavAddPwd())) {
            binding.viewWebDavIndicator.setVisibility(View.VISIBLE);
        } else {
            if (event != null && event.isSuccess()) {
                binding.viewWebDavIndicator.setVisibility(View.GONE);
            } else {
                binding.viewWebDavIndicator.setVisibility(View.VISIBLE);
            }
        }
    }
}
*/

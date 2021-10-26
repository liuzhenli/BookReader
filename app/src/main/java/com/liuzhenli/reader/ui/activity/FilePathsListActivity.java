package com.liuzhenli.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.core.graphics.PathUtils;

import com.liuzhenli.common.BaseApplication;
import com.liuzhenli.common.base.BaseActivity;
import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.utils.AppSharedPreferenceHelper;
import com.liuzhenli.common.utils.AppUtils;
import com.liuzhenli.common.utils.PathUtil;
import com.liuzhenli.reader.utils.storage.Backup;
import com.microedu.reader.databinding.ActFilepathlistBinding;


/**
 * Description:
 *
 * @author liuzhenli 2021/1/7
 * Email: 848808263@qq.com
 */
public class FilePathsListActivity extends BaseActivity {

    private ActFilepathlistBinding inflate;

    public static void start(Context context) {
        Intent intent = new Intent(context, FilePathsListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected View bindContentView() {
        inflate = ActFilepathlistBinding.inflate(getLayoutInflater());
        return inflate.getRoot();
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
        inflate.mTvBookSourcePath.setText(PathUtil.Companion.getPathShow(BaseApplication.getInstance().getFilesDir().getAbsolutePath()));
        inflate.mTvBookSourceBackupPath.setText(PathUtil.Companion.getPathShow(AppSharedPreferenceHelper.getBackupPath(Backup.INSTANCE.defaultPath())));
        inflate.mTvBookCachePath.setText(PathUtil.Companion.getPathShow(BaseApplication.getInstance().getDownloadPath()));
    }
}

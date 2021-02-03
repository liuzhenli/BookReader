package com.liuzhenli.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.liuzhenli.common.BaseApplication;
import com.liuzhenli.reader.base.BaseActivity;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.utils.storage.Backup;
import com.microedu.reader.R;

import butterknife.BindView;

import com.liuzhenli.common.BaseApplication;

import com.liuzhenli.reader.utils.storage.Backup;

/**
 * Description:
 *
 * @author liuzhenli 2021/1/7
 * Email: 848808263@qq.com
 */
public class FilePathsListActivity extends BaseActivity {

    /**
     * 书源导出路径
     */
    @BindView(R.id.textView3)
    TextView mTvBookSourcePath;
    /**
     * 书源导备份路径
     */
    @BindView(R.id.tv_backup_path)
    TextView mTvBookSourceBackupPath;

    @BindView(R.id.tv_book_cache_path)
    TextView mTvBookCachePath;

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
        mTvBookSourcePath.setText(BaseApplication.getInstance().getFilesDir().getAbsolutePath());
        mTvBookSourceBackupPath.setText(Backup.INSTANCE.getDefaultPath());
        mTvBookCachePath.setText(BaseApplication.getInstance().getDownloadPath());
    }
}

package com.liuzhenli.reader.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.reader.base.BaseActivity;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.observer.SampleProgressObserver;
import com.liuzhenli.reader.utils.ToastUtil;
import com.liuzhenli.reader.utils.image.ImageUtil;
import com.liuzhenli.reader.view.loading.DialogUtil;
import com.micoredu.readerlib.helper.BookshelfHelper;
import com.microedu.reader.R;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import io.reactivex.Observable;

/**
 * Description:设置页面
 *
 * @author liuzhenli 2020/10/26
 * Email: 848808263@qq.com
 */
@SuppressLint("NonConstantResourceId")
public class SettingActivity extends BaseActivity {


    @BindView(R.id.view_setting_file_path)
    View mVFilePath;

    @BindView(R.id.view_setting_clear_cache)
    View mViewClearCache;
    @BindView(R.id.view_setting_backup)
    View mViewBackUp;

    public static void start(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_setting;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

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
        ClickUtils.click(mViewClearCache, o -> {
            showDialog();
           RxUtil.subscribe(Observable.create(emitter -> {
                ImageUtil.clearMemoryCache(getApplicationContext());
                ImageUtil.clearDiskCache(getApplicationContext());
                BookshelfHelper.clearCaches(true);
                emitter.onNext(true);
            }), new SampleProgressObserver<Boolean>() {
                @Override
                public void onNext(@NotNull Boolean aBoolean) {
                    hideDialog();
                    toast("缓存已清理");
                }
            });

        });

        ClickUtils.click(mViewBackUp, o -> {
            BackupSettingActivity.start(mContext);
        });

        ClickUtils.click(mVFilePath, o -> {
            FilePathsListActivity.start(mContext);
        });
    }
}

package com.liuzhenli.reader.ui.activity;

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

import butterknife.BindView;
import io.reactivex.Observable;

/**
 * Description:设置页面
 *
 * @author liuzhenli 2020/10/26
 * Email: 848808263@qq.com
 */
public class SettingActivity extends BaseActivity {

    @BindView(R.id.view_setting_clear_cache)
    View mViewClearCache;

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

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void configViews() {
        ClickUtils.click(mViewClearCache, o -> {
            clearCache();
            ImageUtil.clearMemoryCache(getApplicationContext());
            RxUtil.subscribe(Observable.create(emitter -> {
                ImageUtil.clearDiskCache(getApplicationContext());
                emitter.onNext(true);
            }), new SampleProgressObserver<Boolean>() {
                @Override
                public void onNext(Boolean aBoolean) {
                    ToastUtil.showToast("");
                }
            });

        });
    }

    private void clearCache() {
        DialogUtil.showMessagePositiveDialog(mContext, getResources().getString(R.string.clear_cache),
                getResources().getString(R.string.sure_del_download_book),
                getResources().getString(R.string.no), (dialog, index) -> {
                    BookshelfHelper.clearCaches(false);
                }, getResources().getString(R.string.yes), (dialog, index) -> {
                    BookshelfHelper.clearCaches(true);
                }, true);

    }
}

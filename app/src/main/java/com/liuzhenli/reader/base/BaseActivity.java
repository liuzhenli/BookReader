package com.liuzhenli.reader.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.liuzhenli.reader.ReaderApplication;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.base.rxlife.RxAppCompatActivity;
import com.micoredu.readerlib.utils.bar.ImmersionBar;
import com.microedu.reader.R;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * @author Liuzhenli
 * @since 2019-07-06 17:18
 */
public abstract class BaseActivity<T1 extends BaseContract.BasePresenter> extends RxAppCompatActivity {
    protected Context mContext;
    public TextView mTvTitle;
    public Toolbar mToolBar;
    @Inject
    public T1 mPresenter;
    /***状态栏*/
    protected ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mContext = this;
        mImmersionBar = ImmersionBar.with(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }


        ButterKnife.bind(this);
        setupActivityComponent(ReaderApplication.getInstance().getAppComponent());
        mToolBar = findViewById(R.id.toolbar);
        if (mToolBar != null) {
            mTvTitle = findViewById(R.id.tv_toolbar_title);
            mToolBar.setNavigationOnClickListener(v -> onBackPressed());
            initToolBar();
        }
        if (mPresenter != null) {
            //noinspection unchecked
            mPresenter.attachView(this);
        }
        initData();
        configViews();
        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        }
        initImmersionBar();
    }
    public void startActivityByAnim(Intent intent, int animIn, int animExit) {
        startActivity(intent);
        overridePendingTransition(animIn, animExit);
    }
    protected abstract int getLayoutId();

    protected abstract void setupActivityComponent(AppComponent appComponent);

    protected abstract void initToolBar();

    protected abstract void initData();

    protected abstract void configViews();

    protected void restoreState(Bundle savedInstanceState) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 如果你的app可以横竖屏切换，并且适配4.4或者emui3手机请务必在onConfigurationChanged方法里添加这句话
        ImmersionBar.with(this).init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    /**
     * 沉浸状态栏
     */
    protected void initImmersionBar() {
        //mImmersionBar.navigationBarColor(R.color.md_red_900);
        mImmersionBar.statusBarColor(R.color.main);
        mImmersionBar.statusBarDarkFont(true);
        mImmersionBar.fitsSystemWindows(true);
        mImmersionBar.init();
    }
}

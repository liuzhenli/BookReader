package com.micoredu.readerlib;


import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.liuzhenli.common.base.BaseActivity;
import com.liuzhenli.common.base.BaseContract;
import com.micoredu.readerlib.page.PageView;

/**
 * @author liuzhenli
 */
public abstract class BaseReaderActivity<T1 extends BaseContract.BasePresenter> extends BaseActivity<T1> implements View.OnTouchListener {

    protected Context mContext;

    protected abstract int getLayoutId();

    protected abstract void initData();

    protected abstract void configViews();

    public abstract void autoChangeSource();

    public abstract void showSnackBar(PageView pageView, String msg);

    public void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    /**
     * 沉浸状态栏
     */
    protected abstract void initImmersionBar();
}

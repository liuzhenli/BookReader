package com.micoredu.reader.ui.activity;


import android.view.View;
import android.widget.Toast;

import androidx.viewbinding.ViewBinding;

import com.liuzhenli.common.base.BaseContract;
import com.micoredu.reader.ReaderBaseActivity;
import com.micoredu.reader.page.PageView;

/**
 * @author liuzhenli
 */
public abstract class BaseReaderActivity<T1 extends BaseContract.BasePresenter,V extends ViewBinding> extends ReaderBaseActivity<T1,V> implements View.OnTouchListener {

    public abstract void autoChangeSource();

    public abstract void showSnackBar(PageView pageView, String msg);

    public void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}

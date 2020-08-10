package com.micoredu.readerlib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.micoredu.readerlib.page.PageView;

/**
 * @author liuzhenli
 */
public abstract class BaseReaderActivity extends AppCompatActivity {


    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mContext = this;
        initData();
        configViews();
    }

    protected abstract int getLayoutId();

    protected abstract void initData();

    protected abstract void configViews();

    public abstract void autoChangeSource();

    public abstract void showSnackBar(PageView pageView, String msg);

    public void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}

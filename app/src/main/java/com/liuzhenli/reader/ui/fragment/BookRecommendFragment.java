package com.liuzhenli.reader.ui.fragment;

import android.os.Bundle;

import com.liuzhenli.reader.base.BaseFragment;
import com.liuzhenli.reader.network.AppComponent;
import com.microedu.reader.R;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-11-09 22:28
 */
public class BookRecommendFragment extends BaseFragment {

    public static BookRecommendFragment getInstance() {
        BookRecommendFragment instance = new BookRecommendFragment();
        Bundle bundle = new Bundle();
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_test;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void attachView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void configViews() {

    }
}

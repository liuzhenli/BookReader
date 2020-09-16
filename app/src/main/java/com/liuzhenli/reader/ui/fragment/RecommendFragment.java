package com.liuzhenli.reader.ui.fragment;

import android.os.Bundle;

import com.liuzhenli.reader.base.BaseFragment;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.ui.contract.RecommendContract;
import com.liuzhenli.reader.ui.presenter.RecommendPresenter;
import com.microedu.reader.R;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-11-09 22:28
 */
public class RecommendFragment extends BaseFragment<RecommendPresenter> implements RecommendContract.View {

    public static RecommendFragment getInstance() {
        RecommendFragment instance = new RecommendFragment();
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
        appComponent.inject(appComponent);
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

    @Override
    public void showSource() {

    }

    @Override
    public void showError(Exception e) {

    }

    @Override
    public void complete() {

    }
}

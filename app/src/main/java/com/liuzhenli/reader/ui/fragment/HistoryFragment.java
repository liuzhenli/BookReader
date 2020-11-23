package com.liuzhenli.reader.ui.fragment;

import android.os.Bundle;

import com.liuzhenli.reader.base.BaseFragment;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.ui.contract.HistoryContract;
import com.liuzhenli.reader.ui.presenter.HistoryPresenter;
import com.microedu.reader.R;

/**
 * Description:
 *
 * @author liuzhenli 2020/11/22
 * Email: 848808263@qq.com
 */
public class HistoryFragment extends BaseFragment<HistoryPresenter> implements HistoryContract.View {

    public static HistoryFragment getInstance() {
        HistoryFragment instance = new HistoryFragment();
        Bundle bundle = new Bundle();
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void showError(Exception e) {

    }

    @Override
    public void complete() {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_history;
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

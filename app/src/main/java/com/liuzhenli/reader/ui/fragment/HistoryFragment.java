package com.liuzhenli.reader.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liuzhenli.common.base.BaseFragment;
import com.liuzhenli.common.AppComponent;
import com.liuzhenli.reader.ui.contract.HistoryContract;
import com.liuzhenli.reader.ui.presenter.HistoryPresenter;
import com.micoredu.reader.databinding.FragmentHistoryBinding;

/**
 * Description:
 *
 * @author liuzhenli 2020/11/22
 * Email: 848808263@qq.com
 */
public class HistoryFragment extends BaseFragment<HistoryPresenter> implements HistoryContract.View {

    private FragmentHistoryBinding inflate;

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
    public View bindContentView(LayoutInflater inflater, ViewGroup container, boolean attachParent) {
        inflate = FragmentHistoryBinding.inflate(inflater, container, attachParent);
        return inflate.getRoot();
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

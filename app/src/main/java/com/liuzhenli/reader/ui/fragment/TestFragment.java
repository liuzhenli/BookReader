package com.liuzhenli.reader.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liuzhenli.common.base.BaseFragment;
import com.liuzhenli.common.AppComponent;
import com.microedu.reader.databinding.FragmentTestBinding;

public class TestFragment extends BaseFragment {

    private FragmentTestBinding inflate;

    @Override
    public View bindContentView(LayoutInflater inflater, ViewGroup container, boolean attachParent) {
        inflate = FragmentTestBinding.inflate(inflater, container, attachParent);
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

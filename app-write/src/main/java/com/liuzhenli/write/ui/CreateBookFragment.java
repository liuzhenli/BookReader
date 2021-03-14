package com.liuzhenli.write.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.base.BaseFragment;
import com.liuzhenli.write.databinding.FgWriteCreatebookBinding;

/**
 * Description:
 *
 * @author liuzhenli 3/13/21
 * Email: 848808263@qq.com
 */
public class CreateBookFragment extends BaseFragment {

    public static CreateBookFragment getInstance() {
        CreateBookFragment instance = new CreateBookFragment();
        Bundle bundle = new Bundle();
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public View bindContentView(LayoutInflater inflater, ViewGroup container, boolean attachParent) {
        return FgWriteCreatebookBinding.inflate(inflater, container, attachParent).getRoot();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initData() {

    }

    @Override
    public void configViews() {

    }
}

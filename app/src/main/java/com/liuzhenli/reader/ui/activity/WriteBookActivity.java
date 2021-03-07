package com.liuzhenli.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.liuzhenli.common.base.BaseActivity;
import com.liuzhenli.common.AppComponent;
import com.liuzhenli.reader.ui.contract.WriteBookContract;
import com.liuzhenli.reader.ui.presenter.WriteBookPresenter;
import com.microedu.reader.databinding.ActWirtebookBinding;

/**
 * Description:
 *
 * @author liuzhenli 2021/1/20
 * Email: 848808263@qq.com
 */
public class WriteBookActivity extends BaseActivity<WriteBookPresenter> implements WriteBookContract.View {


    public static void start(Context context) {
        Intent intent = new Intent(context, WriteBookActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected View bindContentView() {
        ActWirtebookBinding inflate = ActWirtebookBinding.inflate(getLayoutInflater());
        return inflate.getRoot();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void initToolBar() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void configViews() {

    }

    @Override
    public void showError(Exception e) {

    }

    @Override
    public void complete() {

    }
}

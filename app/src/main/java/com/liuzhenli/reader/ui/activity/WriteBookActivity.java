package com.liuzhenli.reader.ui.activity;

import android.content.Context;
import android.content.Intent;

import com.liuzhenli.reader.base.BaseActivity;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.ui.contract.WriteBookContract;
import com.liuzhenli.reader.ui.presenter.WriteBookPresenter;
import com.microedu.reader.R;

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
    protected int getLayoutId() {
        return R.layout.act_wirtebook;
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

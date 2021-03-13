package com.liuzhenli.write.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.liuzhenli.common.base.BaseBean;
import com.liuzhenli.write.WriteBookComponent;
import com.liuzhenli.write.databinding.ActWirtebookBinding;
import com.liuzhenli.write.ui.WriteBaseActivity;
import com.liuzhenli.write.ui.contract.WriteBookContract;
import com.liuzhenli.write.ui.presenter.WriteBookPresenter;

/**
 * Description:
 *
 * @author liuzhenli 2021/1/20
 * Email: 848808263@qq.com
 */
public class WriteBookActivity extends WriteBaseActivity<WriteBookPresenter> implements WriteBookContract.View {


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
    protected void setupActivityComponent(WriteBookComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    protected void initToolBar() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void configViews() {
        mPresenter.getLocalData();
    }

    @Override
    public void showError(Exception e) {

    }

    @Override
    public void complete() {

    }

    @Override
    public void showLocalData(BaseBean data) {

    }
}

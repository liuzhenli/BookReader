package com.liuzhenli.reader.ui.activity;

import android.view.LayoutInflater;

import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.base.BaseActivity;
import com.liuzhenli.common.base.BaseBean;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.reader.DaggerReadBookComponent;
import com.liuzhenli.reader.ui.contract.LoginContract;
import com.liuzhenli.reader.ui.presenter.LoginPresenter;
import com.microedu.reader.databinding.ActivityLoginBinding;


/**
 * @author Liuzhenli
 * @since 2019-07-07 10:25
 */
public class LoginActivity extends BaseActivity<LoginPresenter, ActivityLoginBinding> implements LoginContract.View {

    @Override
    protected ActivityLoginBinding inflateView(LayoutInflater inflater) {
        return ActivityLoginBinding.inflate(inflater);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerReadBookComponent.builder().build().inject(this);
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void configViews() {
        ClickUtils.click(binding.tvLogin, o ->
                mPresenter.login(binding.mEtUserName.getText().toString(), binding.mEtPassword.getText().toString()));
    }

    @Override
    public void showData(BaseBean data) {

    }

    @Override
    public void showError(Exception e) {

    }

    @Override
    public void complete() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

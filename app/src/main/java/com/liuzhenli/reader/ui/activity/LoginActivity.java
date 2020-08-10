package com.liuzhenli.reader.ui.activity;

import android.widget.EditText;

import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.base.BaseActivity;
import com.liuzhenli.reader.base.BaseBean;
import com.liuzhenli.reader.ui.contract.LoginContract;
import com.liuzhenli.reader.ui.presenter.LoginPresenter;
import com.microedu.reader.R;


import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Liuzhenli
 * @since 2019-07-07 10:25
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {


    @BindView(R.id.et_username)
    EditText mEtUserName;
    @BindView(R.id.et_password)
    EditText mEtPassword;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void configViews() {

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
    @OnClick(R.id.tv_login)
    public void login(){
        mPresenter.login(mEtUserName.getText().toString(), mEtPassword.getText().toString());
    }
}

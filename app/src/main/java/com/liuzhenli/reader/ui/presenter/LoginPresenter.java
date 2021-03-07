package com.liuzhenli.reader.ui.presenter;

import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.common.base.BaseBean;
import com.liuzhenli.reader.network.Api;
import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.common.observer.SampleProgressObserver;
import com.liuzhenli.reader.ui.contract.LoginContract;
import com.liuzhenli.common.utils.AppUtils;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

/**
 * @author Liuzhenli
 * @since 2019-07-07 11:55
 */
public class LoginPresenter extends RxPresenter<LoginContract.View> implements LoginContract.Presenter<LoginContract.View> {
    private static final String TAG = "LoginPresenter";
    private Api mApi;

    @Inject
    public LoginPresenter(Api api) {
        mApi = api;
    }

    @Override
    public void login(String phone, String password) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("phone", "+86" + phone);
        params.put("password", password);
        params.put("app_version", AppUtils.getFormatVersionCode());
        params.put("unique_id", AppUtils.getFormatDeviceUnique() );

        DisposableObserver disposable = RxUtil.subscribe(mApi.getLoginData(params), new SampleProgressObserver<BaseBean>(mView) {

            @Override
            public void onNext(BaseBean baseBean) {

            }

        });
        addSubscribe(disposable);
    }
}

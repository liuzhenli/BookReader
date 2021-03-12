package com.liuzhenli.reader.ui.presenter;

import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.reader.ui.contract.LoginContract;
import com.liuzhenli.common.utils.AppUtils;

import java.util.HashMap;

import javax.inject.Inject;


/**
 * @author Liuzhenli
 * @since 2019-07-07 11:55
 */
public class LoginPresenter extends RxPresenter<LoginContract.View> implements LoginContract.Presenter<LoginContract.View> {
    private static final String TAG = "LoginPresenter";

    @Inject
    public LoginPresenter() {

    }

    @Override
    public void login(String phone, String password) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("phone", "+86" + phone);
        params.put("password", password);
        params.put("app_version", AppUtils.getFormatVersionCode());
        params.put("unique_id", AppUtils.getFormatDeviceUnique() );

//        DisposableObserver disposable = RxUtil.subscribe(mApi.getLoginData(params), new SampleProgressObserver<BaseBean>(mView) {
//
//            @Override
//            public void onNext(BaseBean baseBean) {
//
//            }
//
//        });
//        addSubscribe(disposable);
    }
}

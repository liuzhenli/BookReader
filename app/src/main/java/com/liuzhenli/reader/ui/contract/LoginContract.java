package com.liuzhenli.reader.ui.contract;

import com.liuzhenli.common.base.BaseBean;
import com.liuzhenli.common.base.BaseContract;

/**
 * @author Liuzhenli
 * @since 2019-07-07 11:53
 */
public class LoginContract {
    public interface View extends BaseContract.BaseView {
        void showData(BaseBean data);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void login(String phone,String password);
    }
}

package com.micoredu.reader.ui.contract;

import com.liuzhenli.common.base.BaseContract;

/**
 * Description:
 *
 * @author liuzhenli 2020/11/18
 * Email: 848808263@qq.com
 */
public class TestSourceContract {
    public interface View extends BaseContract.BaseView {
    }

    ;

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
    }
}

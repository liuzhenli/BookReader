package com.liuzhenli.reader.ui.presenter;

import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.reader.ui.contract.TestSourceContract;

import javax.inject.Inject;

/**
 * Description:
 *
 * @author liuzhenli 2020/11/18
 * Email: 848808263@qq.com
 */
public class TestSourcePresenter extends RxPresenter<TestSourceContract.View> implements TestSourceContract.Presenter<TestSourceContract.View> {
    @Inject
    public TestSourcePresenter() {
    }
}

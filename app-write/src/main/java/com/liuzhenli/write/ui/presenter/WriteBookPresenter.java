package com.liuzhenli.write.ui.presenter;


import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.write.ui.contract.WriteBookContract;

import javax.inject.Inject;

/**
 * Description:
 *
 * @author liuzhenli 2021/1/20
 * Email: 848808263@qq.com
 */
public class WriteBookPresenter extends RxPresenter<WriteBookContract.View> implements WriteBookContract.Presenter<WriteBookContract.View> {
    @Inject
    public WriteBookPresenter() {
    }

    @Override
    public void getLocalData() {

    }
}

package com.micoredu.reader.ui.presenter;


import com.liuzhenli.common.base.RxPresenter;
import com.micoredu.reader.ui.contract.DownloadContract;

import javax.inject.Inject;

/**
 * Description:
 *
 * @author liuzhenli 2020/11/22
 * Email: 848808263@qq.com
 */
public class DownloadPresenter extends RxPresenter<DownloadContract.View> implements DownloadContract.Presenter <DownloadContract.View>{

    @Inject
    public DownloadPresenter() {
    }
}

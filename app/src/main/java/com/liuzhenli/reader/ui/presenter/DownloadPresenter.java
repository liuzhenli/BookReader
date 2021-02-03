package com.liuzhenli.reader.ui.presenter;

import com.liuzhenli.reader.base.RxPresenter;
import com.liuzhenli.reader.ui.contract.DownloadContract;
import com.liuzhenli.reader.ui.contract.HistoryContract;

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

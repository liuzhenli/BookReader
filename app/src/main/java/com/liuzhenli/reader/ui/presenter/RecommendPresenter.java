package com.liuzhenli.reader.ui.presenter;

import com.liuzhenli.reader.base.RxPresenter;
import com.liuzhenli.reader.network.Api;
import com.liuzhenli.reader.ui.contract.RecommendContract;

import javax.inject.Inject;

/**
 * Description:
 *
 * @author liuzhenli 2020/9/12
 * Email: 848808263@qq.com
 */
public class RecommendPresenter extends RxPresenter<RecommendContract.View> implements RecommendContract.Presenter<RecommendContract.View> {

    private Api mApi;

    @Inject
    public RecommendPresenter(Api api) {
        this.mApi = api;
    }


    @Override
    public void getSource() {

    }
}

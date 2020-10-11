package com.liuzhenli.reader.ui.presenter;

import com.liuzhenli.reader.base.RxPresenter;
import com.liuzhenli.reader.network.Api;
import com.liuzhenli.reader.ui.contract.BookSiteContract;

import javax.inject.Inject;

/**
 * Description:
 *
 * @author liuzhenli 2020/10/9
 * Email: 848808263@qq.com
 */
public class BookSitePresenter extends RxPresenter<BookSiteContract.View> implements BookSiteContract.Presenter<BookSiteContract.View> {
    private Api mApi;

    @Inject
    public BookSitePresenter(Api mApi) {
        this.mApi = mApi;
    }

    @Override
    public void getBookList() {

    }
}

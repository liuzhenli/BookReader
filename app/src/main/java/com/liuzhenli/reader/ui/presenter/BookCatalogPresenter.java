package com.liuzhenli.reader.ui.presenter;

import com.liuzhenli.reader.base.RxPresenter;
import com.liuzhenli.reader.network.Api;
import com.liuzhenli.reader.ui.contract.BookCatalogContract;
import com.liuzhenli.reader.ui.contract.ReadContract;

import javax.inject.Inject;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-12-14 11:00
 */
public class BookCatalogPresenter extends RxPresenter<BookCatalogContract.View> implements BookCatalogContract.Presenter<BookCatalogContract.View> {

    private Api mApi;

    @Inject
    public BookCatalogPresenter(Api api) {
        this.mApi = api;
    }
}

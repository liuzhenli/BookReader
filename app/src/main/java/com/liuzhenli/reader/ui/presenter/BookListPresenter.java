package com.liuzhenli.reader.ui.presenter;

import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.reader.base.RxPresenter;
import com.liuzhenli.reader.network.Api;
import com.liuzhenli.reader.observer.SampleProgressObserver;
import com.liuzhenli.reader.ui.contract.BookListContract;
import com.liuzhenli.common.utils.LogUtils;
import com.micoredu.readerlib.bean.SearchBookBean;
import com.micoredu.readerlib.model.WebBookModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

/**
 * Description:
 *
 * @author liuzhenli 2020/10/9
 * Email: 848808263@qq.com
 */
public class BookListPresenter extends RxPresenter<BookListContract.View> implements BookListContract.Presenter<BookListContract.View> {
    private Api mApi;

    @Inject
    public BookListPresenter(Api mApi) {
        this.mApi = mApi;
    }

    @Override
    public void getBookList(String url, int page, String tag) {
        addSubscribe(RxUtil.subscribe(WebBookModel.getInstance().findBook(url, page, tag), new SampleProgressObserver<List<SearchBookBean>>(mView) {
            @Override
            public void onNext(@NotNull List<SearchBookBean> searchBookBeans) {
                mView.showBookList(searchBookBeans);
            }
        }));
    }
}

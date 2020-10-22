package com.liuzhenli.reader.ui.presenter;

import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.reader.base.RxPresenter;
import com.liuzhenli.reader.network.Api;
import com.liuzhenli.reader.observer.SampleProgressObserver;
import com.liuzhenli.reader.ui.contract.BookCategoryContract;
import com.liuzhenli.reader.utils.LogUtils;
import com.micoredu.readerlib.bean.SearchBookBean;
import com.micoredu.readerlib.model.WebBookModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

/**
 * Description:
 *
 * @author liuzhenli 2020/10/9
 * Email: 848808263@qq.com
 */
public class BookCategoryPresenter extends RxPresenter<BookCategoryContract.View> implements BookCategoryContract.Presenter<BookCategoryContract.View> {
    private Api mApi;

    @Inject
    public BookCategoryPresenter(Api mApi) {
        this.mApi = mApi;
    }

    @Override
    public void getBookList(String url, int page, String tag) {
        LogUtils.e("1111111", url + "\n" + tag);
        DisposableObserver subscribe = RxUtil.subscribe(WebBookModel.getInstance().findBook(url, page, tag), new SampleProgressObserver<List<SearchBookBean>>(mView) {
            @Override
            public void onNext(List<SearchBookBean> searchBookBeans) {
                mView.showBookList(searchBookBeans);
            }
        });
        addSubscribe(subscribe);
    }
}

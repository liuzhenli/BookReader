/*
package com.liuzhenli.reader.ui.presenter;

import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.common.observer.SampleProgressObserver;
import com.liuzhenli.reader.ui.contract.BookListContract;
import com.liuzhenli.common.utils.ThreadUtils;
import com.micoredu.reader.model.WebBookModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

*/
/**
 * Description:
 *
 * @author liuzhenli 2020/10/9
 * Email: 848808263@qq.com
 *//*

public class BookListPresenter extends RxPresenter<BookListContract.View> implements BookListContract.Presenter<BookListContract.View> {

    @Inject
    public BookListPresenter() {

    }

    @Override
    public void getBookList(String url, int page, String tag) {
        addSubscribe(RxUtil.subscribe(WebBookModel.getInstance().findBook(url, page, tag), new SampleProgressObserver<List<SearchBook>>(mView) {
            @Override
            public void onNext(@NotNull List<SearchBook> searchBookBeans) {
                //搜索结果存入数据库
                ThreadUtils.getInstance().getExecutorService().execute(() ->
                        AppReaderDbHelper.getInstance().getDatabase().getSearchBookDao().insertOrReplaceInTx(searchBookBeans));
                mView.showBookList(searchBookBeans);
            }
        }));
    }
}
*/

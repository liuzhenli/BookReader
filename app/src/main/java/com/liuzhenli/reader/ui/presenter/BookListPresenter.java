package com.liuzhenli.reader.ui.presenter;

import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.reader.network.Api;
import com.liuzhenli.common.observer.SampleProgressObserver;
import com.liuzhenli.reader.ui.contract.BookListContract;
import com.liuzhenli.reader.utils.ThreadUtils;
import com.micoredu.readerlib.bean.SearchBookBean;
import com.micoredu.readerlib.helper.DbHelper;
import com.micoredu.readerlib.model.WebBookModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

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
                //搜索结果存入数据库
                ThreadUtils.getInstance().getExecutorService().execute(() ->
                        DbHelper.getDaoSession().getSearchBookBeanDao().insertOrReplaceInTx(searchBookBeans));
                mView.showBookList(searchBookBeans);
            }
        }));
    }
}

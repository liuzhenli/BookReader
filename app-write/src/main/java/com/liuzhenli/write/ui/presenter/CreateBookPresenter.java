package com.liuzhenli.write.ui.presenter;

import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.common.observer.SampleProgressObserver;
import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.write.bean.WriteBook;
import com.liuzhenli.write.helper.WriteBookHelper;
import com.liuzhenli.write.ui.contract.CreateBookContract;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Description:
 *
 * @author liuzhenli 2021/5/14
 * Email: 848808263@qq.com
 */
public class CreateBookPresenter extends RxPresenter<CreateBookContract.View> implements CreateBookContract.Presenter<CreateBookContract.View> {

    @Inject
    public CreateBookPresenter() {
    }

    @Override
    public void getCreateBooks() {
        addSubscribe(RxUtil.subscribe(Observable.create(new ObservableOnSubscribe<List<WriteBook>>() {
            @Override
            public void subscribe(@NotNull ObservableEmitter<List<WriteBook>> emitter) throws Exception {
                List<WriteBook> allBooks = WriteBookHelper.getAllBooks();
                if (allBooks == null) {
                    allBooks = new ArrayList<>();
                }
                WriteBook writeBook = new WriteBook();
                writeBook.bookName = "新建一本书";
                allBooks.add(0, writeBook);

                emitter.onNext(allBooks);
            }
        }), new SampleProgressObserver<List<WriteBook>>() {
            @Override
            public void onNext(@NotNull List<WriteBook> writeBooks) {

                mView.showAllCreateBooks(writeBooks);
            }
        }));
    }
}

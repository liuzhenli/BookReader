package com.liuzhenli.write.ui.presenter;

import com.liuzhenli.common.base.BaseBean;
import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.common.observer.SampleProgressObserver;
import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.write.bean.WriteBook;
import com.liuzhenli.write.helper.WriteBookHelper;
import com.liuzhenli.write.ui.contract.EditBookContract;

import org.jetbrains.annotations.NotNull;

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
public class EditBookPresenter extends RxPresenter<EditBookContract.View> implements EditBookContract.Presenter<EditBookContract.View> {

    @Inject
    public EditBookPresenter() {
    }

    @Override
    public void saveBooks(WriteBook book) {
        addSubscribe(RxUtil.subscribe(Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(@NotNull ObservableEmitter<Long> emitter) throws Exception {
                long l = WriteBookHelper.saveWriteBook(book);
                emitter.onNext(l);
            }
        }), new SampleProgressObserver<Long>() {
            @Override
            public void onNext(@NotNull Long writeBooks) {
                mView.showSaveResult(writeBooks);
            }
        }));
    }
}

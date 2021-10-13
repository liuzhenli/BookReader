package com.micoredu.reader.ui.presenter;

import androidx.annotation.NonNull;

import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.common.observer.SampleProgressObserver;
import com.liuzhenli.common.utils.RxUtil;
import com.micoredu.reader.bean.ReadHistory;
import com.micoredu.reader.helper.AppReaderDbHelper;
import com.micoredu.reader.ui.contract.ReadHistoryContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Description:
 *
 * @author liuzhenli 2021/10/12
 * Email: 848808263@qq.com
 */
public class ReadHistoryPresenter extends RxPresenter<ReadHistoryContract.View> implements ReadHistoryContract.Presenter<ReadHistoryContract.View> {
    @Inject
    public ReadHistoryPresenter() {
    }

    @Override
    public void getHistory() {
        addSubscribe(Observable.create(new ObservableOnSubscribe<List<ReadHistory>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<ReadHistory>> emitter) throws Exception {
                List<ReadHistory> all = AppReaderDbHelper.getInstance().getDatabase().getReadHistoryDao().getAllGroupByBookName();
                emitter.onNext(all);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new SampleProgressObserver<List<ReadHistory>>() {
            @Override
            public void onNext(@NonNull List<ReadHistory> readHistories) {
                mView.showHistory(readHistories);
            }
        }));
    }

    @Override
    public void deleteItem(int position) {

    }
}

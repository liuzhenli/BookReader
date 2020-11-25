package com.liuzhenli.reader.ui.presenter;

import android.text.TextUtils;

import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.reader.base.RxPresenter;
import com.liuzhenli.reader.observer.SampleProgressObserver;
import com.liuzhenli.reader.ui.contract.EditSourceContract;
import com.micoredu.readerlib.bean.BookSourceBean;
import com.micoredu.readerlib.model.BookSourceManager;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Description:
 *
 * @author liuzhenli 2020/11/16
 * Email: 848808263@qq.com
 */
public class EditSourcePresenter extends RxPresenter<EditSourceContract.View> implements EditSourceContract.Presenter<EditSourceContract.View> {
    @Inject
    public EditSourcePresenter() {
    }

    @Override
    public void saveBookSource(BookSourceBean data) {
        Observable<Boolean> observable = Observable.create(emitter -> {
            //发现数据默认可见
            if (data != null && !TextUtils.isEmpty(data.getRuleFindUrl())) {
                data.setRuleFindEnable(true);
            }
            BookSourceManager.saveBookSource(data);
            emitter.onNext(true);
        });
        addSubscribe(RxUtil.subscribe(observable, new SampleProgressObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                mView.showSaveBookResult();
            }
        }));
    }
}

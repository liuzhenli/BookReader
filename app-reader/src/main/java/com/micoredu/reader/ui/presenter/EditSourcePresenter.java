package com.micoredu.reader.ui.presenter;

import android.text.TextUtils;

import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.common.observer.SampleProgressObserver;
import com.micoredu.reader.bean.BookSourceBean;
import com.micoredu.reader.model.BookSourceManager;
import com.micoredu.reader.ui.contract.EditSourceContract;

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

    @Override
    public void getBookSourceFromString(String str) {
        BookSourceBean bookSourceBean = BookSourceManager.matchSourceBean(str);
        mView.showBookSource(bookSourceBean);
    }


}

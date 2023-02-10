package com.micoredu.reader.ui.presenter;

import android.text.TextUtils;

import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.common.observer.SampleProgressObserver;
import com.micoredu.reader.bean.BookSource;
import com.micoredu.reader.help.source.SourceHelp;
import com.micoredu.reader.ui.contract.EditSourceContract;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Description:
 *
 * @author liuzhenli 2020/11/16
 * Email: 848808263@qq.com
 */

/*
public class EditSourcePresenter extends RxPresenter<EditSourceContract.View> implements EditSourceContract.Presenter<EditSourceContract.View> {
    @Inject
    public EditSourcePresenter() {
    }

    @Override
    public void saveBookSource(BookSource data) {
        Observable<Boolean> observable = Observable.create(emitter -> {
            //发现数据默认可见
            if (data != null && !TextUtils.isEmpty(data.getExploreUrl())) {
                data.setEnabledExplore(true);
            }
            SourceHelp.INSTANCE.insertBookSource(data);
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
        BookSource bookSource = (BookSource) BookSource.Companion.fromJson(str);
        mView.showBookSource(bookSource);
    }
}

 */

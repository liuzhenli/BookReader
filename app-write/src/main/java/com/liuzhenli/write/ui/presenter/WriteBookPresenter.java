package com.liuzhenli.write.ui.presenter;


import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.common.observer.SampleProgressObserver;
import com.liuzhenli.common.utils.L;
import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.write.bean.WriteChapter;
import com.liuzhenli.write.helper.WriteBookHelper;
import com.liuzhenli.write.ui.contract.WriteBookContract;
import com.liuzhenli.write.util.WriteChapterManager;
import com.liuzhenli.write.util.WriteHistoryManager;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Description:
 *
 * @author liuzhenli 2021/1/20
 * Email: 848808263@qq.com
 */
public class WriteBookPresenter extends RxPresenter<WriteBookContract.View> implements WriteBookContract.Presenter<WriteBookContract.View> {
    @Inject
    public WriteBookPresenter() {
    }

    @Override
    public void getLocalData() {

    }

    @Override
    public void saveChapterInfo(WriteChapter chapter) {
        addSubscribe(RxUtil.subscribe(Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NotNull ObservableEmitter<Boolean> emitter) throws Exception {
                WriteBookHelper.saveChapterInfo(chapter);
                WriteHistoryManager.getInstance(chapter.getLocalBookId(), chapter.getId());
                emitter.onNext(true);
            }
        }), new SampleProgressObserver<Boolean>() {
            @Override
            public void onNext(@NotNull Boolean result) {
                mView.showSaveChapterInfoResult(result);
            }
        }));
    }


    @Override
    public void autoSaveDraft(WriteChapter chapter, String content, String path) {
        addSubscribe(RxUtil.subscribe(Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NotNull ObservableEmitter<Boolean> emitter) throws Exception {
                WriteBookHelper.saveChapterInfo(chapter);
                WriteHistoryManager.getInstance(chapter.getLocalBookId(), chapter.getId()).saveHistory(content, chapter.getWordCount());
                emitter.onNext(true);
            }
        }), new SampleProgressObserver<Boolean>() {
            @Override
            public void onNext(@NotNull Boolean result) {
                mView.showSaveChapterInfoResult(result);
            }
        }));
    }
}

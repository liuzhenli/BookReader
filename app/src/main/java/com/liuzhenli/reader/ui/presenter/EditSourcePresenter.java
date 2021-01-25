package com.liuzhenli.reader.ui.presenter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.reader.base.RxPresenter;
import com.liuzhenli.reader.observer.SampleProgressObserver;
import com.liuzhenli.reader.ui.contract.EditSourceContract;
import com.micoredu.readerlib.bean.BookSource3Bean;
import com.micoredu.readerlib.bean.BookSourceBean;
import com.micoredu.readerlib.model.BookSourceManager;

import java.util.List;

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
        BookSourceBean bookSourceBean = matchSourceBean(str);
        mView.showBookSource(bookSourceBean);
    }


    private BookSourceBean matchSourceBean(String str) {
        Gson gson = new Gson();
        BookSource3Bean bookSource3Bean = new BookSource3Bean();
        BookSourceBean bookSource2Bean = new BookSourceBean();
        int r2 = 0, r3 = 0;
        boolean b = str.charAt(0) == '[' && str.charAt(str.length() - 1) == ']';
        try {
            if (b) {
                List<BookSource3Bean> list = gson.fromJson(str, new TypeToken<List<BookSource3Bean>>() {
                }.getType());
                bookSource3Bean = list.get(0);
            } else {
                bookSource3Bean = gson.fromJson(str, BookSource3Bean.class);
            }
            r3 = gson.toJson(bookSource3Bean).length();
        } catch (Exception ignore) {
        }

        try {
            if (b) {
                List<BookSourceBean> list = gson.fromJson(str, new TypeToken<List<BookSourceBean>>() {
                }.getType());
                bookSource2Bean = list.get(0);
            } else {
                bookSource2Bean = gson.fromJson(str, BookSourceBean.class);
            }
            r2 = gson.toJson(bookSource2Bean).length();
            // r2 r3的计算在调用searchUrl2RuleSearchUrl() 等高级转换方法之前，是简化算法的粗糙的做法
            if (r2 > r3)
                return bookSource2Bean;
        } catch (Exception ignore) {
        }

        if (r3 > 0) {
            return bookSource3Bean.addGroupTag("阅读3.0书源").toBookSourceBean();
        }
        return bookSource2Bean;
    }
}

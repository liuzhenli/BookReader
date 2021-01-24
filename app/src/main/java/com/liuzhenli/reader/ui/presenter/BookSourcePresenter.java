package com.liuzhenli.reader.ui.presenter;

import android.content.Context;
import android.text.TextUtils;

import androidx.documentfile.provider.DocumentFile;

import com.google.android.material.snackbar.Snackbar;
import com.liuzhenli.common.exception.ApiException;
import com.liuzhenli.common.utils.GsonUtils;
import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.common.utils.StringUtils;
import com.liuzhenli.reader.ReaderApplication;
import com.liuzhenli.reader.base.RxPresenter;
import com.liuzhenli.reader.network.Api;
import com.liuzhenli.reader.observer.SampleProgressObserver;
import com.liuzhenli.reader.service.CheckSourceService;
import com.liuzhenli.reader.ui.contract.BookSourceContract;
import com.liuzhenli.reader.utils.ApiManager;
import com.liuzhenli.reader.utils.ThreadUtils;
import com.micoredu.readerlib.bean.BookSourceBean;
import com.micoredu.readerlib.helper.DocumentHelper;
import com.micoredu.readerlib.model.BookSourceManager;
import com.microedu.reader.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

import static android.text.TextUtils.isEmpty;


/**
 * Description:
 *
 * @author liuzhenli 2020/11/9
 * Email: 848808263@qq.com
 */
public class BookSourcePresenter extends RxPresenter<BookSourceContract.View> implements BookSourceContract.Presenter<BookSourceContract.View> {
    private Api mApi;

    @Inject
    public BookSourcePresenter(Api api) {
        this.mApi = api;
    }

    @Override
    public void getLocalBookSource(String key) {

        addSubscribe(RxUtil.subscribe(Observable.create(emitter -> {
            //获取全部书源
            if (TextUtils.isEmpty(key)) {
                emitter.onNext(BookSourceManager.getAllBookSource());
            } else if (TextUtils.equals("enable", key)) {
                emitter.onNext(BookSourceManager.getSelectedBookSource());
            } else {
                emitter.onNext(BookSourceManager.getSourceByKey(key));
            }

        }), new SampleProgressObserver<List<BookSourceBean>>() {
            @Override
            public void onNext(List<BookSourceBean> list) {
                mView.showLocalBookSource(list);
            }
        }));
    }

    @Override
    public void setEnable(BookSourceBean bookSource, boolean enable) {

    }

    @Override
    public void setTop(BookSourceBean bookSource, boolean enable) {
        addSubscribe(RxUtil.subscribe(Observable.create(emitter -> {
            BookSourceManager.toTop(bookSource);
            //get all of the book sources
            emitter.onNext(BookSourceManager.getAllBookSource());
        }), new SampleProgressObserver<List<BookSourceBean>>() {
            @Override
            public void onNext(List<BookSourceBean> list) {
                mView.showLocalBookSource(list);
            }
        }));
    }

    @Override
    public void getNetSource(String url) {
        ApiManager.getInstance().settBookSource(url);
        addSubscribe(RxUtil.subscribe(mApi.getBookSource(""), new SampleProgressObserver<ResponseBody>(mView) {
            @Override
            public void onNext(ResponseBody data) {
                configNetBookSource(data);
            }
        }));
    }

    @Override
    public void deleteSelectedSource(List<BookSourceBean> bookSourceBeans) {
        addSubscribe(RxUtil.subscribe(Observable.create(emitter -> {
            if (bookSourceBeans == null || bookSourceBeans.size() == 0) {
                return;
            }
            for (BookSourceBean bookSourceBean : bookSourceBeans) {
                BookSourceManager.deleteBookSource(bookSourceBean);
            }
            emitter.onNext(bookSourceBeans.size());
        }), new SampleProgressObserver<Integer>() {
            @Override
            public void onNext(Integer aBoolean) {
                mView.shoDeleteBookSourceResult();
            }
        }));
    }

    @Override
    public void checkBookSource(Context context, List<BookSourceBean> selectedBookSource) {
        CheckSourceService.start(context, selectedBookSource);
    }

    @Override
    public void loadBookSourceFromFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            mView.showError(new ApiException(1000, new Throwable(ReaderApplication.getInstance().getString(R.string.read_file_error))));
            return;
        }
        String json;
        DocumentFile file;
        try {
            file = DocumentFile.fromFile(new File(filePath));
        } catch (Exception e) {
            mView.showError(new ApiException(1000, new Throwable(ReaderApplication.getInstance().getString(R.string.can_not_open))));
            return;
        }
        json = DocumentHelper.readString(file);
        if (!isEmpty(json)) {
            Observable<List<BookSourceBean>> observable = BookSourceManager.importSource(json);
            if (observable != null) {
                RxUtil.subscribe(observable, new SampleProgressObserver<List<BookSourceBean>>(mView) {
                    @Override
                    public void onNext(@NonNull List<BookSourceBean> bookSource) {
                        mView.showLocalBookSource(bookSource);
                    }
                });
            } else {
                mView.showError(new ApiException(1000, new Throwable(ReaderApplication.getInstance().getString(R.string.type_un_correct))));
            }
        } else {
            mView.showError(new ApiException(1000, new Throwable(ReaderApplication.getInstance().getString(R.string.read_file_error))));
        }
    }

    public void saveData(List<BookSourceBean> data) {
        addSubscribe(RxUtil.subscribe(Observable.create(emitter -> {
            ThreadUtils.getInstance().getExecutorService().execute(() -> BookSourceManager.saveBookSource(data));
            emitter.onNext(data == null ? 0 : data.size());
        }), new SampleProgressObserver<Integer>() {
            @Override
            public void onNext(Integer aBoolean) {
            }
        }));

    }

    public void saveData(BookSourceBean data) {
        ThreadUtils.getInstance().getExecutorService().execute(() -> BookSourceManager.saveBookSource(data));
    }

    public void delData(BookSourceBean data) {
        ThreadUtils.getInstance().getExecutorService().execute(() -> BookSourceManager.removeBookSource(data));
    }

    private void configNetBookSource(ResponseBody data) {
        Observable<List<BookSourceBean>> listObservable = Observable.create(emitter -> {
            List<BookSourceBean> bookSourceList = new ArrayList<>();
            byte[] bytes = new byte[0];
            try {
                bytes = data.bytes();
                String s = new String(bytes);
                if (StringUtils.isJsonArray(s)) {
                    bookSourceList = GsonUtils.parseJArray(s, BookSourceBean.class);
                }
                //发现数据默认可见
                for (int i = 0; i < bookSourceList.size(); i++) {
                    if (!TextUtils.isEmpty(bookSourceList.get(i).getRuleFindUrl())) {
                        bookSourceList.get(i).setRuleFindEnable(true);
                    }
                }
                //存入数据库
                BookSourceManager.addBookSource(bookSourceList);
                //有发现项的书源
                emitter.onNext(bookSourceList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        addSubscribe(RxUtil.subscribe(listObservable, new SampleProgressObserver<List<BookSourceBean>>() {
            @Override
            public void onNext(List<BookSourceBean> bookSourceList) {
                mView.showAddNetSourceResult(bookSourceList);
            }
        }));
    }
}

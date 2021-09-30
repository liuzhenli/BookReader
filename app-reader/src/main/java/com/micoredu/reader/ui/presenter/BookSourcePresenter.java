package com.micoredu.reader.ui.presenter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;

import com.liuzhenli.common.BaseApplication;
import com.liuzhenli.common.exception.ApiException;
import com.liuzhenli.common.gson.GsonUtils;
import com.liuzhenli.common.utils.DocumentUtil;
import com.liuzhenli.common.utils.FileUtils;
import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.common.utils.StringUtils;
import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.common.observer.SampleProgressObserver;
import com.micoredu.reader.R;
import com.micoredu.reader.network.ReaderApi;
import com.micoredu.reader.service.CheckSourceService;
import com.micoredu.reader.ui.contract.BookSourceContract;
import com.liuzhenli.common.utils.ApiManager;
import com.liuzhenli.common.utils.ThreadUtils;
import com.micoredu.reader.bean.BookSourceBean;
import com.micoredu.reader.helper.DocumentHelper;
import com.micoredu.reader.model.BookSourceManager;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import kotlin.text.Charsets;
import okhttp3.ResponseBody;

import static android.text.TextUtils.isEmpty;


/**
 * Description:BookSourceActivity Presenter
 *
 * @author liuzhenli 2020/11/9
 * Email: 848808263@qq.com
 */
public class BookSourcePresenter extends RxPresenter<BookSourceContract.View> implements BookSourceContract.Presenter<BookSourceContract.View> {
    private ReaderApi mApi;

    @Inject
    public BookSourcePresenter(ReaderApi mApi) {
        this.mApi = mApi;
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
            public void onNext(@NotNull List<BookSourceBean> list) {
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
        ApiManager.getInstance().setBookSource(url);
        addSubscribe(RxUtil.subscribe(mApi.getBookSource(""), new SampleProgressObserver<ResponseBody>(mView) {
            @Override
            public void onNext(@androidx.annotation.NonNull ResponseBody data) {
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
            public void onNext(@androidx.annotation.NonNull Integer aBoolean) {
                mView.shoDeleteBookSourceResult();
            }
        }));
    }

    @Override
    public void checkBookSource(Context context, List<BookSourceBean> selectedBookSource) {
        CheckSourceService.start(context, selectedBookSource);
    }

    /**
     * @param uri 1./storage/emulated/0/Documents/ShuFang/ShuFang/Backups/myBookSource.json
     *            2.content://com.android.externalstorage.documents/document/primary:Documents/ShuFang/ShuFang/Backups/myBookSource.json
     */
    @Override
    public void loadBookSourceFromFile(@NonNull Uri uri) {

        if (TextUtils.isEmpty(uri.getPath())) {
            mView.showError(new ApiException(1000, new Throwable(BaseApplication.getInstance().getString(R.string.read_file_error))));
            return;
        }
        String json;
        DocumentFile file;
        try {
            if (FileUtils.isContentFile(uri)) {
                json = new String(DocumentUtil.readBytes(BaseApplication.getInstance(), uri), Charsets.UTF_8);
            } else {
                file = DocumentFile.fromFile(new File(uri.toString()));
                json = DocumentHelper.readString(file);
            }
        } catch (Exception e) {
            mView.showError(new ApiException(1000, new Throwable(BaseApplication.getInstance().getString(R.string.can_not_open))));
            return;
        }

        if (!isEmpty(json)) {
            importSource(json);
        } else {
            mView.showError(new ApiException(1000, new Throwable(BaseApplication.getInstance().getString(R.string.read_file_error))));
        }
    }


    public void importSource(String string) {
        Observable<List<BookSourceBean>> observable = BookSourceManager.importSource(string);
        if (observable != null) {
            RxUtil.subscribe(observable, new SampleProgressObserver<List<BookSourceBean>>(mView) {
                @Override
                public void onNext(@NonNull List<BookSourceBean> bookSource) {
                    mView.showLocalBookSource(bookSource);
                }
            });
        } else {
            mView.showError(new ApiException(1000, new Throwable(BaseApplication.getInstance().getString(R.string.type_un_correct))));
        }
    }

    public void saveData(List<BookSourceBean> data) {
        addSubscribe(RxUtil.subscribe(Observable.create(emitter -> {
            ThreadUtils.getInstance().getExecutorService().execute(() -> BookSourceManager.update(data));
            emitter.onNext(data == null ? 0 : data.size());
        }), new SampleProgressObserver<Integer>() {
            @Override
            public void onNext(@NotNull Integer aBoolean) {
            }
        }));

    }

    public void saveData(BookSourceBean data) {
        ThreadUtils.getInstance().getExecutorService().execute(() -> BookSourceManager.update(data));
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

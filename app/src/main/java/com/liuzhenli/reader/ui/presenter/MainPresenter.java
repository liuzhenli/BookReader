package com.liuzhenli.reader.ui.presenter;

import static android.text.TextUtils.isEmpty;
import static com.liuzhenli.common.utils.Constant.FONT_PATH;

import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;

import com.liuzhenli.common.BaseApplication;
import com.liuzhenli.common.FileHelp;
import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.common.exception.ApiException;
import com.liuzhenli.common.observer.SampleProgressObserver;
import com.liuzhenli.common.utils.AppSharedPreferenceHelper;
import com.liuzhenli.common.utils.DeviceUtil;
import com.liuzhenli.common.utils.DocumentUtil;
import com.liuzhenli.common.utils.FileUtils;
import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.common.utils.ThreadUtils;
import com.liuzhenli.reader.ReaderApplication;
import com.liuzhenli.reader.ui.contract.MainContract;
import com.liuzhenli.reader.utils.storage.Backup;
import com.liuzhenli.reader.utils.storage.WebDavHelp;
import com.micoredu.reader.bean.BookSourceBean;
import com.micoredu.reader.helper.DocumentHelper;
import com.micoredu.reader.model.BookSourceManager;
import com.microedu.reader.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import kotlin.text.Charsets;


/**
 * Description:
 *
 * @author liuzhenli 2021/7/22
 * Email: 848808263@qq.com
 */
public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter<MainContract.View> {
    @Inject
    public MainPresenter() {
    }

    @Override
    public void checkBackupPath() {
        boolean isEmpty;
        String backupPath = AppSharedPreferenceHelper.getBackupPath("");
        if (DeviceUtil.isLaterQ()) {
            isEmpty = TextUtils.isEmpty(backupPath);
        } else {
            isEmpty = false;
        }
        mView.showCheckBackupPathResult(isEmpty);
    }

    @Override
    public void checkWebDav() {
        mView.showWebDavResult(WebDavHelp.INSTANCE.initWebDav());
    }

    @Override
    public void dealDefaultFonts() {
        ThreadUtils.getInstance().getExecutorService().execute(() -> {
            try {
                FileUtils.copyFromAssets(ReaderApplication.getInstance().getAssets(), "fonts/xwkt.ttf", FONT_PATH + File.separator + "霞鹜文楷.ttf", true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void importSource(String url) {
        Observable<List<BookSourceBean>> observable = BookSourceManager.importSource(url);
        if (observable != null) {
            RxUtil.subscribe(observable, new SampleProgressObserver<List<BookSourceBean>>(mView) {
                @Override
                public void onNext(@NonNull List<BookSourceBean> bookSource) {
                    mView.showBookSource(bookSource);
                }
            });
        } else {
            mView.showError(new ApiException(1000, new Throwable(BaseApplication.getInstance().getString(com.micoredu.reader.R.string.type_un_correct))));
        }
    }

    @Override
    public void importSourceFromLocal(@NonNull Uri uri) {
        if (TextUtils.isEmpty(uri.getPath())) {
            mView.showError(new ApiException(1000, new Throwable(BaseApplication.getInstance().getString(com.micoredu.reader.R.string.read_file_error))));
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
            mView.showError(new ApiException(1000, new Throwable(BaseApplication.getInstance().getString(com.micoredu.reader.R.string.can_not_open))));
            return;
        }

        if (!isEmpty(json)) {
            importSource(json);
        } else {
            mView.showError(new ApiException(1000, new Throwable(BaseApplication.getInstance().getString(com.micoredu.reader.R.string.read_file_error))));
        }

    }
}

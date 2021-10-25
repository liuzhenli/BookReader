package com.liuzhenli.reader.ui.presenter;

import com.liuzhenli.common.utils.DocumentUtil;
import com.liuzhenli.common.utils.L;
import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.common.observer.SampleProgressObserver;
import com.liuzhenli.reader.ReaderApplication;
import com.liuzhenli.common.utils.filepicker.entity.FileItem;
import com.liuzhenli.reader.ui.contract.LocalFileContract;
import com.liuzhenli.common.utils.Constant;
import com.liuzhenli.common.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.liuzhenli.common.utils.Constant.FileSuffix.EPUB;
import static com.liuzhenli.common.utils.Constant.FileSuffix.PDF;
import static com.liuzhenli.common.utils.Constant.FileSuffix.TXT;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.documentfile.provider.DocumentFile;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-12-15 10:29
 */
public class LocalFilePresenter extends RxPresenter<LocalFileContract.View> implements LocalFileContract.Presenter<LocalFileContract.View> {

    private static final String TAG = "LocalFilePresenter";

    @Inject
    public LocalFilePresenter() {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void getDirectory(DocumentFile file) {

        addSubscribe(Observable.create((ObservableOnSubscribe<ArrayList<FileItem>>) emitter -> {
            ArrayList<FileItem> fileItems = new ArrayList<>();
            if (file != null) {
                fileItems = DocumentUtil.listFile(ReaderApplication.getInstance(), file.getUri());
            }
            emitter.onNext(fileItems);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SampleProgressObserver<ArrayList<FileItem>>() {
                    @Override
                    public void onNext(@NonNull ArrayList<FileItem> fileItems) {
                        if (mView != null) {
                            mView.showDirectory(fileItems, null);
                        }
                    }
                }));

    }

    public void getDirectory(File file) {
        addSubscribe(Observable
                .create((ObservableOnSubscribe<ArrayList<FileItem>>) subscriber -> {
                    //子线程
                    try {
                        ArrayList<FileItem> data = new ArrayList<>();
                        File[] files = FileUtils.read(file);
                        int size = 0;
                        if (files != null) {
                            size = files.length;
                        }
                        try {
                            for (int i = 0; i < size; i++) {
                                FileItem localFileBean = new FileItem();
                                String fileName = files[i].getName();
                                // 文件
                                if (files[i].isDirectory()) {
                                    localFileBean.fileType = Constant.FileSuffix.DIRECTORY;
                                } else if (files[i].isFile()) {
                                    if (fileName.endsWith(TXT)) {
                                        localFileBean.fileType = TXT;
                                    } else if (fileName.endsWith(PDF)) {
                                        localFileBean.fileType = PDF;
                                    } else if (fileName.endsWith(".epub")) {
                                        localFileBean.fileType = EPUB;
                                    }
                                }
                                localFileBean.file = files[i];
                                localFileBean.name = fileName;
                                localFileBean.isSelected = false;
                                if (files[i].listFiles() != null) {// 文件夹非空
                                    localFileBean.fileCount = "(" + files[i].listFiles().length + ")";
                                } else if (files[i].isFile()) {// 是文件就不显示
                                    localFileBean.fileCount = files[i].length() + "";
                                } else {// 文件夹为空
                                    localFileBean.fileCount = "(0)";
                                }
                                data.add(localFileBean);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Collections.sort(data, (lhs, rhs) -> ((lhs.name).toLowerCase()).compareTo((rhs.name).toLowerCase()));

                        List<FileItem> temp = new ArrayList<>();
                        Iterator<FileItem> list = data.iterator();
                        while (list.hasNext()) {
                            FileItem map = list.next();
                            String fileName = map.name;
                            if (fileName != null && (TXT.endsWith(fileName) || PDF.endsWith(fileName) || Constant.FileSuffix.EPUB.endsWith(fileName))) {
                                temp.add(map);
                                list.remove();
                            }
                        }
                        data.addAll(temp);
                        subscriber.onNext(data);
                        subscriber.onComplete();
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SampleProgressObserver<ArrayList<FileItem>>() {
                    @Override
                    public void onNext(@NonNull ArrayList<FileItem> fileItems) {
                        if (mView != null) {
                            mView.showDirectory(fileItems, file);
                        }
                    }
                }));
    }
}

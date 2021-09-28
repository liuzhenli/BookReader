package com.liuzhenli.reader.ui.presenter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentActivity;

import com.liuzhenli.common.observer.SampleProgressObserver;
import com.liuzhenli.common.utils.FileUtils;
import com.liuzhenli.common.utils.media.ImportBookFileHelper;
import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.reader.ReaderApplication;
import com.liuzhenli.common.utils.filepicker.entity.FileItem;
import com.liuzhenli.reader.ui.contract.LocalTxtContract;
import com.liuzhenli.common.utils.Constant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-12-15 10:29
 */
public class LocalTxtPresenter extends RxPresenter<LocalTxtContract.View> implements LocalTxtContract.Presenter<LocalTxtContract.View> {

    @Inject
    public LocalTxtPresenter() {

    }

    @Override
    public synchronized void getLocalTxt(FragmentActivity activity) {
        addSubscribe(Observable
                .create((ObservableOnSubscribe<ArrayList<FileItem>>) emitter -> {
                    WeakReference<FragmentActivity> act = new WeakReference<>(activity);
                    ImportBookFileHelper.getBookFile(act.get(), new ImportBookFileHelper.LoadBookCallBack(act.get(), bookList -> {
                        ArrayList<FileItem> fileList = new ArrayList<>();

                        for (int i = 0; i < bookList.size(); i++) {
                            File file = bookList.get(i);
                            FileItem localFile = new FileItem();
                            localFile.file = file;
                            String fileName = file.getName();
                            localFile.name = fileName;
                            localFile.time = new Date(file.lastModified());
                            localFile.path = file.getAbsolutePath();
                            if (file.isDirectory()) {// 文件
                                localFile.fileType = Constant.FileAttr.DIRECTORY;

                            } else if (file.isFile()) {
                                localFile.fileType = Constant.FileAttr.FILE;
                                if (fileName.endsWith(Constant.FileSuffix.TXT)) {
                                    localFile.FileSuffix = Constant.FileSuffix.TXT;
                                } else if (fileName.endsWith(Constant.FileSuffix.PDF)) {
                                    localFile.FileSuffix = Constant.FileSuffix.PDF;
                                } else if (fileName.endsWith(Constant.FileSuffix.EPUB)) {
                                    localFile.FileSuffix = Constant.FileSuffix.EPUB;
                                }
                            }
                            if (file.listFiles() != null) {// 文件夹非空
                                localFile.fileCount = "(" + file.listFiles().length + ")";
                            } else if (file.isFile()) {// 是文件就不显示
                                localFile.fileCount = "(" + file.length() + ")";
                            } else {// 文件夹为空
                                localFile.fileCount = "(0)";
                            }
                            fileList.add(localFile);
                        }
                        emitter.onNext(fileList);
                        emitter.onComplete();
                    }));

                }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribeWith(new SampleProgressObserver<ArrayList<FileItem>>() {
                    @Override
                    public void onNext(@NonNull ArrayList<FileItem> fileItems) {
                        mView.showLocalTxt(fileItems);
                    }
                }));
    }

    @Override
    public synchronized void getLocalTxt(Context context, String path) {
        ArrayList<FileItem> fileList = new ArrayList<>();
        if (TextUtils.isEmpty(path)) {
            mView.showLocalTxt(fileList);
            return;
        }
        addSubscribe(Observable
                .create((ObservableOnSubscribe<ArrayList<FileItem>>) emitter -> {
                    dealData(context, path, fileList);
                    emitter.onNext(fileList);
                    emitter.onComplete();
                })
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribeWith(new SampleProgressObserver<ArrayList<FileItem>>() {
                    @Override
                    public void onNext(@NonNull ArrayList<FileItem> fileItems) {
                        mView.showLocalTxt(fileList);
                    }
                }));


    }

    private void dealData(Context context, String path, ArrayList<FileItem> fileList) {
        DocumentFile documentFile = DocumentFile.fromTreeUri(context, Uri.parse(path));
        if (documentFile != null && documentFile.isDirectory()) {
            for (DocumentFile file : documentFile.listFiles()) {
                if (file.isDirectory()) {
                    dealData(context, file.getUri().toString(), fileList);
                    //getLocalTxt(context, file.getUri().toString());
                } else {
                    addToFileItems(fileList, file);
                }
            }
        } else if (documentFile != null && documentFile.isFile() && documentFile.getName() != null) {
            addToFileItems(fileList, documentFile);
        }
    }

    private synchronized void addToFileItems(List<FileItem> fileList, DocumentFile documentFile) {
        Context context = ReaderApplication.getInstance();
        String fileName = documentFile.getName();
        if (fileName == null) {
            return;
        }

        try {
            FileUtils.createDir(Constant.LOCAL_BOOK_PATH);
            File file = FileUtils.createFile(Constant.LOCAL_BOOK_PATH + documentFile.getName());

            InputStream inputStream = context.getContentResolver().openInputStream(documentFile.getUri());
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024 * 100];
            int n = 0;
            while ((n = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, n);
                outputStream.flush();
            }
            outputStream.close();
            inputStream.close();

            FileItem localFile = new FileItem();
            localFile.path = documentFile.getUri().toString();
            localFile.file = file;
            localFile.time = new Date(file.lastModified());
            localFile.name = fileName;
            localFile.fileType = Constant.FileAttr.FILE;
            if (fileName.endsWith(Constant.FileSuffix.TXT)) {
                localFile.FileSuffix = Constant.FileSuffix.TXT;
            } else if (fileName.endsWith(Constant.FileSuffix.PDF)) {
                localFile.FileSuffix = Constant.FileSuffix.PDF;
            } else if (fileName.endsWith(Constant.FileSuffix.EPUB)) {
                localFile.FileSuffix = Constant.FileSuffix.EPUB;
            }
            fileList.add(localFile);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}

package com.liuzhenli.reader.ui.presenter;

import android.net.Uri;
import android.text.TextUtils;

import androidx.documentfile.provider.DocumentFile;

import com.liuzhenli.common.FileHelp;
import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.common.utils.AppSharedPreferenceHelper;
import com.liuzhenli.common.utils.DeviceUtil;
import com.liuzhenli.common.utils.DocumentUtil;
import com.liuzhenli.common.utils.FileUtils;
import com.liuzhenli.common.utils.ThreadUtils;
import com.liuzhenli.reader.ReaderApplication;
import com.liuzhenli.reader.ui.contract.MainContract;
import com.liuzhenli.reader.utils.storage.WebDavHelp;
import com.microedu.reader.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;


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
        /*
        ThreadUtils.getInstance().getExecutorService().execute(() -> {
            String backupPath = AppSharedPreferenceHelper.getBackupPath("");
            if (DeviceUtil.isLaterQ()) {
                if (FileUtils.isContentFile(backupPath)) {
                    InputStream inputStream = ReaderApplication.getInstance().getApplicationContext().getResources().openRawResource(R.raw.xwkt);
                    try {
                        DocumentFile documentFile = DocumentFile.fromTreeUri(ReaderApplication.getInstance(), Uri.parse(backupPath));
                        assert documentFile != null;
                        if (documentFile.findFile("/Fonts")==null){
                            DocumentFile directory = documentFile.createDirectory("/Fonts");
                            assert directory != null;
                            DocumentFile file = directory.createFile("", "霞鹜文楷.ttf");
                            DocumentUtil.writeBytes(ReaderApplication.getInstance(), inputStream.read(), file);
                        }
                        //FileUtils.createDir(backupPath + "/Fonts");
                        File file = FileHelp.createFileIfNotExist(backupPath + "/");
                        OutputStream outputStream = new FileOutputStream(file);
                        byte[] buf = new byte[1024 * 100];
                        int n;
                        while ((n = inputStream.read(buf)) > 0) {
                            outputStream.write(buf, 0, n);
                            outputStream.flush();
                        }
                        outputStream.close();
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

         */

    }
}

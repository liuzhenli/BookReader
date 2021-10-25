package com.liuzhenli.reader.ui.presenter;

import static com.liuzhenli.common.utils.Constant.FONT_PATH;

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
import com.liuzhenli.reader.utils.storage.Backup;
import com.liuzhenli.reader.utils.storage.WebDavHelp;
import com.microedu.reader.R;

import java.io.File;
import java.io.FileNotFoundException;
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
        ThreadUtils.getInstance().getExecutorService().execute(() -> {
            try {
                FileUtils.copyFromAssets(ReaderApplication.getInstance().getAssets(), "fonts/xwkt.ttf", FONT_PATH + File.separator + "霞鹜文楷.ttf", true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

package com.liuzhenli.reader.ui.presenter;

import android.text.TextUtils;

import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.common.utils.AppSharedPreferenceHelper;
import com.liuzhenli.common.utils.DeviceUtil;
import com.liuzhenli.reader.ui.contract.MainContract;
import com.liuzhenli.reader.utils.storage.Backup;
import com.liuzhenli.reader.utils.storage.WebDavHelp;

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
}

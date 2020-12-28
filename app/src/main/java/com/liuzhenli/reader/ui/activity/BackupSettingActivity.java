package com.liuzhenli.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.liuzhenli.common.utils.AppConfigManager;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.reader.base.BaseActivity;
import com.liuzhenli.reader.network.AppComponent;
import com.liuzhenli.reader.utils.storage.Backup;
import com.liuzhenli.reader.utils.storage.BackupRestoreUi;
import com.liuzhenli.reader.utils.storage.Restore;
import com.liuzhenli.reader.utils.storage.WebDavHelp;
import com.liuzhenli.reader.view.loading.DialogUtil;
import com.microedu.reader.R;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheetListItemModel;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Description: 备份与恢复
 * 此页面参考了纯纯写作的备份页面
 *
 * @author liuzhenli 2020/12/14
 * Email: 848808263@qq.com
 */
public class BackupSettingActivity extends BaseActivity implements Backup.CallBack, Restore.CallBack {

    @BindView(R.id.tv_backup_title)
    TextView mTvNetAddress;
    @BindView(R.id.tv_web_dav_account)
    TextView mTvAccount;
    @BindView(R.id.tv_web_dav_password)
    TextView mTvPassword;

    @BindView(R.id.view_setting_backup)
    View mViewNetAddress;
    @BindView(R.id.view_setting_web_dav_account)
    View mVAccount;
    @BindView(R.id.view_setting_web_dav_password)
    View mVPassword;

    /**
     * 恢复
     */
    @BindView(R.id.view_restore)
    View mVRestore;

    /**
     * 备份到本地
     */
    @BindView(R.id.view_backup_path_info)
    View mVBackup;
    /**
     * 备份到云端
     */
    @BindView(R.id.view_backup_to_web)
    View mVBackupToWeb;

    public static void start(Context context) {
        Intent intent = new Intent(context, BackupSettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_backupsetting;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void initToolBar() {
        mTvTitle.setText(getResources().getString(R.string.backup_restore));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void configViews() {
        ClickUtils.click(mViewNetAddress, o -> {
            DialogUtil.showEditTextDialog(mContext, getResources().getString(R.string.web_dav_address), "请输入网址", null, new DialogUtil.DialogActionListener() {
                @Override
                public void onClick(String s) {
                    mTvNetAddress.setText(s);
                    if (!TextUtils.isEmpty(s) && !TextUtils.isEmpty(s.trim())) {
                        AppConfigManager.saveWebDavAddress(s);
                    }
                }
            });
        });
        ClickUtils.click(mVAccount, o -> {
            DialogUtil.showEditTextDialog(mContext, getResources().getString(R.string.web_dav_account), "请输入账号", null, new DialogUtil.DialogActionListener() {
                @Override
                public void onClick(String s) {
                    mTvAccount.setText(s);
                    AppConfigManager.saveWebDavAccountName(s);
                }
            });
        });
        ClickUtils.click(mVPassword, o -> {
            DialogUtil.showEditTextDialog(mContext, getResources().getString(R.string.web_dav_password), "请输入密码", null, new DialogUtil.DialogActionListener() {
                @Override
                public void onClick(String s) {
                    AppConfigManager.saveWebDavAddPwd(s);
                }
            });
        });

        //恢复
        ClickUtils.click(mVRestore, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                restoreFile();
            }
        });
        //备份到手机
        ClickUtils.click(mVBackup, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                BackupRestoreUi.INSTANCE.backup(BackupSettingActivity.this);
            }
        });
        //备份到云端
        ClickUtils.click(mVBackupToWeb, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                BackupRestoreUi.INSTANCE.backup(BackupSettingActivity.this);
            }
        });
        mTvNetAddress.setText(AppConfigManager.getWebDavAddress());


        mTvAccount.setText(AppConfigManager.getWebDavAccountName());
        mTvPassword.setText(AppConfigManager.getWebDavAddPwd());

    }


    /**
     * 恢复
     */
    private void restoreFile() {
        WebDavHelp webDavHelp = WebDavHelp.INSTANCE;
        if (!webDavHelp.initWebDav()) {
            toast("你还配置云备份");
            showBackups(null);
            return;
        }

        Single.create((SingleOnSubscribe<ArrayList<String>>) emitter -> {
            emitter.onSuccess(WebDavHelp.INSTANCE.getWebDavFileNames());
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ArrayList<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(ArrayList<String> strings) {
                        if (strings.size() == 0) {
                            toast("没有云备份");
                        }
                        showBackups(strings);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showBackups(null);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BackupRestoreUi.INSTANCE.onActivityResult(requestCode, resultCode, data);
    }

    private void showBackups(List<String> list) {
        QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(this);
        builder.setGravityCenter(false)
                .setTitle("选择恢复的文件")
                .setAddCancelBtn(false)
                .setAllowDrag(false)
                .setNeedRightMark(true)
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    dialog.dismiss();
                    if (TextUtils.equals(tag, "local")) {
                        Restore.INSTANCE.restore(Backup.INSTANCE.getDefaultPath(), BackupSettingActivity.this);
                    } else if (TextUtils.equals(tag, "auto")) {
                        Restore.INSTANCE.restore(Backup.INSTANCE.getDefaultPath() + File.separator + "auto", BackupSettingActivity.this);
                    } else if (TextUtils.equals(tag, "webDav_empty")) {
                        Restore.INSTANCE.restore(Backup.INSTANCE.getDefaultPath(), BackupSettingActivity.this);
                    } else {
                        WebDavHelp.INSTANCE.restoreWebDav(list.get(position - 2), BackupSettingActivity.this);
                    }
                });
        builder.setCheckedIndex(-1);

        QMUIBottomSheetListItemModel auto = new QMUIBottomSheetListItemModel("自动备份(位于:BookReadApp/auto)", "auto");
        auto.image(R.drawable.dir);
        builder.addItem(auto);

        QMUIBottomSheetListItemModel local = new QMUIBottomSheetListItemModel("本地备份(位于:BookReadApp/)", "local");
        local.image(R.drawable.dir);
        builder.addItem(local);

        if (list == null || list.size() == 0) {
            QMUIBottomSheetListItemModel itemData = new QMUIBottomSheetListItemModel("云备份内容为空,请先配置", "webDav");
            itemData.image(R.drawable.ic_cloud);
            builder.addItem(itemData);
        } else {
            for (int i = 0; i < list.size(); i++) {
                QMUIBottomSheetListItemModel itemData = new QMUIBottomSheetListItemModel(list.get(i), "webDav_empty");
                itemData.image(R.drawable.ic_cloud);
                builder.addItem(itemData);
            }
        }
        builder.build().show();
    }

    @Override
    public void backupSuccess() {
        toast("备份成功");
    }

    @Override
    public void backupError(@NotNull String msg) {
        toast(msg);
    }

    @Override
    public void restoreSuccess() {
        toast("恢复成功");
    }

    @Override
    public void restoreError(@NotNull String msg) {
        toast(msg);
    }
}

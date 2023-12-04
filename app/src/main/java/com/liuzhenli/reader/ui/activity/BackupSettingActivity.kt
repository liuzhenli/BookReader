package com.liuzhenli.reader.ui.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import com.liuzhenli.common.BaseActivity
import com.liuzhenli.common.SharedPreferencesUtil
import com.liuzhenli.common.constant.AppConstant
import com.liuzhenli.common.utils.AppSharedPreferenceHelper
import com.liuzhenli.common.utils.ClickUtils
import com.liuzhenli.common.utils.PathUtil.Companion.getPathShow
import com.liuzhenli.common.widget.DialogUtil
import com.micoredu.reader.R
import com.micoredu.reader.databinding.ActBackupsettingBinding
import com.micoredu.reader.help.storage.Backup.backupPath
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.BottomListSheetBuilder
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheetListItemModel

class BackupSettingActivity : BaseActivity<ActBackupsettingBinding>() {
    private lateinit var mBackupNetDes: Array<String>
    override fun inflateView(inflater: LayoutInflater?): ActBackupsettingBinding {
        return ActBackupsettingBinding.inflate(inflater!!)
    }

    override fun init(savedInstanceState: Bundle?) {
        mBackupNetDes = resources.getStringArray(R.array.backup_network_type_des)
        setBackupPathInfo()
        //web dav address
        ClickUtils.click(binding.mViewNetAddress) {
            DialogUtil.showEditTextDialog(
                this, resources.getString(
                    R.string.web_dav_address
                ), "请输入网址",
                AppSharedPreferenceHelper.getWebDavAddress(), null
            ) { s: String? ->
                AppSharedPreferenceHelper.saveWebDavAddress(s)
                binding.mTvNetAddress.text = AppSharedPreferenceHelper.getWebDavAddress()
            }
        }
        //web dav account
        ClickUtils.click(binding.viewAccount) {
            DialogUtil.showEditTextDialog(
                this, resources.getString(
                    R.string.web_dav_account
                ), "请输入账号",
                AppSharedPreferenceHelper.getWebDavAccountName(), null
            ) { s: String? ->
                binding.mTvAccount.text = s
                AppSharedPreferenceHelper.saveWebDavAccountName(s)
            }
        }
        //web dav password
        ClickUtils.click(binding.mVPassword) {
            DialogUtil.showEditTextDialog(
                this, resources.getString(
                    R.string.web_dav_password
                ), "请输入密码",
                AppSharedPreferenceHelper.getWebDavAddPwd(), null
            ) { password: String? -> AppSharedPreferenceHelper.saveWebDavAddPwd(password) }
        }

        //恢复
        ClickUtils.click(binding.mVRestore) { restoreFile() }
        //备份到手机
        ClickUtils.click(binding.mVBackup) { }
        //备份长按 选择备份位置
        ClickUtils.longClick(binding.mVBackup) { }
        //备份到云端
        ClickUtils.click(binding.mVBackupToWeb) { }

        //备份网络
        ClickUtils.click(binding.mVBackupNetSetting) {
            val selection = if (SharedPreferencesUtil.getInstance().getInt(
                    AppSharedPreferenceHelper.AUTO_BACKUP_NET_TYPE,
                    AppSharedPreferenceHelper.BackupNetType.WIFI_ONLY
                ) == AppSharedPreferenceHelper.BackupNetType.ALL_ALLOWED
            ) 0 else 1
            DialogUtil.sowSingleChoiceDialog(
                mContext,
                mBackupNetDes,
                { dialog: DialogInterface?, which: Int ->
                    if (which == 0) {
                        SharedPreferencesUtil.getInstance().putInt(
                            AppSharedPreferenceHelper.AUTO_BACKUP_NET_TYPE,
                            AppSharedPreferenceHelper.BackupNetType.ALL_ALLOWED
                        )
                    } else {
                        SharedPreferencesUtil.getInstance().putInt(
                            AppSharedPreferenceHelper.AUTO_BACKUP_NET_TYPE,
                            AppSharedPreferenceHelper.BackupNetType.WIFI_ONLY
                        )
                    }
                    setBackupNetMode()
                },
                selection
            )
        }
        ClickUtils.click(
            binding.mTVBackupGuide
        ) { o: Any? -> WebViewActivity.start(mContext, AppConstant.URL_BACKUP_GUIDE) }
        binding.mTvNetAddress.text = AppSharedPreferenceHelper.getWebDavAddress()
        binding.mTvAccount.text = AppSharedPreferenceHelper.getWebDavAccountName()
        binding.mTvPassword.text = AppSharedPreferenceHelper.getWebDavAddPwd()
        setBackupNetMode()
    }

    private fun setBackupNetMode() {
        if (SharedPreferencesUtil.getInstance().getInt(
                AppSharedPreferenceHelper.AUTO_BACKUP_NET_TYPE,
                AppSharedPreferenceHelper.BackupNetType.WIFI_ONLY
            ) == AppSharedPreferenceHelper.BackupNetType.ALL_ALLOWED
        ) {
            binding.mTVBackupNetType.text = mBackupNetDes[0]
        } else {
            binding.mTVBackupNetType.text = mBackupNetDes[1]
        }
    }

    /**
     * 恢复
     */
    private fun restoreFile() {
//        WebDavHelp webDavHelp = WebDavHelp.INSTANCE;
//        if (!webDavHelp.initWebDav()) {
//            showBackups(null);
//            return;
//        }
//
//        Single.create((SingleOnSubscribe<ArrayList<String>>) emitter ->
//                        emitter.onSuccess(WebDavHelp.INSTANCE.getWebDavFileNames())).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleObserver<ArrayList<String>>() {
//                    @Override
//                    public void onSubscribe(@NotNull Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(@NotNull ArrayList<String> strings) {
//                        if (strings.size() == 0) {
//                            //toast("没有云备份");
//                        }
//                        showBackups(strings);
//                    }
//
//                    @Override
//                    public void onError(@NotNull Throwable e) {
//                        e.printStackTrace();
//                        showBackups(null);
//                        EventBus.getDefault().post(new OnWebDavSetEvent(false));
//                    }
//                });
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // BackupRestoreUi.INSTANCE.onActivityResult(requestCode, resultCode, data, BackupSettingActivity.this, BackupSettingActivity.this);
        setBackupPathInfo()
    }

    /**
     * 文件恢复
     * @param list 备份的文件列表
     */
    private fun showBackups(list: List<String>?) {
        val builder = BottomListSheetBuilder(this)
        builder.setGravityCenter(false)
            .setTitle("选择恢复的文件")
            .setAddCancelBtn(false)
            .setAllowDrag(false)
            .setNeedRightMark(true)
            .setOnSheetItemClickListener { dialog: QMUIBottomSheet, itemView: View?, position: Int, tag: String? ->
                dialog.dismiss()
                if (TextUtils.equals(tag, "local")) {
                    //Restore.INSTANCE.restore(Backup.INSTANCE.defaultPath(), BackupSettingActivity.this);
                } else if (TextUtils.equals(tag, "auto")) {
                    // Restore.INSTANCE.restore(Backup.INSTANCE.defaultPath() + File.separator + "auto", BackupSettingActivity.this);
                } else if (TextUtils.equals(tag, "webDav_empty")) {
                    //Restore.INSTANCE.restore(Backup.INSTANCE.defaultPath(), BackupSettingActivity.this);
                } else {
                    // WebDavHelp.INSTANCE.restoreWebDav(list.get(position - 2), BackupSettingActivity.this);
                }
            }
        builder.setCheckedIndex(-1)
        val auto = QMUIBottomSheetListItemModel("自动备份", "auto")
        auto.image(R.drawable.dir)
        builder.addItem(auto)
        val local = QMUIBottomSheetListItemModel("本地备份", "local")
        local.image(R.drawable.dir)
        builder.addItem(local)
        if (list.isNullOrEmpty()) {
            val itemData = QMUIBottomSheetListItemModel("云备份内容为空,请先配置", "webDav_empty")
            itemData.image(R.drawable.ic_cloud)
            builder.addItem(itemData)
        } else {
            for (i in list.indices) {
                val itemData = QMUIBottomSheetListItemModel(list[i], "webDav")
                itemData.image(R.drawable.ic_cloud)
                builder.addItem(itemData)
            }
        }
        builder.build().show()
    }

    private fun setBackupPathInfo() {
        val defPath = backupPath
        val localPath = AppSharedPreferenceHelper.getBackupPath(defPath)
        val path = if (TextUtils.isEmpty(localPath)) "未设置" else String.format(
            "备份路径：%s",
            getPathShow(localPath)
        )
        binding.tvViewBackupPathInfoVis.text = path
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, BackupSettingActivity::class.java)
            context.startActivity(intent)
        }
    }
}
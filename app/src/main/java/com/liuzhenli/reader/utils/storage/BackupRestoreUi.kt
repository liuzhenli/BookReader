package com.liuzhenli.reader.utils.storage

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.FragmentActivity
import com.hwangjr.rxbus.RxBus
import com.liuzhenli.common.BaseApplication
import com.liuzhenli.common.SharedPreferencesUtil
import com.liuzhenli.common.constant.RxBusTag
import com.liuzhenli.common.observer.MyObserver
import com.liuzhenli.common.utils.AppConfigManager
import com.liuzhenli.reader.utils.PermissionUtil
import com.liuzhenli.reader.utils.ToastUtil
import com.liuzhenli.reader.utils.filepicker.picker.FilePicker
import com.liuzhenli.reader.utils.storage.WebDavHelp.getWebDavFileNames
import com.liuzhenli.reader.utils.storage.WebDavHelp.showRestoreDialog
import com.microedu.reader.R
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import java.util.*

object BackupRestoreUi : Backup.CallBack, Restore.CallBack {

    private const val backupSelectRequestCode = 22
    private const val restoreSelectRequestCode = 33

    /**
     * 备份成功
     */
    override fun backupSuccess() {
        ToastUtil.showToast(R.string.backup_success)
    }

    /**
     * 备份是失败
     */
    override fun backupError(msg: String) {
        ToastUtil.showToast(msg)
    }

    /**
     * 恢复成功
     */
    override fun restoreSuccess() {
        ToastUtil.showToast(R.string.restore_success)
        RxBus.get().post(RxBusTag.RECREATE, true)
    }

    /**
     * 恢复失败
     */
    override fun restoreError(msg: String) {
        ToastUtil.showToast(msg)
    }

    /**
     * 备份
     */
    fun backup(activity: FragmentActivity) {
        val backupPath = AppConfigManager.getBackupPath(Backup.defaultPath)
        if (backupPath.isNullOrEmpty()) {
            selectBackupFolder(activity)
        } else {
            if (backupPath.isContentPath()) {
                val uri = Uri.parse(backupPath)
                val doc = DocumentFile.fromTreeUri(activity, uri)
                if (doc?.canWrite() == true) {
                    Backup.backup(activity, backupPath, this)
                } else {
                    selectBackupFolder(activity)
                }
            } else {
                backupUsePermission(activity)
            }
        }
    }

    /**
     * 请求权限
     */
    private fun backupUsePermission(activity: FragmentActivity, path: String = Backup.defaultPath) {
        PermissionUtil.requestPermission(activity, object : MyObserver<Boolean>() {
            override fun onNext(t: Boolean) {
                AppConfigManager.setBackupPath(path)
                Backup.backup(activity, path, this@BackupRestoreUi)
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    /**
     * 选择备份路径
     */
    fun selectBackupFolder(activity: FragmentActivity) {
        activity.alert {
            titleResource = R.string.select_folder
            items(activity.resources.getStringArray(R.array.select_folder).toList()) { _, index ->
                when (index) {
                    0 -> {
                        AppConfigManager.setBackupPath(Backup.defaultPath)
                        backupUsePermission(activity)
                    }
                    1 -> {
                        try {
                            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            activity.startActivityForResult(intent, backupSelectRequestCode)
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            activity.toast(e.localizedMessage ?: "ERROR")
                        }
                    }
                    2 -> {
                        PermissionUtil.requestPermission(activity, object : MyObserver<Boolean>() {
                            override fun onNext(t: Boolean) {
                                selectBackupFolderApp(activity, false)
                            }
                        }, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                }
            }
        }.show()
    }

    /**
     * 选择备份文件夹
     */
    private fun selectBackupFolderApp(activity: Activity, isRestore: Boolean) {
        val picker = FilePicker(activity, FilePicker.DIRECTORY)
        picker.setBackgroundColor(ContextCompat.getColor(activity, R.color.background))
        picker.setTopBackgroundColor(ContextCompat.getColor(activity, R.color.background))
        picker.setItemHeight(30)
        picker.setOnFilePickListener { currentPath: String ->
            AppConfigManager.setBackupPath(currentPath)
            if (isRestore) {
                Restore.restore(currentPath, this)
            } else {
                Backup.backup(activity, currentPath, this)
            }
        }
        picker.show()
    }

    /**
     * 备份恢复
     */
    fun restore(activity: FragmentActivity) {
        Single.create { emitter: SingleEmitter<ArrayList<String>?> ->
            emitter.onSuccess(getWebDavFileNames())
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<ArrayList<String>?> {
                    override fun onSuccess(strings: ArrayList<String>) {
                        if (!showRestoreDialog(activity, strings, this@BackupRestoreUi)) {
                            val path = AppConfigManager.getBackupPath(Backup.defaultPath)
                            if (TextUtils.isEmpty(path)) {
                                selectRestoreFolder(activity)
                            } else {
                                if (path.isContentPath()) {
                                    val uri = Uri.parse(path)
                                    val doc = DocumentFile.fromTreeUri(activity, uri)
                                    if (doc?.canWrite() == true) {
                                        Restore.restore(activity, Uri.parse(path), this@BackupRestoreUi)
                                    } else {
                                        selectRestoreFolder(activity)
                                    }
                                } else {
                                    restoreUsePermission(activity)
                                }
                            }
                        }
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                    }
                })
    }

    /**
     * 恢复
     */
    private fun restoreUsePermission(activity: FragmentActivity, path: String = Backup.defaultPath) {
        PermissionUtil.requestPermission(activity, object : MyObserver<Boolean>() {
            override fun onNext(t: Boolean) {
                AppConfigManager.setBackupPath(path)
                Restore.restore(path, this@BackupRestoreUi)
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    /**
     * 设置恢复路径
     */
    private fun selectRestoreFolder(activity: FragmentActivity) {
        activity.alert {
            titleResource = R.string.select_folder
            items(activity.resources.getStringArray(R.array.select_folder).toList()) { _, index ->
                when (index) {
                    0 -> restoreUsePermission(activity)
                    1 -> {
                        try {
                            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            activity.startActivityForResult(intent, restoreSelectRequestCode)
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            activity.toast(e.localizedMessage ?: "ERROR")
                        }
                    }
                    2 -> {
                        PermissionUtil.requestPermission(activity, object : MyObserver<Boolean>() {
                            override fun onNext(t: Boolean) {
                                selectBackupFolderApp(activity, true)
                            }
                        })
                    }
                }
            }
        }.show()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            backupSelectRequestCode -> if (resultCode == RESULT_OK) {
                data?.data?.let { uri ->
                    BaseApplication.getInstance().contentResolver.takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                    AppConfigManager.setBackupPath(uri.toString())
                    Backup.backup(BaseApplication.getInstance(), uri.toString(), this)
                }
            }
            restoreSelectRequestCode -> if (resultCode == RESULT_OK) {
                data?.data?.let { uri ->
                    BaseApplication.getInstance().contentResolver.takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                    AppConfigManager.setBackupPath(uri.toString())
                    Restore.restore(BaseApplication.getInstance(), uri, this)
                }
            }
        }
    }

}

fun String?.isContentPath(): Boolean = this?.startsWith("content://") == true
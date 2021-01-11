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
import com.liuzhenli.common.BaseApplication
import com.liuzhenli.common.observer.MyObserver
import com.liuzhenli.common.utils.AppSharedPreferenceHelper
import com.liuzhenli.reader.utils.PermissionUtil
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

object BackupRestoreUi {

    private const val backupSelectRequestCode = 22
    private const val restoreSelectRequestCode = 33

    /**
     * 备份
     */
    fun backup(activity: FragmentActivity, isBackupToWebDav: Boolean = false, callBack: Backup.CallBack?, restoreCallBack: Restore.CallBack?) {
        val backupPath = AppSharedPreferenceHelper.getBackupPath(Backup.defaultPath)
        if (backupPath.isNullOrEmpty()) {
            selectBackupFolder(activity, isBackupToWebDav, callBack,restoreCallBack)
        } else {
            if (backupPath.isContentPath()) {
                val uri = Uri.parse(backupPath)
                val doc = DocumentFile.fromTreeUri(activity, uri)
                if (doc?.canWrite() == true) {
                    Backup.backup(activity, backupPath, callBack, isBackupToWebDav)
                } else {
                    selectBackupFolder(activity, isBackupToWebDav, callBack, restoreCallBack)
                }
            } else {
                backupUsePermission(activity, isBackupToWebDav, Backup.defaultPath, callBack)
            }
        }
    }

    /**
     * 请求权限
     */
    private fun backupUsePermission(activity: FragmentActivity, isBackupToWebDav: Boolean = true, path: String = Backup.defaultPath, callBack: Backup.CallBack?) {
        PermissionUtil.requestPermission(activity, object : MyObserver<Boolean>() {
            override fun onNext(t: Boolean) {
                AppSharedPreferenceHelper.setBackupPath(path)
                Backup.backup(activity, path, callBack, isBackupToWebDav)
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    /**
     * 选择备份路径
     */
    fun selectBackupFolder(activity: FragmentActivity, isBackupToWebDav: Boolean = true, callBack: Backup.CallBack?, restoreCallBack: Restore.CallBack?) {
        activity.alert {
            titleResource = R.string.select_folder
            items(activity.resources.getStringArray(R.array.select_folder).toList()) { _, index ->
                when (index) {
                    0 -> {
                        AppSharedPreferenceHelper.setBackupPath(Backup.defaultPath)
                        backupUsePermission(activity, isBackupToWebDav, Backup.defaultPath, callBack)
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
                                selectBackupFolderApp(activity, false, callBack, restoreCallBack)
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
    private fun selectBackupFolderApp(activity: Activity, isRestore: Boolean, callBack: Backup.CallBack?, restoreCallBack: Restore.CallBack?) {
        val picker = FilePicker(activity, FilePicker.DIRECTORY)
        picker.setBackgroundColor(ContextCompat.getColor(activity, R.color.background))
        picker.setTopBackgroundColor(ContextCompat.getColor(activity, R.color.background))
        picker.setItemHeight(30)
        picker.setOnFilePickListener { currentPath: String ->
            AppSharedPreferenceHelper.setBackupPath(currentPath)
            if (isRestore) {
                Restore.restore(currentPath, restoreCallBack)
            } else {
                Backup.backup(activity, currentPath, callBack)
            }
        }
        picker.show()
    }

    /**
     * 备份恢复
     */
    fun restore(activity: FragmentActivity, callBack: Restore.CallBack, backupCallBack: Backup.CallBack?) {
        Single.create { emitter: SingleEmitter<ArrayList<String>?> ->
            emitter.onSuccess(getWebDavFileNames())
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<ArrayList<String>?> {
                    override fun onSuccess(strings: ArrayList<String>) {
                        if (!showRestoreDialog(activity, strings, callBack)) {
                            val path = AppSharedPreferenceHelper.getBackupPath(Backup.defaultPath)
                            if (TextUtils.isEmpty(path)) {
                                selectRestoreFolder(activity, backupCallBack,callBack)
                            } else {
                                if (path.isContentPath()) {
                                    val uri = Uri.parse(path)
                                    val doc = DocumentFile.fromTreeUri(activity, uri)
                                    if (doc?.canWrite() == true) {
                                        Restore.restore(activity, Uri.parse(path), callBack)
                                    } else {
                                        selectRestoreFolder(activity, backupCallBack,callBack)
                                    }
                                } else {
                                    restoreUsePermission(activity, Backup.defaultPath, callBack)
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
    private fun restoreUsePermission(activity: FragmentActivity, path: String = Backup.defaultPath, callBack: Restore.CallBack?) {
        PermissionUtil.requestPermission(activity, object : MyObserver<Boolean>() {
            override fun onNext(t: Boolean) {
                AppSharedPreferenceHelper.setBackupPath(path)
                Restore.restore(path, callBack)
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    /**
     * 设置恢复路径
     */
    private fun selectRestoreFolder(activity: FragmentActivity, callBack: Backup.CallBack?, restoreCallBack: Restore.CallBack?) {
        activity.alert {
            titleResource = R.string.select_folder
            items(activity.resources.getStringArray(R.array.select_folder).toList()) { _, index ->
                when (index) {
                    0 -> restoreUsePermission(activity, Backup.defaultPath, restoreCallBack)
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
                                selectBackupFolderApp(activity, true, callBack, restoreCallBack)
                            }
                        })
                    }
                }
            }
        }.show()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, callBack: Backup.CallBack, restoreCallBack: Restore.CallBack?) {
        when (requestCode) {
            backupSelectRequestCode -> if (resultCode == RESULT_OK) {
                data?.data?.let { uri ->
                    BaseApplication.getInstance().contentResolver.takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                    AppSharedPreferenceHelper.setBackupPath(uri.toString())
                    Backup.backup(BaseApplication.getInstance(), uri.toString(), callBack)
                }
            }
            restoreSelectRequestCode -> if (resultCode == RESULT_OK) {
                data?.data?.let { uri ->
                    BaseApplication.getInstance().contentResolver.takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                    AppSharedPreferenceHelper.setBackupPath(uri.toString())
                    Restore.restore(BaseApplication.getInstance(), uri, restoreCallBack)
                }
            }
        }
    }

}

fun String?.isContentPath(): Boolean = this?.startsWith("content://") == true
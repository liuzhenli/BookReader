package com.micoredu.reader.utils.storage

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.liuzhenli.common.BaseApplication
import com.liuzhenli.common.FileHelp
import com.liuzhenli.common.SharedPreferencesUtil
import com.liuzhenli.common.utils.FileUtils
import com.liuzhenli.common.utils.GsonUtil
import com.micoredu.reader.helper.BookshelfHelper
import com.micoredu.reader.helper.AppReaderDbHelper
import com.micoredu.reader.model.BookSourceManager
import com.micoredu.reader.model.ReplaceRuleManager
import com.micoredu.reader.model.TxtChapterRuleManager
import com.micoredu.reader.utils.DocumentUtil
import com.thegrizzlylabs.sardineandroid.model.Set
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * 备份
 */
object Backup {

    /**生成备份文件的存储位置*/
    val backupPath = BaseApplication.getInstance().filesDir.absolutePath + File.separator + "backup"

    /***恢复备份默认地址*/
    val defaultPath by lazy {
        FileUtils.getSdCardPath() + File.separator + "YiShuFang/backups"
    }

    val backupFileNames by lazy {
        arrayOf(
                "myBookShelf.json",
                "myBookSource.json",
                "myBookSearchHistory.json",
                "myBookReplaceRule.json",
                "myTxtChapterRule.json",
                "mmvConfig.json"
        )
    }

    /***自动备份  一天内自动备份一次*/
    fun autoBack() {
        val lastBackup = SharedPreferencesUtil.getInstance().getLong("lastBackup", 0)
        if (System.currentTimeMillis() - lastBackup < TimeUnit.DAYS.toMillis(1)) {
            return
        }
        val path = SharedPreferencesUtil.getInstance().getString("backupPath", defaultPath)
        if (path == null) {
            backup(BaseApplication.getInstance(), defaultPath, null, true)
        } else {
            backup(BaseApplication.getInstance(), path, null, true)
        }
    }

    /**
     * 备份
     * @param isBackupToWebDav 是否备份到云端
     */
    fun backup(context: Context, path: String, callBack: CallBack?, isBackupToWebDav: Boolean = true, isAuto: Boolean = false) {

        SharedPreferencesUtil.getInstance().putLong("lastBackup", System.currentTimeMillis())
        Single.create(SingleOnSubscribe<Boolean> { e ->
            BookshelfHelper.getAllBook().let {
                if (it.isNotEmpty()) {
                    val json = GsonUtil.toJson(it)
                    FileHelp.createFileIfNotExist(backupPath + File.separator + "myBookShelf.json").writeText(json)
                }
            }
            BookSourceManager.getAllBookSource().let {
                if (it.isNotEmpty()) {
                    val json = GsonUtil.toJson(it)
                    FileHelp.createFileIfNotExist(backupPath + File.separator + "myBookSource.json").writeText(json)
                }
            }
            AppReaderDbHelper.getInstance().database.searchHistoryDao.all().let {
                if (it.isNotEmpty()) {
                    val json = GsonUtil.toJson(it)
                    FileHelp.createFileIfNotExist(backupPath + File.separator + "myBookSearchHistory.json").writeText(json)
                }
            }
            ReplaceRuleManager.getAll().blockingGet().let {
                if (it.isNotEmpty()) {
                    val json = GsonUtil.toJson(it)
                    FileHelp.createFileIfNotExist(backupPath + File.separator + "myBookReplaceRule.json").writeText(json)
                }
            }
            TxtChapterRuleManager.getAll().let {
                if (it.isNotEmpty()) {
                    val json = GsonUtil.toJson(it)
                    FileHelp.createFileIfNotExist(backupPath + File.separator + "myTxtChapterRule.json").writeText(json)
                }
            }

            //备份mmkv 文件
            SharedPreferencesUtil.getInstance().prefs.allKeys().let {
                val list: ArrayList<MMKVBean> = ArrayList()
                for (i in 0 until (it!!.size)) {
                    val key = it[i]
                    val objectValue = SharedPreferencesUtil.getInstance().getObjectValue(key)
                    var type: String = when (objectValue) {
                        is Int -> MMKVBean.Type.INT
                        is Boolean -> MMKVBean.Type.BOOL
                        is Float -> MMKVBean.Type.FLOAT
                        is Double -> MMKVBean.Type.DOUBLE
                        is Long -> MMKVBean.Type.LONG
                        is Set -> MMKVBean.Type.SET
                        else -> MMKVBean.Type.STRING
                    }
                    list.add(MMKVBean(type, key, objectValue.toString()))
                }
                list.let {
                    val json = GsonUtil.toJson(list)
                    FileHelp.createFileIfNotExist(backupPath + File.separator + "mmvConfig.json").writeText(json)
                }
            }


//            Preferences.getSharedPreferences(context, backupPath, "config")?.let { sp ->
//                val edit = sp.edit()
//                SharedPreferencesUtil.getInstance().prefs.all.map {
//                    when (val value = it.value) {
//                        is Int -> edit.putInt(it.key, value)
//                        is Boolean -> edit.putBoolean(it.key, value)
//                        is Long -> edit.putLong(it.key, value)
//                        is Float -> edit.putFloat(it.key, value)
//                        is String -> edit.putString(it.key, value)
//                        else -> Unit
//                    }
//                }
//                edit.commit()
//            }
            if (path.isContentPath()) {
                copyBackup(context, Uri.parse(path), isAuto)
            } else {
                copyBackup(path, isAuto)
            }
            if (isBackupToWebDav) {
                WebDavHelp.backUpWebDav(backupPath, callBack)
            } else {
                callBack?.backupSuccess()
            }
            e.onSuccess(true)
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Boolean> {
                    override fun onSuccess(t: Boolean) {
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        callBack?.backupError(e.localizedMessage ?: "ERROR")
                    }

                    override fun onSubscribe(d: Disposable) {
                    }
                })
    }

    @Throws(Exception::class)
    private fun copyBackup(context: Context, uri: Uri, isAuto: Boolean) {
        synchronized(this) {
            DocumentFile.fromTreeUri(context, uri)?.let { treeDoc ->
                for (fileName in backupFileNames) {
                    val file = File(backupPath + File.separator + fileName)
                    if (file.exists()) {
                        if (isAuto) {
                            treeDoc.findFile("auto")?.findFile(fileName)?.delete()
                            var autoDoc = treeDoc.findFile("auto")
                            if (autoDoc == null) {
                                autoDoc = treeDoc.createDirectory("auto")
                            }
                            autoDoc?.createFile("", fileName)?.let {
                                DocumentUtil.writeBytes(context, file.readBytes(), it)
                            }
                        } else {
                            treeDoc.findFile(fileName)?.delete()
                            treeDoc.createFile("", fileName)?.let {
                                DocumentUtil.writeBytes(context, file.readBytes(), it)
                            }
                        }
                    }
                }
            }
        }
    }

    @Throws(java.lang.Exception::class)
    private fun copyBackup(path: String, isAuto: Boolean) {
        synchronized(this) {
            for (fileName in backupFileNames) {
                if (isAuto) {
                    val file = File(backupPath + File.separator + fileName)
                    if (file.exists()) {
                        file.copyTo(FileHelp.createFileIfNotExist(path + File.separator + "auto" + File.separator + fileName), true)
                    }
                } else {
                    val file = File(backupPath + File.separator + fileName)
                    if (file.exists()) {
                        file.copyTo(FileHelp.createFileIfNotExist(path + File.separator + fileName), true)
                    }
                }
            }
        }
    }

    interface CallBack {
        fun backupSuccess()
        fun backupError(msg: String)
    }
}
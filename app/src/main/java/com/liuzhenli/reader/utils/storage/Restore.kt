package com.liuzhenli.reader.utils.storage

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import androidx.documentfile.provider.DocumentFile
import com.liuzhenli.common.BaseApplication
import com.liuzhenli.common.FileHelp
import com.liuzhenli.common.SharedPreferencesUtil
import com.liuzhenli.common.utils.DocumentUtil
import com.liuzhenli.common.utils.GsonUtil
import com.liuzhenli.common.utils.LauncherIcon
import com.liuzhenli.common.utils.fromJsonArray
import com.micoredu.reader.R
import com.micoredu.reader.bean.*
import com.micoredu.reader.helper.AppReaderDbHelper
import com.micoredu.reader.helper.ReadConfigManager
import com.micoredu.reader.model.BookSourceManager
import com.micoredu.reader.model.ReplaceRuleManager
import com.micoredu.reader.model.TxtChapterRuleManager
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File

object Restore {

    fun restore(context: Context, uri: Uri, callBack: CallBack?) {
        Single.create(SingleOnSubscribe<Boolean> { e ->
            DocumentFile.fromTreeUri(context, uri)?.listFiles()?.forEach { doc ->
                for (fileName in Backup.backupFileNames) {
                    if (doc.name == fileName) {
                        DocumentUtil.readBytes(context, doc.uri)?.let {
                            FileHelp.createFileIfNotExist(Backup.backupPath + File.separator + fileName)
                                    .writeBytes(it)
                        }
                    }
                }
            }
            e.onSuccess(true)
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Boolean> {
                    override fun onSuccess(t: Boolean) {
                        restore(Backup.backupPath, callBack)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        callBack?.restoreError(e.localizedMessage ?: "ERROR")
                    }

                    override fun onSubscribe(d: Disposable) {
                    }
                })
    }

    fun restore(path: String, callBack: Restore.CallBack?) {
        Single.create(SingleOnSubscribe<Boolean> { e ->
            try {
                val file = FileHelp.createFileIfNotExist(path + File.separator + "myBookShelf.json")
                val json = file.readText()
                GsonUtil.fromJsonArray<BookShelfBean>(json)?.forEach { bookshelf ->
                    if (bookshelf.noteUrl != null) {
                        AppReaderDbHelper.getInstance().database.bookShelfDao.insertOrReplace(bookshelf)
                    }
                    if (bookshelf.bookInfoBean.noteUrl != null) {
                        AppReaderDbHelper.getInstance().database.bookInfoDao.insertOrReplace(bookshelf.bookInfoBean)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                val file = FileHelp.createFileIfNotExist(path + File.separator + "myBookSource.json")
                val json = file.readText()
                GsonUtil.fromJsonArray<BookSourceBean>(json)?.let {
                    BookSourceManager.addBookSource(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                val file = FileHelp.createFileIfNotExist(path + File.separator + "myBookSearchHistory.json")
                val json = file.readText()
                GsonUtil.fromJsonArray<SearchHistoryBean>(json)?.let {
                    AppReaderDbHelper.getInstance().database.searchHistoryDao.insertOrReplaceInTx(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                val file = FileHelp.createFileIfNotExist(path + File.separator + "myReadHistory.json")
                val json = file.readText()
                GsonUtil.fromJsonArray<ReadHistory>(json)?.let {
                    AppReaderDbHelper.getInstance().database.readHistoryDao.insertOrReplace(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                val file = FileHelp.createFileIfNotExist(path + File.separator + "myBookReplaceRule.json")
                val json = file.readText()
                GsonUtil.fromJsonArray<ReplaceRuleBean>(json)?.let {
                    ReplaceRuleManager.addDataS(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                val file = FileHelp.createFileIfNotExist(path + File.separator + "myTxtChapterRule.json")
                val json = file.readText()
                GsonUtil.fromJsonArray<TxtChapterRuleBean>(json)?.let {
                    TxtChapterRuleManager.save(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            val file = FileHelp.createFileIfNotExist(path + File.separator + "mmvConfig.json")
            val json = file.readText()
            GsonUtil.fromJsonArray<MMKVBean>(json)?.map {
                val value = it.value;
                when {
                    TextUtils.equals(MMKVBean.Type.INT, it.type) -> {
                        try {
                            SharedPreferencesUtil.getInstance().putInt(it.key, value.toInt())
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    TextUtils.equals(MMKVBean.Type.BOOL, it.type) -> {
                        try {
                            SharedPreferencesUtil.getInstance().putBoolean(it.key, value.toBoolean())
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    TextUtils.equals(MMKVBean.Type.FLOAT, it.type) -> {
                        try {
                            SharedPreferencesUtil.getInstance().putFloat(it.key, value.toFloat())
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }


                    TextUtils.equals(MMKVBean.Type.DOUBLE, it.type) -> {
                        try {
                            SharedPreferencesUtil.getInstance().putDouble(it.key, value.toDouble())
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }


                    TextUtils.equals(MMKVBean.Type.LONG, it.type) -> {
                        try {
                            SharedPreferencesUtil.getInstance().putLong(it.key, value.toLong())
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }


                    TextUtils.equals(MMKVBean.Type.SET, it.type) -> {
                    }

                    else -> {
                        try {
                            SharedPreferencesUtil.getInstance().putString(it.key, value)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }


            e.onSuccess(true)
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Boolean> {
                    override fun onSuccess(t: Boolean) {
                        LauncherIcon.ChangeIcon(SharedPreferencesUtil.getInstance().getString("launcher_icon", BaseApplication.getInstance().getString(R.string.icon_main)))
                        ReadConfigManager.getInstance().updateReaderSettings()
                        BaseApplication.getInstance().upThemeStore()
                        BaseApplication.getInstance().initNightTheme()
                        callBack?.restoreSuccess()
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        callBack?.restoreError(e.localizedMessage ?: "ERROR")
                    }

                    override fun onSubscribe(d: Disposable) {
                    }
                })
    }


    interface CallBack {
        fun restoreSuccess()
        fun restoreError(msg: String)
    }

}
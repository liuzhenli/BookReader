package com.micoredu.reader.utils.storage

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.liuzhenli.common.BaseApplication
import com.liuzhenli.common.FileHelp
import com.liuzhenli.common.SharedPreferencesUtil
import com.liuzhenli.common.utils.GsonUtil
import com.liuzhenli.common.utils.LauncherIcon
import com.liuzhenli.common.utils.fromJsonArray
import com.micoredu.reader.R
import com.micoredu.reader.bean.*
import com.micoredu.reader.helper.DbHelper
import com.micoredu.reader.helper.ReadConfigManager
import com.micoredu.reader.model.BookSourceManager
import com.micoredu.reader.model.ReplaceRuleManager
import com.micoredu.reader.model.TxtChapterRuleManager
import com.micoredu.reader.utils.DocumentUtil
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
                        DbHelper.getDaoSession().bookShelfBeanDao.insertOrReplace(bookshelf)
                    }
                    if (bookshelf.bookInfoBean.noteUrl != null) {
                        DbHelper.getDaoSession().bookInfoBeanDao.insertOrReplace(bookshelf.bookInfoBean)
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
                    DbHelper.getDaoSession().searchHistoryBeanDao.insertOrReplaceInTx(it)
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
            var donateHb = SharedPreferencesUtil.getInstance().getLong("DonateHb", 0)
            donateHb = if (donateHb > System.currentTimeMillis()) 0 else donateHb

//            SharedPreferencesUtil.getInstance().prefs.all?.map {
//                val edit = SharedPreferencesUtil.getInstance()
//                when (val value = it.value) {
//                    is Int -> edit.putInt(it.key, value)
//                    is Boolean -> edit.putBoolean(it.key, value)
//                    is Long -> edit.putLong(it.key, value)
//                    is Float -> edit.putFloat(it.key, value)
//                    is String -> edit.putString(it.key, value)
//                    else -> Unit
//                }
//                edit.putLong("DonateHb", donateHb)
//                edit.putInt("versionCode", BaseApplication.getInstance().mVersionCode)
//                edit.prefs.edit().apply()
//            }
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
package com.liuzhenli.reader.utils.storage

import android.content.Context
import android.text.TextUtils
import com.liuzhenli.common.FileHelp
import com.liuzhenli.common.constant.AppConstant
import com.liuzhenli.common.utils.AppSharedPreferenceHelper
import com.liuzhenli.common.utils.ZipUtils
import com.micoredu.reader.utils.webdav.WebDav
import com.micoredu.reader.utils.webdav.http.HttpAuth
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.selector
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

object WebDavHelp {
    public var isWebDavSet: Boolean = false;
    private val zipFilePath = FileHelp.getCachePath() + "/backup" + ".zip"
    private val unzipFilesPath by lazy {
        FileHelp.getCachePath()
    }

    private fun getWebDavUrl(): String {

        var url = AppSharedPreferenceHelper.getWebDavAddress()
        if (url.isNullOrEmpty() || TextUtils.equals(url, "坚果云")) {
            url = AppConstant.DEFAULT_WEB_DAV_URL
        }
        if (!url.endsWith("/")) {
            url = "${url}/"
        }
        return url
    }

    fun initWebDav(): Boolean {
        val account = AppSharedPreferenceHelper.getWebDavAccountName()
        val password = AppSharedPreferenceHelper.getWebDavAddPwd()
        if (!account.isNullOrBlank() && !password.isNullOrBlank()) {
            HttpAuth.auth = HttpAuth.Auth(account, password)
            return true
        }
        return false
    }

    /**
     * 获取网络备份内容
     */
    fun getWebDavFileNames(): ArrayList<String> {
        val url = getWebDavUrl()
        val names = arrayListOf<String>()
        try {
            if (initWebDav()) {
                var files = WebDav("${url}YiShuFang/").listFiles()
                files = files.reversed()
                for (index: Int in 0 until min(10, files.size)) {
                    files[index].displayName?.let {
                        names.add(it)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return names
    }

    fun showRestoreDialog(
        context: Context,
        names: ArrayList<String>,
        callBack: Restore.CallBack?
    ): Boolean {
        return if (names.isNotEmpty()) {
            context.selector(title = "选择恢复文件", items = names) { _, index ->
                if (index in 0 until names.size) {
                    restoreWebDav(names[index], callBack)
                }
            }
            true
        } else {
            false
        }
    }

    /**
     * 恢复云备份
     */
    fun restoreWebDav(name: String, callBack: Restore.CallBack?) {
        Single.create(SingleOnSubscribe<Boolean> { e ->
            getWebDavUrl().let {
                val file = WebDav("${it}YiShuFang/" + name)
                file.downloadTo(zipFilePath, true)
                @Suppress("BlockingMethodInNonBlockingContext")
                ZipUtils.unzipFile(zipFilePath, unzipFilesPath)
            }
            e.onSuccess(true)
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Boolean> {
                override fun onSuccess(t: Boolean) {
                    Restore.restore(unzipFilesPath, callBack)
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    callBack?.restoreError(e.localizedMessage ?: "ERROR")

                }
            })
    }

    /**
     * 备份到服务器
     */
    fun backUpWebDav(path: String, callBack: Backup.CallBack?) {
        try {
            if (initWebDav()) {
                val paths = arrayListOf(*Backup.backupFileNames)
                for (i in 0 until paths.size) {
                    paths[i] = path + File.separator + paths[i]
                }
                FileHelp.deleteFile(zipFilePath)
                if (ZipUtils.zipFiles(paths, zipFilePath)) {
                    WebDav("${getWebDavUrl()}YiShuFang").makeAsDir()
                    val data = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        .format(Date(System.currentTimeMillis()))
                    val putUrl = "${getWebDavUrl()}YiShuFang/backup${data}.zip"
                    val success = WebDav(putUrl).upload(zipFilePath)
                    if (success) {
                        callBack?.backupSuccess()
                    } else {
                        callBack?.backupError("WebDav 备份失败了")
                    }
                }
            } else {
                callBack?.backupError("WebDav:未配置云端地址")
            }
        } catch (e: Exception) {
            callBack?.backupError("WebDav${e.localizedMessage}")
        }
    }
}
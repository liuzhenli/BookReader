package com.micoredu.reader.help.source

import android.os.Handler
import android.os.Looper
import com.micoredu.reader.bean.BookSource
import com.micoredu.reader.bean.RssSource
import com.liuzhenli.common.utils.AppConfig
import com.liuzhenli.common.utils.NetworkUtils
import com.liuzhenli.common.utils.ToastUtil
import com.micoredu.reader.dao.appDb
import com.micoredu.reader.utils.EncoderUtils
import com.micoredu.reader.utils.splitNotBlank
import kotlinx.coroutines.flow.*
import splitties.init.appCtx

object SourceHelp {

    suspend fun getSourceByKey(key: String?) {
        appDb.bookSourceDao.flowSearch(key!!).conflate().collect {
            return@collect
        }
    }

    val groupList: List<String> = appDb.bookSourceDao.allGroups

    val selectedBookSource: List<BookSource> =
        appDb.bookSourceDao.allEnabled

    val allBookSource: List<BookSource> = appDb.bookSourceDao.all

    private val handler = Handler(Looper.getMainLooper())
    private val list18Plus by lazy {
        try {
            return@lazy String(appCtx.assets.open("18PlusList.txt").readBytes())
                .splitNotBlank("\n")
        } catch (e: Exception) {
            return@lazy arrayOf<String>()
        }
    }

    fun insertRssSource(vararg rssSources: RssSource) {
        rssSources.forEach { rssSource ->
            if (is18Plus(rssSource.sourceUrl)) {
                handler.post {
                    ToastUtil.showToast("${rssSource.sourceName}是18+网址,禁止导入.")
                }
            } else {
                appDb.rssSourceDao.insert(rssSource)
            }
        }
    }

    fun insertBookSource(vararg bookSources: BookSource) {
        bookSources.forEach { bookSource ->
            if (is18Plus(bookSource.bookSourceUrl)) {
                handler.post {
                    ToastUtil.showToast("${bookSource.bookSourceName}是18+网址,禁止导入.")
                }
            } else {
                appDb.bookSourceDao.insert(bookSource)
            }
        }
    }

    private fun is18Plus(url: String?): Boolean {
        url ?: return false
        val baseUrl = NetworkUtils.getBaseUrl(url)
        baseUrl ?: return false
        if (AppConfig.isGooglePlay) return false
        kotlin.runCatching {
            val host = baseUrl.split("//", ".")
            val base64Url = EncoderUtils.base64Encode("${host[host.lastIndex - 1]}.${host.last()}")
            list18Plus.forEach {
                if (base64Url == it) {
                    return true
                }
            }
        }
        return false
    }


    fun removeBookSource(vararg bookSources: BookSource) {
        bookSources.forEach { bookSource ->
            appDb.bookSourceDao.delete(bookSource)
        }
    }

    fun update(vararg bookSources: BookSource) {
        bookSources.forEach { bookSource ->
            appDb.bookSourceDao.update(bookSource)
        }
    }

    fun update(bookSources: List<BookSource>) {
        bookSources.forEach { bookSource ->
            update(bookSource)
        }
    }

    fun toTop(bookSource: BookSource) {
        bookSource.customOrder = appDb.bookSourceDao.maxOrder + 1
        update(bookSource)
    }


}
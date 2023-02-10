package com.micoredu.reader.help

import com.microedu.lib.reader.R
import com.liuzhenli.common.utils.AppConst
import com.liuzhenli.common.exception.NoStackTraceException
import com.micoredu.reader.help.coroutine.Coroutine
import com.micoredu.reader.help.http.newCallStrResponse
import com.micoredu.reader.help.http.okHttpClient
import com.micoredu.reader.utils.jsonPath
import com.micoredu.reader.utils.readString
import kotlinx.coroutines.CoroutineScope
import splitties.init.appCtx

object AppUpdate {

    fun checkFromGitHub(
        scope: CoroutineScope,
    ): Coroutine<UpdateInfo> {
        return Coroutine.async(scope) {
            val lastReleaseUrl = appCtx.getString(R.string.latest_release_api)
            val body = okHttpClient.newCallStrResponse {
                url(lastReleaseUrl)
            }.body
            if (body.isNullOrBlank()) {
                throw NoStackTraceException("获取新版本出错")
            }
            val rootDoc = jsonPath.parse(body)
            val tagName = rootDoc.readString("$.tag_name")
                ?: throw NoStackTraceException("获取新版本出错")
            if (tagName > AppConst.appInfo.versionName) {
                val updateBody = rootDoc.readString("$.body")
                    ?: throw NoStackTraceException("获取新版本出错")
                val downloadUrl = rootDoc.readString("$.assets[0].browser_download_url")
                    ?: throw NoStackTraceException("获取新版本出错")
                val fileName = rootDoc.readString("$.assets[0].name")
                    ?: throw NoStackTraceException("获取新版本出错")
                return@async UpdateInfo(tagName, updateBody, downloadUrl, fileName)
            } else {
                throw NoStackTraceException("已是最新版本")
            }
        }.timeout(10000)
    }

    data class UpdateInfo(
        val tagName: String,
        val updateLog: String,
        val downloadUrl: String,
        val fileName: String
    )

}
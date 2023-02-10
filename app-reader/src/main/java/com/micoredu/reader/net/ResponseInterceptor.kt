package com.micoredu.reader.net

import android.text.TextUtils
import com.liuzhenli.common.utils.NetworkUtils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import splitties.init.appCtx
import java.nio.charset.Charset


class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if (!NetworkUtils.isConnected(appCtx)) {
            request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build()
        }

        val response = chain.proceed(request)
        if (NetworkUtils.isConnected(appCtx)) {


            val responseBody = response.body
            val bufferedSource = responseBody?.source()
            bufferedSource?.request(Long.MAX_VALUE)
            val buffer = bufferedSource?.buffer
            val contentType = responseBody?.contentType()
            var charset = Charset.forName("UTF-8")
            if (contentType != null) {
                charset = contentType.charset(Charset.forName("UTF-8"))
            }

            val cacheControl = request.cacheControl.toString()
            return if (TextUtils.isEmpty(cacheControl)) {
                response.newBuilder()
                    .header("Cache-Control", "no-cache")
                    .removeHeader("Pragma")
                    .build()
            } else {
                response.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build()
            }
        } else {
            return response.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                .removeHeader("Pragma")
                .build()
        }

    }
}
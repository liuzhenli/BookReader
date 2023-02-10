package com.micoredu.reader.net

import com.liuzhenli.common.utils.ApiManager
import com.micoredu.reader.bean.BookSource
import retrofit2.http.GET
import retrofit2.http.Headers

interface Api {

    @Headers("url_name:" + ApiManager.apiName.BOOK_SOURCE)
    @GET(ApiManager.apiName.BOOK_SOURCE)
    suspend fun getBookSource(): List<BookSource>
}
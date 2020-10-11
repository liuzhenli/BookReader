package com.micoredu.readerlib.impl;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * get web content
 */

public interface IHttpGetApi {
    @GET
    Observable<Response<String>> get(@Url String url,
                                     @HeaderMap Map<String, String> headers);

    @GET
    Observable<Response<String>> getMap(@Url String url,
                                        @QueryMap(encoded = true) Map<String, String> queryMap,
                                        @HeaderMap Map<String, String> headers);

}

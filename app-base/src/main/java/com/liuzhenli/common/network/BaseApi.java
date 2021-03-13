package com.liuzhenli.common.network;

import retrofit2.Retrofit;

/**
 * @author Liuzhenli
 * @since 2019-07-06 19:37
 */
public class BaseApi {
    public static BaseApi instance;

    protected BaseApiService service;

    public BaseApi(Retrofit retrofit) {
        service = retrofit.create(BaseApiService.class);
    }

    public static BaseApi getInstance(Retrofit retrofit) {
        if (instance == null)
            instance = new BaseApi(retrofit);
        return instance;
    }
}

package com.liuzhenli.common.network;

import com.liuzhenli.common.gson.CustomGsonConverterFactory;
import com.liuzhenli.common.utils.Constant;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @author Liuzhenli
 * @since 2019-07-06 19:37
 */
public class BaseApi {
    public static BaseApi instance;

    protected BaseApiService service;

    public BaseApi(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_BASE_URL)
                // 添加Rx适配器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                // 添加Gson转换器
                .addConverterFactory(CustomGsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        service = retrofit.create(BaseApiService.class);
    }

    public static BaseApi getInstance(OkHttpClient okHttpClient) {
        if (instance == null)
            instance = new BaseApi(okHttpClient);
        return instance;
    }
}

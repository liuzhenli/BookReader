package com.liuzhenli.reader.network;

import com.liuzhenli.common.base.BaseBean;
import com.liuzhenli.common.network.BaseApi;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author Liuzhenli
 * @since 2019-07-06 19:37
 */
public class Api extends BaseApi {

    private ApiService mApiService;


    public Api(Retrofit okHttpClient) {
        super(okHttpClient);
        this.mApiService = (ApiService) service;
    }

    public Observable<BaseBean> getLoginData(Map<String, String> params) {
        return mApiService.getLoginData(params);
    }
}

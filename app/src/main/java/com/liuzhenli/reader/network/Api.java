package com.liuzhenli.reader.network;

import com.liuzhenli.common.base.BaseBean;
import com.liuzhenli.common.network.BaseApi;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;

/**
 * @author Liuzhenli
 * @since 2019-07-06 19:37
 */
public class Api extends BaseApi {

    private ApiService mApiservice;


    public Api(OkHttpClient okHttpClient) {
        super(okHttpClient);
        this.mApiservice = (ApiService) service;
    }

    public Observable<BaseBean> getLoginData(Map<String, String> params) {
        return mApiservice.getLoginData(params);
    }
}

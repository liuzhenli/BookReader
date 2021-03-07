package com.liuzhenli.reader.network;

import com.liuzhenli.common.base.BaseBean;
import com.liuzhenli.common.gson.CustomGsonConverterFactory;
import com.liuzhenli.common.network.BaseApi;
import com.liuzhenli.common.utils.Constant;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

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

    public Observable<ResponseBody> getBookSource(String id) {
        return mApiservice.getBookSource(id);
    }
}

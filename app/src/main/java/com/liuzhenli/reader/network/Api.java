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
public class Api {

    private ApiService mApiService;
    private static Api mApi;

    public Api(Retrofit retrofit) {
        mApiService = retrofit.create(ApiService.class);
    }

    public static Api getInstance(Retrofit retrofit) {
        if (mApi == null) {
            mApi = new Api(retrofit);
        }
        return mApi;
    }

    public Observable<BaseBean> getLoginData(Map<String, String> params) {
        return mApiService.getLoginData(params);
    }
}

package com.liuzhenli.reader.network;

import com.liuzhenli.reader.base.BaseBean;
import com.liuzhenli.reader.gson.CustomGsonConverterFactory;
import com.liuzhenli.reader.utils.Constant;

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
public class Api {
    public static Api instance;

    private ApiService service;

    public Api(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_BASE_URL)
                // 添加Rx适配器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                // 添加Gson转换器
                .addConverterFactory(CustomGsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        service = retrofit.create(ApiService.class);
    }

    public static Api getInstance(OkHttpClient okHttpClient) {
        if (instance == null)
            instance = new Api(okHttpClient);
        return instance;
    }

    public Observable<BaseBean> getLoginData(Map<String, String> params) {
        return service.getLoginData(params);
    }

    public Observable<ResponseBody> getBookSource(String id) {
        return service.getBookSource(id);
    }
}

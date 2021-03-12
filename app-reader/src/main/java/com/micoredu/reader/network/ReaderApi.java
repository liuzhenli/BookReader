package com.micoredu.reader.network;

import com.liuzhenli.common.gson.CustomGsonConverterFactory;
import com.liuzhenli.common.network.BaseApi;
import com.liuzhenli.common.network.BaseApiService;
import com.liuzhenli.common.utils.Constant;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.http.Query;

/**
 * Description:
 *
 * @author liuzhenli 3/11/21
 * Email: 848808263@qq.com
 */
public class ReaderApi {

    private ReaderApiService mApiService;
    private static ReaderApi instance;

    public ReaderApi(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_BASE_URL)
                // 添加Rx适配器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                // 添加Gson转换器
                .addConverterFactory(CustomGsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        mApiService = retrofit.create(ReaderApiService.class);
    }

    public static ReaderApi getInstance(OkHttpClient okHttpClient) {
        if (instance == null)
            instance = new ReaderApi(okHttpClient);
        return instance;
    }

    public Observable<ResponseBody> getBookSource(String s) {
        return  mApiService. getBookSource(s);
    }
}

package com.micoredu.reader.network;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

/**
 * Description:
 *
 * @author liuzhenli 3/11/21
 * Email: 848808263@qq.com
 */
public class ReaderApi {

    private final ReaderApiService mApiService;
    private static ReaderApi instance;

    public ReaderApi(Retrofit retrofit) {
        mApiService = retrofit.create(ReaderApiService.class);
    }

    public static ReaderApi getInstance(Retrofit retrofit) {
        if (instance == null)
            instance = new ReaderApi(retrofit);
        return instance;
    }

    public Observable<ResponseBody> getBookSource(String s) {
        return mApiService.getBookSource(s);
    }
}

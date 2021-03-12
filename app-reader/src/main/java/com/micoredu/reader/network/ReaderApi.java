package com.micoredu.reader.network;

import com.liuzhenli.common.network.BaseApi;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

/**
 * Description:
 *
 * @author liuzhenli 3/11/21
 * Email: 848808263@qq.com
 */
public class ReaderApi extends BaseApi {

    private ReaderApiService mApiservice;

    public ReaderApi(OkHttpClient okHttpClient) {
        super(okHttpClient);
        this.mApiservice = (ReaderApiService) service;
    }

    public Observable<ResponseBody> getBookSource(String id) {
        return mApiservice.getBookSource(id);
    }
}

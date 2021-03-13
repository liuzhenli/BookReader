package com.liuzhenli.write.network;

import retrofit2.Retrofit;

/**
 * Description:
 *
 * @author liuzhenli 3/13/21
 * Email: 848808263@qq.com
 */
public class WriteApi {


    private WriteApiService mWriteApiService;
    private static WriteApi mWriteApi;

    public WriteApi(Retrofit retrofit) {
        mWriteApiService = retrofit.create(WriteApiService.class);
    }

    public static WriteApi getInstance(Retrofit retrofit) {
        if (mWriteApi == null) {
            mWriteApi = new WriteApi(retrofit);
        }
        return mWriteApi;
    }
}

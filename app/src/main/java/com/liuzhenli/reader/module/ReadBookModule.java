package com.liuzhenli.reader.module;

import com.liuzhenli.reader.network.Api;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Description:
 *
 * @author liuzhenli 3/13/21
 * Email: 848808263@qq.com
 */
@Module
public class ReadBookModule {

    @Singleton
    @Provides
    public Api provideApi(Retrofit retrofit) {
        return Api.getInstance(retrofit);
    }

}

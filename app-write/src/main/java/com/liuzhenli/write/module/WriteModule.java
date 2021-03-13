package com.liuzhenli.write.module;

import com.liuzhenli.write.network.WriteApi;

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
public class WriteModule {

    @Singleton
    @Provides
    public WriteApi provideWriteApi(Retrofit retrofit) {
        return WriteApi.getInstance(retrofit);
    }

}

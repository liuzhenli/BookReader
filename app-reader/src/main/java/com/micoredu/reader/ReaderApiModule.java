


package com.micoredu.reader;


import com.liuzhenli.common.BaseApplication;
import com.liuzhenli.common.network.support.HeaderInterceptor;
import com.liuzhenli.common.network.support.LoggingInterceptor;
import com.liuzhenli.common.network.support.RewriteCacheControlInterceptor;
import com.micoredu.reader.network.ReaderApi;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

@Module
public class ReaderApiModule {

    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient() {

        OkHttpClient.Builder builder = null;
        try {
            LoggingInterceptor logging = new LoggingInterceptor(new MyLog());
            if (BaseApplication.isDebug) {
                logging.setLevel(LoggingInterceptor.Level.BODY);
            } else {
                logging.setLevel(LoggingInterceptor.Level.NONE);
            }

            HeaderInterceptor headerInterceptor = new HeaderInterceptor();

            File cacheFile = new File(BaseApplication.getInstance().getCacheDir(), "[缓存目录]");
            RewriteCacheControlInterceptor rewrite_cache_control_interceptor = new RewriteCacheControlInterceptor();

            builder = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    // 失败重发
                    .retryOnConnectionFailure(false)
                    .addInterceptor(headerInterceptor)
                    .cache(new Cache(cacheFile, 1024 * 1024 * 100))
                    .addInterceptor(logging)
                    .addInterceptor(rewrite_cache_control_interceptor)
                    .addNetworkInterceptor(rewrite_cache_control_interceptor)
            ;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder.build();
    }


    @Singleton
    @Provides
    protected ReaderApi provideBookService(OkHttpClient okHttpClient) {
        return ReaderApi.getInstance(okHttpClient);
    }

    public static class MyLog implements LoggingInterceptor.LoggerM {
        @Override
        public void log(String message) {
            Logger.i("oklog: " + message);
        }
    }
}
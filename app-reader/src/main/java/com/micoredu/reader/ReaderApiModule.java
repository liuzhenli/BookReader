package com.micoredu.reader;

import com.micoredu.reader.network.ReaderApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ReaderApiModule {

    @Singleton
    @Provides
    protected ReaderApi provideBookService(Retrofit retrofit) {
        return ReaderApi.getInstance(retrofit);
    }
}
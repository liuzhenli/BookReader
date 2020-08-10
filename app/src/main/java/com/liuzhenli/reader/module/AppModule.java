package com.liuzhenli.reader.module;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

/**
 * @author Liuzhenli
 * @since 2019-07-06 16:28
 */
@Module
public class AppModule {
    private Application application;

    public AppModule(Application application){
        this.application=application;
    }

    @Provides
    public Application provideApplication(){
        return application;
    }
}

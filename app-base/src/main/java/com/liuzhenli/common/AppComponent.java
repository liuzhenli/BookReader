package com.liuzhenli.common;

import com.liuzhenli.common.module.ApiModule;
import com.liuzhenli.common.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Liuzhenli
 * @since 19/7/6
 */
@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {
}

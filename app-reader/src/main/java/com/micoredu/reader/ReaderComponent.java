package com.micoredu.reader;

import com.liuzhenli.common.module.ApiModule;
import com.liuzhenli.common.module.AppModule;
import com.micoredu.reader.ui.history.ReadHistoryActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Description:
 *
 * @author liuzhenli 3/11/21
 * Email: 848808263@qq.com
 */
@Singleton
@Component(modules = {ReaderApiModule.class, AppModule.class, ApiModule.class})
public interface ReaderComponent {

    void inject(ReadHistoryActivity readHistoryActivity);
}

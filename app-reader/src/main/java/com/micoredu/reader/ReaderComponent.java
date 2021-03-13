package com.micoredu.reader;

import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.module.ApiModule;
import com.liuzhenli.common.module.AppModule;
import com.liuzhenli.common.scope.ActivityScope;
import com.micoredu.reader.ui.activity.BookSourceActivity;
import com.micoredu.reader.ui.activity.DownloadActivity;
import com.micoredu.reader.ui.activity.EditSourceActivity;
import com.micoredu.reader.ui.activity.ReaderActivity;
import com.micoredu.reader.ui.activity.TestSourceActivity;

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
    void inject(ReaderActivity activity);

    void inject(BookSourceActivity bookSourceActivity);

    void inject(DownloadActivity downloadActivity);

    void inject(EditSourceActivity editSourceActivity);

    void inject(TestSourceActivity testSourceActivity);
}

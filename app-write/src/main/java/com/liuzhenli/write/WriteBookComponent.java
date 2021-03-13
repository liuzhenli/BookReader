package com.liuzhenli.write;

import com.liuzhenli.common.module.ApiModule;
import com.liuzhenli.common.module.AppModule;
import com.liuzhenli.write.module.WriteModule;
import com.liuzhenli.write.ui.activity.WriteBookActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Description:
 *
 * @author liuzhenli 3/11/21
 * Email: 848808263@qq.com
 */

@Singleton
@Component(modules = {ApiModule.class, AppModule.class, WriteModule.class})
public interface WriteBookComponent {
    void inject(WriteBookActivity writeBookActivity);
}

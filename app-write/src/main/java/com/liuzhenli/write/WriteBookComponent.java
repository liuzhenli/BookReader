package com.liuzhenli.write;

import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.scope.ActivityScope;

import dagger.Component;

/**
 * Description:
 *
 * @author liuzhenli 3/11/21
 * Email: 848808263@qq.com
 */

@ActivityScope
@Component(dependencies = AppComponent.class)
public interface WriteBookComponent {
    void inject(WriteBookActivity writeBookActivity);
}

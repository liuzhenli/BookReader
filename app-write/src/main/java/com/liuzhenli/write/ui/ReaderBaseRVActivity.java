package com.liuzhenli.write.ui;

import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.base.BaseContract;
import com.liuzhenli.common.base.BaseRvActivity;
import com.liuzhenli.write.DaggerWriteBookComponent;
import com.liuzhenli.write.WriteBookComponent;
import com.liuzhenli.write.module.WriteModule;

/**
 * Description:
 *
 * @author liuzhenli 3/13/21
 * Email: 848808263@qq.com
 */
public abstract class ReaderBaseRVActivity<T1 extends BaseContract.BasePresenter, T2> extends BaseRvActivity<T1 , T2> {

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        WriteBookComponent readerComponent = DaggerWriteBookComponent.builder()
                .writeModule(new WriteModule())
                .build();
        setupActivityComponent(readerComponent);

    }

    protected abstract void setupActivityComponent(WriteBookComponent appComponent);
}

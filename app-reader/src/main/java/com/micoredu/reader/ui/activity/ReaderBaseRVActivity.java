package com.micoredu.reader.ui.activity;

import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.base.BaseContract;
import com.liuzhenli.common.base.BaseRvActivity;
import com.micoredu.reader.DaggerReaderComponent;
import com.micoredu.reader.ReaderApiModule;
import com.micoredu.reader.ReaderComponent;

/**
 * Description:
 *
 * @author liuzhenli 3/13/21
 * Email: 848808263@qq.com
 */
public abstract class ReaderBaseRVActivity<T1 extends BaseContract.BasePresenter, T2> extends BaseRvActivity<T1 , T2> {

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        ReaderComponent readerComponent = DaggerReaderComponent.builder()
                .readerApiModule(new ReaderApiModule())
                .build();
        setupActivityComponent(readerComponent);

    }

    protected abstract void setupActivityComponent(ReaderComponent appComponent);
}

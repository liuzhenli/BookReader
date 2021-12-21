package com.micoredu.reader;

import androidx.viewbinding.ViewBinding;

import com.liuzhenli.common.AppComponent;
import com.liuzhenli.common.base.BaseActivity;
import com.liuzhenli.common.base.BaseContract;
import com.micoredu.reader.DaggerReaderComponent;

/**
 * Description:
 *
 * @author liuzhenli 3/13/21
 * Email: 848808263@qq.com
 */
public abstract class ReaderBaseActivity<T extends BaseContract.BasePresenter, V extends ViewBinding> extends BaseActivity<T, V> {

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        ReaderComponent readerComponent = DaggerReaderComponent.builder()
                .readerApiModule(new ReaderApiModule())
                .build();
        setupActivityComponent(readerComponent);

    }

    protected abstract void setupActivityComponent(ReaderComponent appComponent);
}

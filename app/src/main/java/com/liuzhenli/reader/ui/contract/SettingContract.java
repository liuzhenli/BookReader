package com.liuzhenli.reader.ui.contract;

import com.liuzhenli.reader.base.BaseContract;

/**
 * Description:
 *
 * @author liuzhenli 2021/2/2
 * Email: 848808263@qq.com
 */
public class SettingContract {
    public interface View extends BaseContract.BaseView {
        void showCacheSize(String size);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getCacheSize();
    }
}

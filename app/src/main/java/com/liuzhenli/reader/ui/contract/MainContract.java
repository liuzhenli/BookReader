package com.liuzhenli.reader.ui.contract;

import com.liuzhenli.common.base.BaseContract;

/**
 * Description:
 *
 * @author liuzhenli 2021/7/22
 * Email: 848808263@qq.com
 */
public class MainContract {
    public interface View extends BaseContract.BaseView {
        void showCheckBackupPathResult(boolean isEmpty);

        /**
         * show if user has set webDav
         *
         * @param isSet true if is set
         */
        void showWebDavResult(boolean isSet);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void checkBackupPath();

        /**
         * check if user has set webdav
         */
        void checkWebDav();
    }
}

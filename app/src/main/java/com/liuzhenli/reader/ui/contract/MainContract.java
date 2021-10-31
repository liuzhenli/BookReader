package com.liuzhenli.reader.ui.contract;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.liuzhenli.common.base.BaseContract;
import com.micoredu.reader.bean.BookSourceBean;

import java.util.List;

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

        void showBookSource(List<BookSourceBean> list);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void checkBackupPath();

        /**
         * check if user has set webdav
         */
        void checkWebDav();

        void dealDefaultFonts();

        void importSource(String url);

        void importSourceFromLocal(@NonNull Uri uri);
    }
}

package com.liuzhenli.reader.ui.contract;

import com.liuzhenli.reader.base.BaseContract;
import com.micoredu.readerlib.bean.BookSourceBean;

/**
 * Description:
 *
 * @author liuzhenli 2020/11/16
 * Email: 848808263@qq.com
 */
public class EditSourceContract {
    public interface View extends BaseContract.BaseView {
        void showSaveBookResult();
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void saveBookSource(BookSourceBean data);
    }
}

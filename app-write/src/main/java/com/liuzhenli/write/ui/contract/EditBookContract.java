package com.liuzhenli.write.ui.contract;

import com.liuzhenli.common.base.BaseContract;
import com.liuzhenli.write.bean.WriteBook;

/**
 * Description:
 *
 * @author liuzhenli 2021/5/14
 * Email: 848808263@qq.com
 */
public class EditBookContract {
    public interface View extends BaseContract.BaseView {
        void showSaveResult(long data);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void saveBooks(WriteBook book);
    }
}

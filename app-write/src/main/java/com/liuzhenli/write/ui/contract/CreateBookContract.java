package com.liuzhenli.write.ui.contract;

import com.liuzhenli.common.base.BaseContract;
import com.liuzhenli.write.bean.WriteBook;

import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2021/5/14
 * Email: 848808263@qq.com
 */
public class CreateBookContract {
    public interface View extends BaseContract.BaseView {
        void showAllCreateBooks(List<WriteBook> books);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getCreateBooks();
    }
}

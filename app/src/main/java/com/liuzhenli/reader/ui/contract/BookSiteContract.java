package com.liuzhenli.reader.ui.contract;

import com.liuzhenli.reader.base.BaseBean;
import com.liuzhenli.reader.base.BaseContract;

/**
 * Description:
 *
 * @author liuzhenli 2020/10/9
 * Email: 848808263@qq.com
 */
public class BookSiteContract {
    public interface View extends BaseContract.BaseView {
        void showBookList(BaseBean data);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getBookList();
    }
}

package com.liuzhenli.reader.ui.contract

import com.liuzhenli.common.base.BaseContract
import com.micoredu.reader.bean.BookShelfBean

/**
 * Description:
 *
 * @author  liuzhenli 2021/10/19
 * Email: 848808263@qq.com
 */
class ManageBookshelfContract {
    public interface View : BaseContract.BaseView {
        fun showBookList(books: BookShelfBean)
    }

    public interface Presenter<T> : BaseContract.BasePresenter<T> {
        fun getBookList();
    }
}
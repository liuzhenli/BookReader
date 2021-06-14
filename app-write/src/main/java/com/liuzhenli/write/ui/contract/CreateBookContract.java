package com.liuzhenli.write.ui.contract;

import com.liuzhenli.common.base.BaseContract;
import com.liuzhenli.write.bean.WriteBook;
import com.liuzhenli.write.bean.WriteChapter;

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

        void showChapterList(List<WriteChapter> chapters);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getCreateBooks();

        void getChapterList(long bookId);
    }
}

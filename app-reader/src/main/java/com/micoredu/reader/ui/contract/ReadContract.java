package com.micoredu.reader.ui.contract;

import com.liuzhenli.common.base.BaseContract;
import com.micoredu.reader.bean.BookInfoBean;
import com.micoredu.reader.bean.BookShelfBean;

import java.io.File;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-11-10 12:30
 */
public class ReadContract {
    public interface View extends BaseContract.BaseView {
        String getNoteUrl();

        //判断是否在书架
        boolean isInBookShelf();

        /**
         * 显示书的信息
         *
         * @param bookInfo 书信息
         */
        void showBookInfo(BookShelfBean bookInfo);

        void showFontFile(File[] files);

        BookShelfBean getBookShelf();

        void showChangeBookSourceResult(BookShelfBean book);

    }


    public interface Presenter<T> extends BaseContract.BasePresenter<T> {



        /***获取书的信息*/
        void getBookInfo(String url);

        void updateBookInfo(BookInfoBean bookInfo);

        void saveProgress(BookShelfBean bookShelf);

        void getFontFile();

        void changeBookSource();

        void saveReadHistory();
    }


}

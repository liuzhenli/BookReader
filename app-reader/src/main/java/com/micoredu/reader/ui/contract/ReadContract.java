package com.micoredu.reader.ui.contract;

import com.liuzhenli.common.base.BaseContract;
import com.micoredu.reader.bean.Book;
import com.micoredu.reader.model.webBook.BookInfo;

import java.io.File;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-11-10 12:30
 */
public class ReadContract {
    public interface View extends BaseContract.BaseView {
        String getBookUrl();

        //判断是否在书架
        boolean isInBookShelf();

        /**
         * 显示书的信息
         *
         * @param bookInfo 书信息
         */
        void showBookInfo(Book bookInfo);

        void showFontFile(File[] files);

        Book getBookShelf();

        void showChangeBookSourceResult(Book book);

    }


    public interface Presenter<T> extends BaseContract.BasePresenter<T> {



        /***获取书的信息*/
        void getBookInfo(String url);

        void updateBookInfo(BookInfo bookInfo);

        void saveProgress(Book bookShelf);

        void getFontFile();

        void changeBookSource();

        void saveReadHistory();
    }


}

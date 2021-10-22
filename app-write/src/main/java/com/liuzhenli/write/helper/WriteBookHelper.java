package com.liuzhenli.write.helper;

import com.liuzhenli.write.bean.WriteBook;
import com.liuzhenli.write.bean.WriteChapter;
import com.liuzhenli.write.data.WriteBookDao;
import com.liuzhenli.write.data.WriteChapterDao;

import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2021/5/13
 * Email: 848808263@qq.com
 */
public class WriteBookHelper {

    /**
     * @param book bookInfo
     * @return row ID of newly inserted entity
     */
    public static long saveWriteBook(WriteBook book) {
        return WriteDbHelper.getInstance().getAppDatabase().getWriteBookDao().insertOrReplace(book);
    }

    public static List<WriteBook> getAllBooks() {
        return WriteDbHelper.getInstance().getAppDatabase().getWriteBookDao().getAllBooks();
    }

    public static WriteBook getBookById(long bookId) {
        return WriteDbHelper.getInstance().getAppDatabase().getWriteBookDao().getBookById(bookId);
    }

    public static void deleteBook(WriteBook writeBook) {
        WriteDbHelper.getInstance().getAppDatabase().getWriteBookDao().delete(writeBook);
    }

    public static void deleteBook(long bookId) {
        WriteDbHelper.getInstance().getAppDatabase().getWriteBookDao().deleteByBookId(bookId);
    }

    public static List<WriteChapter> getChapterList(long localBookId) {
        return WriteDbHelper.getInstance().getAppDatabase().getWriteChapterDao().getChapterList(localBookId);
    }

    public static void saveChapterInfo(WriteChapter writeChapter) {
        WriteDbHelper.getInstance().getAppDatabase().getWriteChapterDao().insertOrReplace(writeChapter);
    }
}

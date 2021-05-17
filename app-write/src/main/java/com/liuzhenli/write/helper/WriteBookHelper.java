package com.liuzhenli.write.helper;

import com.liuzhenli.write.bean.WriteBook;
import com.liuzhenli.write.greendao.WriteBookDao;

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
        return WriteDbHelper.getInstance().getDaoSession().getWriteBookDao().insertOrReplace(book);
    }

    public static List<WriteBook> getAllBooks() {
        return WriteDbHelper.getInstance().getDaoSession().getWriteBookDao().queryBuilder().list();
    }

    public static WriteBook getBookById(long bookId) {
        return WriteDbHelper.getInstance().getDaoSession().getWriteBookDao().queryBuilder()
                .where(WriteBookDao.Properties.BookId.eq(bookId)).limit(1).build().unique();
    }

    public static void deleteBook(WriteBook writeBook) {
        WriteDbHelper.getInstance().getDaoSession().getWriteBookDao().delete(writeBook);
    }

    public static void deleteBook(long bookId) {
        WriteDbHelper.getInstance().getDaoSession().getWriteBookDao().deleteByKey(bookId);
    }
}

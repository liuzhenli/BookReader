package com.micoredu.reader.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.jayway.jsonpath.Criteria;
import com.micoredu.reader.bean.BookSourceBean;

import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2021/6/28
 * Email: 848808263@qq.com
 */
@Dao
public interface BookSourceDao {
    String TABLENAME = "booksources";

    @Query("select * from booksources order by SerialNumber ASC")
    List<BookSourceBean> getAllBookSource();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplace(BookSourceBean bookSourceBean);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplaceInTx(List<BookSourceBean> beanList);

    @Query("DELETE FROM booksources WHERE bookSourceUrl IS:bookSourceUrl")
    void delete(String bookSourceUrl);

    @Delete()
    void delete(BookSourceBean bookSourceUrl);

    @Query("select Count(bookSourceUrl) from booksources")
    int count();

    @Query("SELECT * FROM booksources WHERE bookSourceUrl IS:bookSourceUrl")
    BookSourceBean getByBookSourceUrl(String bookSourceUrl);

    @Query("SELECT * FROM booksources WHERE enable is 1 order by SerialNumber asc")
    List<BookSourceBean> getSelectedBookSource();

    @Query("select * from booksources where enable =1 order by SerialNumber ASC")
    List<BookSourceBean> getSelectedBookSourceBySerialNumber();

    @Query("select * from booksources where BookSourceName like '%'||:keyword||'%'  " +
            "or BookSourceGroup like '%'||:keyword||'%' or BookSourceUrl like '%'||:keyword||'%'" +
            " order by SerialNumber ASC")
    List<BookSourceBean> getSourceByKey(String keyword);

    @Query("select * from booksources where enable =1 and ruleFindEnable =1 order by SerialNumber ASC")
    List<BookSourceBean> getRuleFindEnable();

}

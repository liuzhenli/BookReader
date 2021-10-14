package com.micoredu.reader.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.micoredu.reader.bean.ReadHistory;

import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2021/6/28
 * Email: 848808263@qq.com
 */
@Dao
public interface ReadHistoryDao {
    @Query("select * from readHistory")
    List<ReadHistory> getAll();

    @Query("select id,bookCover,type,bookName,authorName,noteUrl,dayMillis, sum(sumTime) as sumTime from readHistory group by bookName order by dayMillis")
    List<ReadHistory> getAllGroupByBookName();

    @Query("select * from readHistory where bookName=:bookName and dayMillis=:dayMillis")
    ReadHistory getByBookName(String bookName, long dayMillis);

    @Query("select sum(sumTime) from readHistory")
    long getAllTime();

    @Query("select sum(sumTime) from readHistory where dayMillis=:dayMillis")
    long getTodayAllTime(long dayMillis);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplace(ReadHistory readHistory);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplace(List<ReadHistory> readHistory);

    @Query("delete from readHistory where bookName = :bookName")
    void deleteByBookName(String bookName);

    @Query("delete from readHistory")
    void clear();
}

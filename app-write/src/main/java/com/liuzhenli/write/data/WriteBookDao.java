package com.liuzhenli.write.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.liuzhenli.write.bean.WriteBook;

import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2021/6/28
 * Email: 848808263@qq.com
 */
@Dao
public interface WriteBookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOrReplace(WriteBook book);

    @Query("select * from writeBook")
    List<WriteBook> getAllBooks();

    @Query("select * from writebook where bookId is :bookId limit 1")
    WriteBook getBookById(long bookId);

    @Delete
    void delete(WriteBook writeBook);

    @Query("delete from writebook where bookId = :bookId")
    void deleteByBookId(long bookId);
}

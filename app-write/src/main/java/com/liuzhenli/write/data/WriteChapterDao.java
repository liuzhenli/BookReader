package com.liuzhenli.write.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.liuzhenli.write.bean.WriteChapter;

import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2021/6/28
 * Email: 848808263@qq.com
 */
@Dao
public interface WriteChapterDao {
    @Query("select * from writechapter where bookId =:bookId")
    List<WriteChapter> getChapterList(long bookId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplace(WriteChapter writeChapter);

    @Query("select * from writechapter where createTime = :createTime")
    WriteChapter getChapterByCreateTime(long createTime);

    @Delete
    void delete(WriteChapter mChapter);

    @Query("select * from writechapter where bookId = :bId and chapterId=:cId")
    WriteChapter getWriteChapter(long bId, long cId);
}

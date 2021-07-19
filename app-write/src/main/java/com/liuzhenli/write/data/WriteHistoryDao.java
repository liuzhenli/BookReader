package com.liuzhenli.write.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.liuzhenli.write.bean.WriteHistory;
import com.liuzhenli.write.util.QETag;

/**
 * Description:
 *
 * @author liuzhenli 2021/6/28
 * Email: 848808263@qq.com
 */
@Dao
public interface WriteHistoryDao {

    @Query("SELECT * FROM writehistory WHERE localBookId IS :bId AND localChapterId IS :cId")
    WriteHistory getWriteHistory(long bId, long cId);

    @Insert
    void insert(WriteHistory history);
}

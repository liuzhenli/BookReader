package com.liuzhenli.write.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.liuzhenli.write.bean.WriteBook;
import com.liuzhenli.write.bean.WriteChapter;
import com.liuzhenli.write.bean.WriteHistory;

/**
 * Description:
 *
 * @author liuzhenli 2021/6/28
 * Email: 848808263@qq.com
 */
@Database(entities = {WriteBook.class, WriteChapter.class, WriteHistory.class},
        version = 1, exportSchema = false)
public abstract class AppWriteDatabase extends RoomDatabase {

    @Override
    public void clearAllTables() {

    }

    public abstract WriteBookDao getWriteBookDao();

    public abstract WriteChapterDao getWriteChapterDao();

    public abstract WriteHistoryDao getWriteHistoryDao();
}

package com.micoredu.reader.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.micoredu.reader.bean.TxtChapterRuleBean;

import java.util.List;
import java.util.stream.DoubleStream;

/**
 * Description:
 *
 * @author liuzhenli 2021/6/28
 * Email: 848808263@qq.com
 */
@Dao
public interface TxtChapterRuleDao {
    @Query("select * from txtChapterRule order by serialNumber")
    List<TxtChapterRuleBean> loadAll();

    @Query("select * from txtChapterRule where enable =1")
    List<TxtChapterRuleBean> getEnabled();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplaceInTx(List<TxtChapterRuleBean> ruleBeanList);

    @Delete
    void delete(TxtChapterRuleBean txtChapterRuleBean);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplace(TxtChapterRuleBean txtChapterRuleBean);

}

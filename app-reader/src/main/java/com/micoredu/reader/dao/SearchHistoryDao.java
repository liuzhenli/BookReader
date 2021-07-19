package com.micoredu.reader.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.micoredu.reader.bean.SearchHistoryBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2021/6/28
 * Email: 848808263@qq.com
 */
@Dao
public interface SearchHistoryDao {

    @Query("SELECT * FROM searchHistory ORDER BY date DESC")
    List<SearchHistoryBean> all();

    @Delete
    void delete(SearchHistoryBean data);

    @Query("Delete from searchHistory")
    void deleteAll();

    @Update
    void update(SearchHistoryBean searchHistoryBean);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplaceInTx(SearchHistoryBean searchHistoryBean);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplaceInTx(@NotNull List<? extends SearchHistoryBean> it);

    @Query("SELECT * FROM searchHistory WHERE type is :type and content is :searchKey LIMIT 1")
    List<SearchHistoryBean> getHistoryByType(int type, String searchKey);


}

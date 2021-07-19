package com.micoredu.reader.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.micoredu.reader.bean.CookieBean;

/**
 * Description:
 *
 * @author liuzhenli 2021/6/28
 * Email: 848808263@qq.com
 */
@Dao
public interface CookieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplace(CookieBean cookieBean);

    @Query("select * from cookies where url is:bookSourceUrl")
    CookieBean load(String bookSourceUrl);
}

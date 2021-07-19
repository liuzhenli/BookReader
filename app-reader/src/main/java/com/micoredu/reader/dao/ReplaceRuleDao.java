package com.micoredu.reader.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.jayway.jsonpath.Criteria;
import com.micoredu.reader.bean.ReplaceRuleBean;

import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2021/6/28
 * Email: 848808263@qq.com
 */
@Dao
public interface ReplaceRuleDao {
    @Query("select * from replaceRule  order by serialNumber asc")
    List<ReplaceRuleBean> getAll();

    @Query("select * from replaceRule where enable = 1 order by serialNumber asc")
    List<ReplaceRuleBean> getEnabled();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplace(ReplaceRuleBean replaceRuleBean);

    @Delete
    void delete(ReplaceRuleBean li);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplaceInTx(List<ReplaceRuleBean> replaceRuleBeans);

    @Query("select count(id) from replaceRule")
    int count();

    @Query("select * from replaceRule where enable = :enable and replaceSummary =:replaceSummary and serialNumber <>:sn order by serialNumber asc")
    List<ReplaceRuleBean> getByProperties(int enable, String replaceSummary, int sn);
}

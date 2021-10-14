package com.micoredu.reader.bean;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Description:浏览历史
 *
 * @author liuzhenli 2021/1/29
 * Email: 848808263@qq.com
 */
@Entity(tableName = "readHistory")
public class ReadHistory {
    @PrimaryKey(autoGenerate = true)
    public Long id = null;
    public String bookCover;
    public String type;
    public String bookName;
    public String authorName;
    public String noteUrl;
    /**
     * 时间戳,每天的唯一标识
     */
    public long dayMillis;
    /**
     * 当天阅读总的时长
     */
    public long sumTime;
}

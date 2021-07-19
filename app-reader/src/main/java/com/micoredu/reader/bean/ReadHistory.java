package com.micoredu.reader.bean;


import androidx.annotation.NonNull;
import androidx.room.Entity;

/**
 * Description:浏览历史
 *
 * @author liuzhenli 2021/1/29
 * Email: 848808263@qq.com
 */
@Entity(tableName = "readHistory", primaryKeys = "noteUrl")
public class ReadHistory {

    public String bookCover;
    public int type;
    public String bookName;
    public String authorName;
    @NonNull
    public String noteUrl;
    public long lastReadTime;
    public long startTime;
    /**
     * 阅读总的时长
     */
    public long readTotalTime;
}

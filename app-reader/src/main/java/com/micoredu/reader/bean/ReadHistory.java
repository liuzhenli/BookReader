package com.micoredu.reader.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Description:浏览历史
 *
 * @author liuzhenli 2021/1/29
 * Email: 848808263@qq.com
 */
@Entity
public class ReadHistory {


    public String bookCover;
    public int type;
    public String bookName;
    public String authorName;
    public String noteUrl;
    public long lastReadTime;
    public long startTime;
    /**
     * 阅读总的时长
     */
    public long readTotalTime;
    @Generated(hash = 1986717428)
    public ReadHistory(String bookCover, int type, String bookName,
                       String authorName, String noteUrl, long lastReadTime, long startTime,
                       long readTotalTime) {
        this.bookCover = bookCover;
        this.type = type;
        this.bookName = bookName;
        this.authorName = authorName;
        this.noteUrl = noteUrl;
        this.lastReadTime = lastReadTime;
        this.startTime = startTime;
        this.readTotalTime = readTotalTime;
    }
    @Generated(hash = 1863927908)
    public ReadHistory() {
    }
    public String getBookCover() {
        return this.bookCover;
    }
    public void setBookCover(String bookCover) {
        this.bookCover = bookCover;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getBookName() {
        return this.bookName;
    }
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
    public String getAuthorName() {
        return this.authorName;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    public String getNoteUrl() {
        return this.noteUrl;
    }
    public void setNoteUrl(String noteUrl) {
        this.noteUrl = noteUrl;
    }
    public long getLastReadTime() {
        return this.lastReadTime;
    }
    public void setLastReadTime(long lastReadTime) {
        this.lastReadTime = lastReadTime;
    }
    public long getStartTime() {
        return this.startTime;
    }
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public long getReadTotalTime() {
        return this.readTotalTime;
    }
    public void setReadTotalTime(long readTotalTime) {
        this.readTotalTime = readTotalTime;
    }

}

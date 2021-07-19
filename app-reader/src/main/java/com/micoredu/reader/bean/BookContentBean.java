package com.micoredu.reader.bean;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * bookContent
 */
@Entity(tableName = "bookContents", primaryKeys = "durChapterUrl")
public class BookContentBean {

    /***对应BookInfoBean noteUrl*/
    private String noteUrl;
    @NonNull
    private String durChapterUrl;

    /***当前章节  （包括番外）*/
    private int durChapterIndex;

    /***当前章节内容*/
    private String durChapterContent;
    /***来源  某个网站/本地*/
    private String tag;

    private Long timeMillis;

    public BookContentBean() {

    }

    public String getDurChapterUrl() {
        return durChapterUrl;
    }

    public void setDurChapterUrl(String durChapterUrl) {
        this.durChapterUrl = durChapterUrl;
    }

    public int getDurChapterIndex() {
        return durChapterIndex;
    }

    public void setDurChapterIndex(int durChapterIndex) {
        this.durChapterIndex = durChapterIndex;
    }

    public String getDurChapterContent() {
        return durChapterContent;
    }

    public void setDurChapterContent(String durChapterContent) {
        this.durChapterContent = durChapterContent;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getNoteUrl() {
        return this.noteUrl;
    }

    public void setNoteUrl(String noteUrl) {
        this.noteUrl = noteUrl;
    }

    public Long getTimeMillis() {
        return this.timeMillis;
    }

    public void setTimeMillis(Long timeMillis) {
        this.timeMillis = timeMillis;
    }

    public boolean outTime() {
        if (timeMillis == null) {
            return true;
        }
        return timeMillis < System.currentTimeMillis();
    }

}

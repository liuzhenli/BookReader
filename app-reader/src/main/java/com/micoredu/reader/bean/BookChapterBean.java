package com.micoredu.reader.bean;

import androidx.room.Entity;
import androidx.room.Ignore;

import com.google.gson.Gson;
import com.micoredu.reader.helper.BookshelfHelper;


import java.util.Objects;

/**
 * 章节列表
 */
@Entity(tableName = "bookChapters", primaryKeys = "durChapterUrl")
public class BookChapterBean extends BaseChapterBean implements Cloneable {

    /***章节内容在文章中的起始位置(本地)**/
    private Long start;
    /***章节内容在文章中的终止位置(本地)*/
    private Long end;

    public BookChapterBean() {
    }

    @Ignore
    public BookChapterBean(String tag, String durChapterName, String durChapterUrl) {
        this.tag = tag;
        this.durChapterName = durChapterName;
        this.durChapterUrl = durChapterUrl;
    }

    @Override
    protected Object clone() {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(this);
            return gson.fromJson(json, BookChapterBean.class);
        } catch (Exception ignored) {
        }
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BookChapterBean) {
            BookChapterBean bookChapterBean = (BookChapterBean) obj;
            return Objects.equals(bookChapterBean.durChapterUrl, durChapterUrl);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return durChapterUrl.hashCode();
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDurChapterName() {
        return this.durChapterName;
    }

    public void setDurChapterName(String durChapterName) {
        this.durChapterName = durChapterName;
    }

    public String getDurChapterUrl() {
        return this.durChapterUrl;
    }

    public void setDurChapterUrl(String durChapterUrl) {
        this.durChapterUrl = durChapterUrl;
    }

    public int getDurChapterIndex() {
        return this.durChapterIndex;
    }

    public void setDurChapterIndex(int durChapterIndex) {
        this.durChapterIndex = durChapterIndex;
    }

    public String getNoteUrl() {
        return this.noteUrl;
    }

    public void setNoteUrl(String noteUrl) {
        this.noteUrl = noteUrl;
    }

    public Long getStart() {
        return this.start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return this.end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public Boolean getHasCache(BookInfoBean bookInfoBean) {
        return BookshelfHelper.isChapterCached(bookInfoBean.getName(), tag, this, bookInfoBean.isAudio());
    }

}

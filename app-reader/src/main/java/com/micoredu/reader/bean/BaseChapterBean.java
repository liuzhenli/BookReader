package com.micoredu.reader.bean;


import androidx.annotation.NonNull;

public class BaseChapterBean {
    protected String tag;
    /***当前章节对应的文章地址***/
    @NonNull
    protected String durChapterUrl = "";

    /***当前章节数***/
    protected int durChapterIndex;

    /***对应BookInfoBean noteUrl;**/
    protected String noteUrl;

    /***当前章节名称*/
    protected String durChapterName;


    public String getDurChapterUrl() {
        return durChapterUrl;
    }

    public void setDurChapterUrl(String durChapterUrl) {
        this.durChapterUrl = durChapterUrl;
    }

    public String getDurChapterName() {
        return durChapterName;
    }

    public void setDurChapterName(String durChapterName) {
        this.durChapterName = durChapterName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    public String getNoteUrl() {
        return noteUrl;
    }

    public void setNoteUrl(String noteUrl) {
        this.noteUrl = noteUrl;
    }

    public int getDurChapterIndex() {
        return durChapterIndex;
    }

    public void setDurChapterIndex(int durChapterIndex) {
        this.durChapterIndex = durChapterIndex;
    }
}

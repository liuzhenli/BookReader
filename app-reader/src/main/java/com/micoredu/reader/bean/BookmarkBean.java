package com.micoredu.reader.bean;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookMarks")
public class BookmarkBean implements Cloneable {

    @PrimaryKey
    private Long id = System.currentTimeMillis();
    private String noteUrl;
    private String bookName;
    private String chapterName;
    private Integer chapterIndex;
    private Integer pageIndex;
    private String content;


    @Ignore
    public BookmarkBean(Long id, String noteUrl, String bookName, String chapterName,
                        Integer chapterIndex, Integer pageIndex, String content) {
        this.id = id;
        this.noteUrl = noteUrl;
        this.bookName = bookName;
        this.chapterName = chapterName;
        this.chapterIndex = chapterIndex;
        this.pageIndex = pageIndex;
        this.content = content;
    }

    public BookmarkBean() {
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        BookmarkBean bookmarkBean = (BookmarkBean) super.clone();
        bookmarkBean.id = id;
        bookmarkBean.noteUrl = noteUrl;
        bookmarkBean.bookName = bookName;
        bookmarkBean.chapterIndex = chapterIndex;
        bookmarkBean.chapterName = chapterName;
        bookmarkBean.pageIndex = pageIndex;
        bookmarkBean.content = content;

        return bookmarkBean;
    }

    public String getNoteUrl() {
        return this.noteUrl;
    }

    public void setNoteUrl(String noteUrl) {
        this.noteUrl = noteUrl;
    }

    public String getChapterName() {
        return this.chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public Integer getChapterIndex() {
        return this.chapterIndex;
    }

    public void setChapterIndex(Integer chapterIndex) {
        this.chapterIndex = chapterIndex;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBookName() {
        return this.bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPageIndex() {
        return this.pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }
}

package com.micoredu.reader.bean;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "searchHistory")
public class SearchHistoryBean {
    @PrimaryKey(autoGenerate = true)
    private Long id = null;
    private int type;
    private String content;
    private long date;

    public long getDate() {
        return this.date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Ignore
    public SearchHistoryBean(int type, String content, long date) {
        this.type = type;
        this.content = content;
        this.date = date;
    }

    @Ignore
    public SearchHistoryBean(Long id, int type, String content, long date) {
        this.id = id;
        this.type = type;
        this.content = content;
        this.date = date;
    }

    public SearchHistoryBean() {

    }

}

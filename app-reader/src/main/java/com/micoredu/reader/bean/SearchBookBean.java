package com.micoredu.reader.bean;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.liuzhenli.common.constant.AppConstant;
import com.micoredu.reader.helper.BookshelfHelper;
import com.micoredu.reader.model.BookSourceManager;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

@Entity(tableName = "searchBooks", primaryKeys = "noteUrl")
public class SearchBookBean implements BaseBookBean {
    @NonNull
    private String noteUrl = "";
    /***封面URL*/
    private String coverUrl;
    private String name;
    private String author;
    private String tag;
    /***分类*/
    private String kind;
    /***来源*/
    private String origin;
    private String lastChapter;
    /***简介*/
    private String introduce;
    /***目录URL*/
    private String chapterUrl;
    private long addTime = 0L;
    private Long upTime = 0L;
    private String variable;

    @Ignore
    private Boolean isCurrentSource = false;
    @Ignore
    private int originNum = 1;
    @Ignore
    private int lastChapterNum = -2;
    @Ignore
    private int searchTime = Integer.MAX_VALUE;
    @Ignore
    private LinkedHashSet<String> originUrls;
    @Ignore
    private Map<String, String> variableMap;
    @Ignore
    private String bookInfoHtml;

    public SearchBookBean() {

    }

    @Ignore
    public SearchBookBean(String tag, String origin) {
        this.tag = tag;
        this.origin = origin;
    }


    @Override
    public String getVariable() {
        return this.variable;
    }

    @Override
    public void setVariable(String variable) {
        this.variable = variable;
    }

    @Override
    public void putVariable(String key, String value) {
        if (variableMap == null) {
            variableMap = new HashMap<>();
        }
        variableMap.put(key, value);
        variable = new Gson().toJson(variableMap);
    }

    @Override
    public Map<String, String> getVariableMap() {
        if (variableMap == null) {
            return new Gson().fromJson(variable, AppConstant.MAP_STRING);
        }
        return variableMap;
    }

    @NonNull
    @Override
    public String getNoteUrl() {
        return noteUrl;
    }

    @Override
    public void setNoteUrl(@NonNull String noteUrl) {
        this.noteUrl = noteUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name.trim().replaceAll("　", "") : null;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = BookshelfHelper.formatAuthor(author);
    }

    public String getLastChapter() {
        return lastChapter == null ? "" : lastChapter;
    }

    public void setLastChapter(String lastChapter) {
        this.lastChapter = lastChapter;

    }

    public int getLastChapterNum() {
        if (lastChapterNum == -2) {
            this.lastChapterNum = BookshelfHelper.guessChapterNum(lastChapter);
        }
        return lastChapterNum;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    @Override
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Boolean getIsCurrentSource() {
        return this.isCurrentSource;
    }

    public void setIsCurrentSource(Boolean isCurrentSource) {
        this.isCurrentSource = isCurrentSource;
        if (isCurrentSource) {
            this.addTime = System.currentTimeMillis();
        }
    }

    public int getOriginNum() {
        return originNum;
    }

    public void addOriginUrl(String origin) {
        if (this.originUrls == null) {
            this.originUrls = new LinkedHashSet<>();
        }
        this.originUrls.add(origin);
        originNum = this.originUrls.size();
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getChapterUrl() {
        return this.chapterUrl;
    }

    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }

    public long getAddTime() {
        return this.addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public int getWeight() {
        BookSourceBean source = BookSourceManager.getBookSourceByUrl(this.tag);
        if (source != null) {
            return source.getWeight();
        } else {
            return 0;
        }
    }

    public int getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(int searchTime) {
        this.searchTime = searchTime;
    }

    public Long getUpTime() {
        return this.upTime;
    }

    public void setUpTime(Long upTime) {
        this.upTime = upTime;
    }

    public String getBookInfoHtml() {
        return bookInfoHtml;
    }

    public void setBookInfoHtml(String bookInfoHtml) {
        this.bookInfoHtml = bookInfoHtml;
    }

    // 一次性存入搜索书籍节点信息
    public void setSearchInfo(String name, String author, String kind, String lastChapter,
                              String introduce, String coverUrl, String noteUrl) {
        this.name = name;
        this.author = author;
        this.kind = kind;
        this.lastChapter = lastChapter;
        this.introduce = introduce;
        this.coverUrl = coverUrl;
        this.noteUrl = noteUrl;
    }
}

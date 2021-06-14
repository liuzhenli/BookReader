package com.liuzhenli.write.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Description:
 *
 * @author liuzhenli 2021/1/21
 * Email: 848808263@qq.com
 */
@Entity
public class WriteBook implements Serializable {

    public static final long serialVersionUID = 1L;

    /***local bookId*/
    @Id(autoincrement = true)
    private Long id;

    private long bookId;

    private String bookName;
    /***作品简介**/
    private String intro;
    /***推荐语**/
    private String recommendation;
    /***封面*/
    private String coverUrl;

    private String tagWords;

    /***书分类**/
    private String category;

    private int lastChapterId;

    private int wordCount;
    private int imageCount;
    private int chapterCount;
    /***0未完成 1完成*/
    private int finished;
    /***是否是代表作品*/
    private int magnumOpusFlag;
    /***修改时间*/
    private int modifyTime;

    private int createTime;

    /*** 0 日期 1 普通*/
    private int orderType;

    /*** 大纲路径 0 false 1 true*/
    private int outline;

    /*** 0:章节为列表视图   1:章节为卡片视图*/
    private int viewType;

    @Generated(hash = 470471163)
    public WriteBook(Long id, long bookId, String bookName, String intro,
            String recommendation, String coverUrl, String tagWords,
            String category, int lastChapterId, int wordCount, int imageCount,
            int chapterCount, int finished, int magnumOpusFlag, int modifyTime,
            int createTime, int orderType, int outline, int viewType) {
        this.id = id;
        this.bookId = bookId;
        this.bookName = bookName;
        this.intro = intro;
        this.recommendation = recommendation;
        this.coverUrl = coverUrl;
        this.tagWords = tagWords;
        this.category = category;
        this.lastChapterId = lastChapterId;
        this.wordCount = wordCount;
        this.imageCount = imageCount;
        this.chapterCount = chapterCount;
        this.finished = finished;
        this.magnumOpusFlag = magnumOpusFlag;
        this.modifyTime = modifyTime;
        this.createTime = createTime;
        this.orderType = orderType;
        this.outline = outline;
        this.viewType = viewType;
    }

    @Generated(hash = 1320800007)
    public WriteBook() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getBookId() {
        return this.bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return this.bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getIntro() {
        return this.intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getRecommendation() {
        return this.recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getCoverUrl() {
        return this.coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getTagWords() {
        return this.tagWords;
    }

    public void setTagWords(String tagWords) {
        this.tagWords = tagWords;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getLastChapterId() {
        return this.lastChapterId;
    }

    public void setLastChapterId(int lastChapterId) {
        this.lastChapterId = lastChapterId;
    }

    public int getWordCount() {
        return this.wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public int getImageCount() {
        return this.imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public int getChapterCount() {
        return this.chapterCount;
    }

    public void setChapterCount(int chapterCount) {
        this.chapterCount = chapterCount;
    }

    public int getFinished() {
        return this.finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public int getMagnumOpusFlag() {
        return this.magnumOpusFlag;
    }

    public void setMagnumOpusFlag(int magnumOpusFlag) {
        this.magnumOpusFlag = magnumOpusFlag;
    }

    public int getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(int modifyTime) {
        this.modifyTime = modifyTime;
    }

    public int getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getOrderType() {
        return this.orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getOutline() {
        return this.outline;
    }

    public void setOutline(int outline) {
        this.outline = outline;
    }

    public int getViewType() {
        return this.viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

}

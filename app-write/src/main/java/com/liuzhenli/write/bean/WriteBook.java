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

    /***书id*/
    @Id(autoincrement = true)
    public Long bookId;

    public String bookName;
    /***作品简介**/
    public String intro;
    /***推荐语**/
    public String recommendation;
    /***封面*/
    public String coverUrl;

    public String tagWords;

    /***书分类**/
    public String category;

    public Integer lastChapterId;

    public Integer wordCount;
    public Integer imageCount;
    public Integer chapterCount;
    /***0未完成 1完成*/
    public Integer finished;
    /***是否是代表作品*/
    public Integer magnumOpusFlag;
    /***修改时间*/
    public Long modifyTime;

    public Long createTime;

    /*** 0 日期 1 普通*/
    public Integer orderType;

    /*** 大纲路径 0 false 1 true*/
    public Integer outline;

    /*** 0:章节为列表视图   1:章节为卡片视图*/
    public Integer viewType;

    @Generated(hash = 642005423)
    public WriteBook(Long bookId, String bookName, String intro,
            String recommendation, String coverUrl, String tagWords,
            String category, Integer lastChapterId, Integer wordCount,
            Integer imageCount, Integer chapterCount, Integer finished,
            Integer magnumOpusFlag, Long modifyTime, Long createTime,
            Integer orderType, Integer outline, Integer viewType) {
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

    public Long getBookId() {
        return this.bookId;
    }

    public void setBookId(Long bookId) {
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

    public Integer getLastChapterId() {
        return this.lastChapterId;
    }

    public void setLastChapterId(Integer lastChapterId) {
        this.lastChapterId = lastChapterId;
    }

    public Integer getWordCount() {
        return this.wordCount;
    }

    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }

    public Integer getImageCount() {
        return this.imageCount;
    }

    public void setImageCount(Integer imageCount) {
        this.imageCount = imageCount;
    }

    public Integer getChapterCount() {
        return this.chapterCount;
    }

    public void setChapterCount(Integer chapterCount) {
        this.chapterCount = chapterCount;
    }

    public Integer getFinished() {
        return this.finished;
    }

    public void setFinished(Integer finished) {
        this.finished = finished;
    }

    public Integer getMagnumOpusFlag() {
        return this.magnumOpusFlag;
    }

    public void setMagnumOpusFlag(Integer magnumOpusFlag) {
        this.magnumOpusFlag = magnumOpusFlag;
    }

    public Long getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getOrderType() {
        return this.orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getOutline() {
        return this.outline;
    }

    public void setOutline(Integer outline) {
        this.outline = outline;
    }

    public Integer getViewType() {
        return this.viewType;
    }

    public void setViewType(Integer viewType) {
        this.viewType = viewType;
    }

}

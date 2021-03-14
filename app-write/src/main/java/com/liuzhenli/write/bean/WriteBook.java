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
    public long bookId;

    public String bookName;
    /***作品简介**/
    public String summary;
    /***推荐语**/
    public String recommendation;
    /***封面*/
    public String coverUrl;

    public String tagWords;

    /***书分类**/
    public String category;

    public int lastChapterId;

    public int wordCount;
    public int imageCount;
    public int chapterCount;
    /***0未完成 1完成*/
    public int finished;
    /***是否是代表作品*/
    public int magnumOpusFlag;
    /***修改时间*/
    public long modifyTime;

    public long createTime;

    /*** 0 日期 1 普通*/
    public int orderType;

    /*** 大纲路径 0 false 1 true*/
    public int outline;

    /*** 0:章节为列表视图   1:章节为卡片视图*/
    public int viewType;

    @Generated(hash = 474520462)
    public WriteBook(long bookId, String bookName, String summary,
            String recommendation, String coverUrl, String tagWords,
            String category, int lastChapterId, int wordCount, int imageCount,
            int chapterCount, int finished, int magnumOpusFlag, long modifyTime,
            long createTime, int orderType, int outline, int viewType) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.summary = summary;
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

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public long getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
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

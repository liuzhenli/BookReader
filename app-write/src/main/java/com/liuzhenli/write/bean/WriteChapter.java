package com.liuzhenli.write.bean;

import com.umeng.commonsdk.debug.I;

import org.greenrobot.greendao.annotation.Entity;

import java.io.Serializable;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Description:bookChapter info
 *
 * @author liuzhenli 2021/1/21
 * Email: 848808263@qq.com
 */
@Entity
public class WriteChapter implements Serializable {
    public static final long serialVersionUID = 1L;

    public Long id;

    public Long bookId;

    public Long chapterId;

    public String title;

    public String contentUrl;

    public String htmlUrl;

    public Integer wordCount;

    public Integer imageCount;

    public Double orderValue;

    public Integer published;

    public Long syncChapterTime;

    public Long timestamp;

    public Long modifyTime;

    public Long releaseTime;

    public Long createTime;

    public Long localBookId;

    public Integer localUploadFlag;

    public Integer localDownloadFlag; // 0需要从服务端下载，1不需要从服务端下载

    public Integer localEditingFlag;// 0退出编辑状态，1进入编辑状态，加载章节后进入编辑状态，正常退出后要恢复状态。

    public Integer localPublishStatus;

    public Long localUpdateTime;

    public Integer localDelete;

    public Long publishTimeValue;

    public String attachments;

    public Integer draftWordCount;

    public Integer draftImageCount;

    public String lastTag;

    public Integer conflict_status;

    public String clientId;

    public String contentTag;

    public Boolean drafted;

    @Generated(hash = 1438640317)
    public WriteChapter(Long id, Long bookId, Long chapterId, String title, String contentUrl,
            String htmlUrl, Integer wordCount, Integer imageCount, Double orderValue, Integer published,
            Long syncChapterTime, Long timestamp, Long modifyTime, Long releaseTime, Long createTime,
            Long localBookId, Integer localUploadFlag, Integer localDownloadFlag,
            Integer localEditingFlag, Integer localPublishStatus, Long localUpdateTime,
            Integer localDelete, Long publishTimeValue, String attachments, Integer draftWordCount,
            Integer draftImageCount, String lastTag, Integer conflict_status, String clientId,
            String contentTag, Boolean drafted) {
        this.id = id;
        this.bookId = bookId;
        this.chapterId = chapterId;
        this.title = title;
        this.contentUrl = contentUrl;
        this.htmlUrl = htmlUrl;
        this.wordCount = wordCount;
        this.imageCount = imageCount;
        this.orderValue = orderValue;
        this.published = published;
        this.syncChapterTime = syncChapterTime;
        this.timestamp = timestamp;
        this.modifyTime = modifyTime;
        this.releaseTime = releaseTime;
        this.createTime = createTime;
        this.localBookId = localBookId;
        this.localUploadFlag = localUploadFlag;
        this.localDownloadFlag = localDownloadFlag;
        this.localEditingFlag = localEditingFlag;
        this.localPublishStatus = localPublishStatus;
        this.localUpdateTime = localUpdateTime;
        this.localDelete = localDelete;
        this.publishTimeValue = publishTimeValue;
        this.attachments = attachments;
        this.draftWordCount = draftWordCount;
        this.draftImageCount = draftImageCount;
        this.lastTag = lastTag;
        this.conflict_status = conflict_status;
        this.clientId = clientId;
        this.contentTag = contentTag;
        this.drafted = drafted;
    }

    @Generated(hash = 1392370881)
    public WriteChapter() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBookId() {
        return this.bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public long getChapterId() {
        return this.chapterId;
    }

    public void setChapterId(long chapterId) {
        this.chapterId = chapterId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentUrl() {
        return this.contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getHtmlUrl() {
        return this.htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
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

    public double getOrderValue() {
        return this.orderValue;
    }

    public void setOrderValue(double orderValue) {
        this.orderValue = orderValue;
    }

    public int getPublished() {
        return this.published;
    }

    public void setPublished(int published) {
        this.published = published;
    }

    public long getSyncChapterTime() {
        return this.syncChapterTime;
    }

    public void setSyncChapterTime(long syncChapterTime) {
        this.syncChapterTime = syncChapterTime;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public long getReleaseTime() {
        return this.releaseTime;
    }

    public void setReleaseTime(long releaseTime) {
        this.releaseTime = releaseTime;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLocalBookId() {
        return this.localBookId;
    }

    public void setLocalBookId(long localBookId) {
        this.localBookId = localBookId;
    }

    public int getLocalUploadFlag() {
        return this.localUploadFlag;
    }

    public void setLocalUploadFlag(int localUploadFlag) {
        this.localUploadFlag = localUploadFlag;
    }

    public int getLocalDownloadFlag() {
        return this.localDownloadFlag;
    }

    public void setLocalDownloadFlag(int localDownloadFlag) {
        this.localDownloadFlag = localDownloadFlag;
    }

    public int getLocalEditingFlag() {
        return this.localEditingFlag;
    }

    public void setLocalEditingFlag(int localEditingFlag) {
        this.localEditingFlag = localEditingFlag;
    }

    public int getLocalPublishStatus() {
        return this.localPublishStatus;
    }

    public void setLocalPublishStatus(int localPublishStatus) {
        this.localPublishStatus = localPublishStatus;
    }

    public long getLocalUpdateTime() {
        return this.localUpdateTime;
    }

    public void setLocalUpdateTime(long localUpdateTime) {
        this.localUpdateTime = localUpdateTime;
    }

    public int getLocalDelete() {
        return this.localDelete;
    }

    public void setLocalDelete(int localDelete) {
        this.localDelete = localDelete;
    }

    public long getPublishTimeValue() {
        return this.publishTimeValue;
    }

    public void setPublishTimeValue(long publishTimeValue) {
        this.publishTimeValue = publishTimeValue;
    }

    public String getAttachments() {
        return this.attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public int getDraftWordCount() {
        return this.draftWordCount;
    }

    public void setDraftWordCount(int draftWordCount) {
        this.draftWordCount = draftWordCount;
    }

    public int getDraftImageCount() {
        return this.draftImageCount;
    }

    public void setDraftImageCount(int draftImageCount) {
        this.draftImageCount = draftImageCount;
    }

    public String getLastTag() {
        return this.lastTag;
    }

    public void setLastTag(String lastTag) {
        this.lastTag = lastTag;
    }

    public int getConflict_status() {
        return this.conflict_status;
    }

    public void setConflict_status(int conflict_status) {
        this.conflict_status = conflict_status;
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getContentTag() {
        return this.contentTag;
    }

    public void setContentTag(String contentTag) {
        this.contentTag = contentTag;
    }

    public boolean getDrafted() {
        return this.drafted;
    }

    public void setDrafted(boolean drafted) {
        this.drafted = drafted;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }

    public void setImageCount(Integer imageCount) {
        this.imageCount = imageCount;
    }

    public void setOrderValue(Double orderValue) {
        this.orderValue = orderValue;
    }

    public void setPublished(Integer published) {
        this.published = published;
    }

    public void setSyncChapterTime(Long syncChapterTime) {
        this.syncChapterTime = syncChapterTime;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public void setReleaseTime(Long releaseTime) {
        this.releaseTime = releaseTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setLocalBookId(Long localBookId) {
        this.localBookId = localBookId;
    }

    public void setLocalUploadFlag(Integer localUploadFlag) {
        this.localUploadFlag = localUploadFlag;
    }

    public void setLocalDownloadFlag(Integer localDownloadFlag) {
        this.localDownloadFlag = localDownloadFlag;
    }

    public void setLocalEditingFlag(Integer localEditingFlag) {
        this.localEditingFlag = localEditingFlag;
    }

    public void setLocalPublishStatus(Integer localPublishStatus) {
        this.localPublishStatus = localPublishStatus;
    }

    public void setLocalUpdateTime(Long localUpdateTime) {
        this.localUpdateTime = localUpdateTime;
    }

    public void setLocalDelete(Integer localDelete) {
        this.localDelete = localDelete;
    }

    public void setPublishTimeValue(Long publishTimeValue) {
        this.publishTimeValue = publishTimeValue;
    }

    public void setDraftWordCount(Integer draftWordCount) {
        this.draftWordCount = draftWordCount;
    }

    public void setDraftImageCount(Integer draftImageCount) {
        this.draftImageCount = draftImageCount;
    }

    public void setConflict_status(Integer conflict_status) {
        this.conflict_status = conflict_status;
    }

    public void setDrafted(Boolean drafted) {
        this.drafted = drafted;
    }
}

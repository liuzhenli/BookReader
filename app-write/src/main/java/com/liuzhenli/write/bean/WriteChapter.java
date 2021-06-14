package com.liuzhenli.write.bean;

import org.greenrobot.greendao.annotation.Entity;

import java.io.Serializable;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Description:bookChapter info
 *
 * @author liuzhenli 2021/1/21
 * Email: 848808263@qq.com
 */
@Entity
public class WriteChapter implements Serializable {
    public static final long serialVersionUID = 11654651654615L;

    /*** local chapter id*/
    @Id(autoincrement = true)
    private Long id;

    private long localBookId;

    private long bookId;

    private long chapterId;

    /*** chapter title*/
    private String title;

    private String contentUrl;

    private String htmlUrl;

    private int wordCount;

    private int imageCount;

    private double orderValue;

    private int published;

    private long syncChapterTime;

    private long timestamp;

    private long modifyTime;

    private long releaseTime;

    private long createTime;

    private long localUploadFlag;
    /*** 是否需要从服务端下载*/
    private int localDownloadFlag;

    /*** 加载章节后进入编辑状态，正常退出后要恢复状态。0退出编辑状态，1进入编辑状态*/
    private int localEditingFlag;

    private int localPublishStatus;

    private long localUpdateTime;

    private int localDelete;

    private long publishTimeValue;

    private String attachments;

    private int draftWordCount;

    private int draftImageCount;

    private String lastTag;

    private int conflictStatus;

    private String clientId;

    private String contentTag;

    private Boolean drafted;

    @Generated(hash = 1123570893)
    public WriteChapter(Long id, long localBookId, long bookId, long chapterId,
            String title, String contentUrl, String htmlUrl, int wordCount,
            int imageCount, double orderValue, int published, long syncChapterTime,
            long timestamp, long modifyTime, long releaseTime, long createTime,
            long localUploadFlag, int localDownloadFlag, int localEditingFlag,
            int localPublishStatus, long localUpdateTime, int localDelete,
            long publishTimeValue, String attachments, int draftWordCount,
            int draftImageCount, String lastTag, int conflictStatus,
            String clientId, String contentTag, Boolean drafted) {
        this.id = id;
        this.localBookId = localBookId;
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
        this.conflictStatus = conflictStatus;
        this.clientId = clientId;
        this.contentTag = contentTag;
        this.drafted = drafted;
    }

    @Generated(hash = 1392370881)
    public WriteChapter() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getLocalBookId() {
        return this.localBookId;
    }

    public void setLocalBookId(long localBookId) {
        this.localBookId = localBookId;
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

    public long getLocalUploadFlag() {
        return this.localUploadFlag;
    }

    public void setLocalUploadFlag(long localUploadFlag) {
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

    public int getConflictStatus() {
        return this.conflictStatus;
    }

    public void setConflictStatus(int conflictStatus) {
        this.conflictStatus = conflictStatus;
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

    public Boolean getDrafted() {
        return this.drafted;
    }

    public void setDrafted(Boolean drafted) {
        this.drafted = drafted;
    }

}

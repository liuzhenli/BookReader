package com.liuzhenli.write.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Description:
 *
 * @author liuzhenli 2021/5/30
 * Email: 848808263@qq.com
 */
@Entity
public class WriteHistory {

    @Id(autoincrement = true)
    private Long id;
    private long localBookId;
    private long localChapterId;
    private int conflictStatus;
    private int fileIndex;
    private int type;
    private long time;
    private int wordCount;
    private int imgCount;
    private String eTag;
    @Generated(hash = 21359941)
    public WriteHistory(Long id, long localBookId, long localChapterId,
            int conflictStatus, int fileIndex, int type, long time, int wordCount,
            int imgCount, String eTag) {
        this.id = id;
        this.localBookId = localBookId;
        this.localChapterId = localChapterId;
        this.conflictStatus = conflictStatus;
        this.fileIndex = fileIndex;
        this.type = type;
        this.time = time;
        this.wordCount = wordCount;
        this.imgCount = imgCount;
        this.eTag = eTag;
    }
    @Generated(hash = 1614788565)
    public WriteHistory() {
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
    public long getLocalChapterId() {
        return this.localChapterId;
    }
    public void setLocalChapterId(long localChapterId) {
        this.localChapterId = localChapterId;
    }
    public int getConflictStatus() {
        return this.conflictStatus;
    }
    public void setConflictStatus(int conflictStatus) {
        this.conflictStatus = conflictStatus;
    }
    public int getFileIndex() {
        return this.fileIndex;
    }
    public void setFileIndex(int fileIndex) {
        this.fileIndex = fileIndex;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public int getWordCount() {
        return this.wordCount;
    }
    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }
    public int getImgCount() {
        return this.imgCount;
    }
    public void setImgCount(int imgCount) {
        this.imgCount = imgCount;
    }
    public String getETag() {
        return this.eTag;
    }
    public void setETag(String eTag) {
        this.eTag = eTag;
    }
}

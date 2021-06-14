package com.liuzhenli.write.bean;

import android.text.Spanned;

import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2021/6/14
 * Email: 848808263@qq.com
 */
public class DraftContent {
    public long bookId;
    public long chapterId;
    public String title;
    public String authorPostscript;
    public String url;
    public String updateTime;
    public String tag;
    public long timestamp;
    public Spanned mSpannableText;
    public List<ImageElement> imageAttachmentList;
}

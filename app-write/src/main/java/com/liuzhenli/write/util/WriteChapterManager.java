package com.liuzhenli.write.util;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;

import com.liuzhenli.common.encript.AES;
import com.liuzhenli.common.exception.ApiCodeException;
import com.liuzhenli.common.utils.Constant;
import com.liuzhenli.common.utils.FileUtils;
import com.liuzhenli.common.utils.L;
import com.liuzhenli.write.bean.DraftContent;
import com.liuzhenli.write.bean.ImageElement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2021/6/14
 * Email: 848808263@qq.com
 */
public class WriteChapterManager {

    public WriteChapterManager() {
    }

    public synchronized void saveDraft(final String content, final String chapterPath, final List<ImageElement> attachList, final String attachImagePath, Boolean encryption) {

        String fullPath = chapterPath + "/content.xml";
        try {
            if (encryption) {
                if (content != null) {
                    byte[] encrypt = AES.encrypt(content, Constant.AES_KEY);
                    if (encrypt != null) {
                        String encryptResultStr = AES.parseByte2HexStr(encrypt);
                        L.e("加密秘文" + encryptResultStr);
                        FileUtils.writeFromBuffer(fullPath, encryptResultStr.getBytes("utf-8"));
                    }
                }

            } else {
                FileUtils.writeFromBuffer(fullPath, content.getBytes("utf-8"));
            }
            if (!chapterPath.equals(attachImagePath)) {
                for (int i = 0; attachList != null && i < attachList.size(); i++) {
                    if (attachList.get(i).mAttachmentFile != null) {
                        String attachmentName = attachList.get(i).mAttachmentFile.replace(Constant.ATTACHMENT_SUFFIX_SERVER, Constant.ATTACHMENT_SUFFIX);
                        File largeFile = new File(attachImagePath + "/" + attachmentName);
                        File smallFile = new File(attachImagePath + "/" + "h_" + attachmentName);
                        FileUtils.copyFile(largeFile, new File(chapterPath + "/" + attachmentName));
                        FileUtils.copyFile(smallFile, new File(chapterPath + "/" + "h_" + attachmentName));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiCodeException(2222, "保存章节文件出错：" + e.getLocalizedMessage());
        }

    }

    public synchronized DraftContent loadDraft(final String chapterPath, Boolean encryption) {
        String contentFile = chapterPath + "/content.xml";
        String content = "";
        if (encryption) {
            String tmpContent = FileUtils.readFileToBuffer(contentFile, "utf-8");
            try {
                byte[] decryptFrom = AES.parseHexStr2Byte(tmpContent);
                content = new String(AES.decrypt(decryptFrom, Constant.AES_KEY), "utf-8");
                //L.e("解密 :",content);
            } catch (Exception e) {
                content = "内容解析出错了";
                L.e("解密出错 :" + e.getMessage());
            }
        } else {
            content = FileUtils.readFileToBuffer(contentFile, "utf-8");
        }
        L.e(content);
        Spanned mSpannableText = new SpannableString("");
        List<ImageElement> attachmentElementList = new ArrayList<>();
        String title = "";
        String body = "";
        String authorPostscript = "";    //作者有话说
        if (content != null && content.length() > 0) {
            Document doc = Jsoup.parse(content);
            Element heads = doc.head();
            for (int i = 0; i < heads.childNodeSize(); i++) {
                Element meta = heads.child(i);
                String name = meta.attr("name");
                if ("chapter:authorspeak".equals(name)) {
                    authorPostscript = meta.attr("content");
                    break;
                }
            }
            Elements titleElemnts = doc.getElementsByTag("title");
            title = titleElemnts.text();
            Elements bodyElemnts = doc.getElementsByTag("body");
            body = bodyElemnts.html();
            body = body.replaceAll("\n<br />", "<br />");
            mSpannableText = Html.fromHtml(body, new Html.ImageGetter() {
                @Override
                public Drawable getDrawable(String source) {
                    return null;
                }
            }, null);
        }
        DraftContent chapterContent = new DraftContent();
        chapterContent.authorPostscript = authorPostscript;
        chapterContent.title = title;
        chapterContent.mSpannableText = mSpannableText;
        chapterContent.imageAttachmentList = attachmentElementList;
        return chapterContent;
    }

}

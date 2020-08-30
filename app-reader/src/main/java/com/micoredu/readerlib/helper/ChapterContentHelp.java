package com.micoredu.readerlib.helper;

import android.text.TextUtils;

import com.luhuiguo.chinese.ChineseUtils;
import com.micoredu.readerlib.bean.ReplaceRuleBean;
import com.micoredu.readerlib.model.ReplaceRuleManager;
import com.micoredu.readerlib.utils.ReaderConfig;

public class ChapterContentHelp {
    private static ChapterContentHelp instance;

    public static synchronized ChapterContentHelp getInstance() {
        if (instance == null)
            instance = new ChapterContentHelp();
        return instance;
    }

    /**
     * 转繁体
     */
    private String toTraditional(String content) {
        int convertCTS = ReadBookControl.getInstance().getTextConvert();
        switch (convertCTS) {
            case ReaderConfig.CNText.CN_SIMPLE:
                content = ChineseUtils.toSimplified(content);
                break;
            case ReaderConfig.CNText.CN_TRADITION:
                content = ChineseUtils.toTraditional(content);
                break;
            default:
                break;
        }
        return content;
    }

    /**
     * 替换净化
     */
    public String replaceContent(String bookName, String bookTag, String content, Boolean replaceEnable) {
        if (!replaceEnable) return toTraditional(content);
        if (ReplaceRuleManager.getEnabled().size() == 0) return toTraditional(content);
        //替换
        for (ReplaceRuleBean replaceRule : ReplaceRuleManager.getEnabled()) {
            if (isUseTo(replaceRule.getUseTo(), bookTag, bookName)) {
                try {
                    content = content.replaceAll(replaceRule.getFixedRegex(), replaceRule.getReplacement());
                } catch (Exception ignored) {
                }
            }
        }
        return toTraditional(content);
    }

    private boolean isUseTo(String useTo, String bookTag, String bookName) {
        return TextUtils.isEmpty(useTo)
                || useTo.contains(bookTag)
                || useTo.contains(bookName);
    }

}

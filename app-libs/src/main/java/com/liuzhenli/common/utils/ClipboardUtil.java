package com.liuzhenli.common.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

import com.liuzhenli.common.BaseApplication;

/**
 * Description:
 *
 * @author liuzhenli 2020/11/6
 * Email: 848808263@qq.com
 */
public class ClipboardUtil {

    /**
     * 复制内容到剪切板
     *
     * @param content 文本
     */
    public static boolean copyToClipboard(String content) {
        try {
            ClipboardManager clipboardManager = (ClipboardManager) BaseApplication.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData label = ClipData.newPlainText("Label", content);
            if (clipboardManager != null) {
                clipboardManager.setPrimaryClip(label);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取剪切板内容
     */
    public static String getContent() {
        ClipboardManager manager = (ClipboardManager) BaseApplication.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null) {
            if (manager.hasPrimaryClip() && manager.getPrimaryClip() != null && manager.getPrimaryClip().getItemCount() > 0) {
                CharSequence addedText = manager.getPrimaryClip().getItemAt(0).getText();
                String addedTextString = String.valueOf(addedText);
                if (!TextUtils.isEmpty(addedTextString)) {
                    return addedTextString;
                }
            }
        }
        return "";
    }

    /**
     * 清空剪切板
     */
    public static void clear() {
        ClipboardManager manager = (ClipboardManager) BaseApplication.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null && manager.getPrimaryClip() != null) {
            try {
                manager.setPrimaryClip(manager.getPrimaryClip());
                manager.setPrimaryClip(ClipData.newPlainText("", ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}

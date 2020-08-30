package com.liuzhenli.common.utils;

import android.widget.TextView;

/**
 * Description:
 *
 * @author liuzhenli 2020/8/28
 * Email: 848808263@qq.com
 */
public class FillContentUtil {

    public static void setText(TextView view, String text) {
        view.setText(text);
    }

    public static void setNumberText(TextView view, int text) {
        view.setText(String.format("%s", text));
    }
}

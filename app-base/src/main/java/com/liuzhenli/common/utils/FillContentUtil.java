package com.liuzhenli.common.utils;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

/**
 * Description:
 *
 * @author liuzhenli 2020/8/28
 * Email: 848808263@qq.com
 */
public class FillContentUtil {

    public interface Place {
        int left = 0;
        int top = 1;
        int right = 2;
        int down = 3;
    }

    public static void setText(TextView view, CharSequence text) {
        view.setCompoundDrawables(null, null, null, null);
        view.setText(text);
    }

    public static void setTextDrawable(TextView view, String text, Drawable drawable, int place) {
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        switch (place) {
            case Place.left:
                view.setCompoundDrawables(drawable, null, null, null);
                break;
            case Place.top:
                view.setCompoundDrawables(null, drawable, null, null);
                break;
            case Place.right:
                view.setCompoundDrawables(null, null, drawable, null);
                break;
            case Place.down:
                view.setCompoundDrawables(null, null, null, drawable);
                break;
            default:
                break;
        }
        view.setText(text);
    }

    public static void setNumberText(TextView view, int text) {
        view.setText(String.format("%s", text));
    }


}

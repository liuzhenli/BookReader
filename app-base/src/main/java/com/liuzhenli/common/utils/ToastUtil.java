package com.liuzhenli.common.utils;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.liuzhenli.common.BaseApplication;

/**
 * @author Liuzhenli
 * @since 2019-07-07 03:11
 */
public class ToastUtil {
    public static void showToast(String ex) {
        if (TextUtils.isEmpty(ex)) {
            return;
        }
        showToast(BaseApplication.Companion.getInstance(), ex);
    }

    public static void showToast(int resId) {
        showToast(BaseApplication.Companion.getInstance(), BaseApplication.Companion.getInstance().getResources().getString(resId));
    }

    private static Toast sToast;

    public static void showToast(Context context, CharSequence text) {
        showToast(context, text, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
    }

    public static void showLongToast(Context context, CharSequence text) {
        showToast(context, text, Toast.LENGTH_LONG);
    }

    public static void showToast(Context context, CharSequence text, int length) {
        if (context == null) {
            throw new RuntimeException("ToastUtil Context can not be null");
        }
        if (sToast != null) {
            sToast.cancel();
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            makeAndShow(context, text, length);
        } else {
            Looper.prepare();
            makeAndShow(context, text, length);
            Looper.loop();
        }
    }

    private static void makeAndShow(Context context, CharSequence text, int length) {
        sToast = Toast.makeText(context.getApplicationContext(), null, length);
        //sToast.setGravity(Gravity.CENTER, 0, 0);
        sToast.setText(text);
        sToast.show();
    }
}

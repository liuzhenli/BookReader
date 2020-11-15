package com.liuzhenli.common.utils;

import android.annotation.SuppressLint;
import android.view.View;

import com.jakewharton.rxbinding3.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-12-07 21:37
 * @since 1.0.0
 */
public class ClickUtils {
    private static final int DURATION = 500;
    public static long mLastClickTime;
    @SuppressLint("CheckResult")
    public static void click(View view, Consumer action) {
        if (Thread.currentThread().getName().contains("main")) {
            RxView.clicks(view)
                    .throttleFirst(DURATION, TimeUnit.MILLISECONDS)
                    .subscribe(action);
        }
    }

    @SuppressLint("CheckResult")
    public static void longClick(View view, Consumer action) {
        RxView.longClicks(view).subscribe(action);
    }

    @SuppressLint("CheckResult")
    public static void click(View view, Consumer action, long duration) {
        if (Thread.currentThread().getName().contains("main")) {
            RxView.clicks(view)
                    .throttleFirst(duration, TimeUnit.MILLISECONDS)
                    .subscribe(action);
        }
    }

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - mLastClickTime;
        if (0 < timeD && timeD < DURATION) {
            return true;
        }
        mLastClickTime = time;
        return false;
    }
}

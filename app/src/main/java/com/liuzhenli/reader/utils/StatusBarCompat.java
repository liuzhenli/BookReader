/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.liuzhenli.reader.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * @author yuyh.
 * @date 16/8/9.
 */
public class StatusBarCompat {
    private static final int INVALID_VAL = -1;
    public static final int SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT = 0x00000010;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static View compat(Activity activity, int statusColor) {
        int color = 0x0000000;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (statusColor != INVALID_VAL) {

                if (statusColor == 0x0000000 && (Rom.isVivo() && Build.VERSION.SDK_INT < Build.VERSION_CODES.M)) {
                    color = Color.parseColor("#80000000");
                } else {
                    color = statusColor;
                }
            }
            activity.getWindow().setStatusBarColor(color);
            return null;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);

            if (statusColor == 0x0000000 && (Rom.isVivo() || Rom.isOppo())) {
                color = Color.parseColor("#80000000");
            } else {
                color = statusColor;
            }

            View statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity));

            statusBarView.setBackgroundColor(color);
            contentView.addView(statusBarView, lp);
            return statusBarView;
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
//                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//
//            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
//            if (statusColor != INVALID_VAL) {
//                color = statusColor;
//            }
//            View statusBarView = contentView.getChildAt(0);
//            if (statusBarView != null && statusBarView.getMeasuredHeight() == getStatusBarHeight(activity)) {
//                statusBarView.setBackgroundColor(color);
//                return statusBarView;
//            }
//            statusBarView = new View(activity);
//            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                    getStatusBarHeight(activity));
//            statusBarView.setBackgroundColor(color);
//            contentView.addView(statusBarView, lp);
//            return statusBarView;
//        }
        return null;

    }

    public static void compat(Activity activity) {
        compat(activity, INVALID_VAL);
    }
//    public static void compat(Activity activity,boolean changeTextColor) {
//        compat(activity, INVALID_VAL);
//        processPrivateAPI(changeTextColor);
//
//    }


    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * 处理android4.4.4到android5.0的状态栏的文字变色
     * 是否处理stausbar的文字为深色
     * 调用私有API处理颜色
     */
    public static void processPrivateAPI(Activity activity, boolean lightStatusBar) {
        if (Rom.isFlyme()) {
            try {
                processFlyMe(activity, lightStatusBar);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Rom.isMiui()) {
            try {
                processMIUI(activity, lightStatusBar);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Rom.isOppo()) {
            setLightStatusBarIcon(activity, true);
        } else {
            if (lightStatusBar) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }

    }


    public static void setLightStatusBarIcon(Activity activity, boolean lightMode) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int vis = window.getDecorView().getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (lightMode) {
                vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (lightMode) {
                vis |= SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT;
            } else {
                vis &= ~SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT;
            }
        }
        window.getDecorView().setSystemUiVisibility(vis);
    }


    /**
     * 改变魅族的状态栏字体为黑色，要求FlyMe4以上
     */
    private static void processFlyMe(Activity activity, boolean isLightStatusBar) throws Exception {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        Class<?> instance = Class.forName("android.view.WindowManager$LayoutParams");
        int value = instance.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON").getInt(lp);
        Field field = instance.getDeclaredField("meizuFlags");
        field.setAccessible(true);
        int origin = field.getInt(lp);
        if (isLightStatusBar) {
            field.set(lp, origin | value);
        } else {
            field.set(lp, (~value) & origin);
        }
    }

    /**
     * 改变小米的状态栏字体颜色为黑色, 要求MIUI6以上
     * Tested on: MIUIV7 5.0 Redmi-Note3
     */
    private static void processMIUI(Activity activity, boolean lightStatusBar) throws Exception {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        int darkModeFlag;
        Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
        Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
        darkModeFlag = field.getInt(layoutParams);
        Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
        extraFlagField.invoke(activity.getWindow(), lightStatusBar ? darkModeFlag : 0, darkModeFlag);
    }

}
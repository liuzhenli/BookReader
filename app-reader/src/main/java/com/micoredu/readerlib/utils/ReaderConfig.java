package com.micoredu.readerlib.utils;

import com.micoredu.readerlib.R;
import com.micoredu.readerlib.ReaderLibManager;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-11-10 15:23
 */
public class ReaderConfig {
    // 默认的显示参数配置
    public static final int DEFAULT_MARGIN_HEIGHT = 20;
    public static final int DEFAULT_MARGIN_WIDTH = 15;
    public static final int DEFAULT_TIP_SIZE = 12;
    public static final float MAX_SCROLL_OFFSET = 100;
    public static final int TIP_ALPHA = 180;


    /**
     * 翻页模式
     */
    public @interface PageMode {
        /***覆盖*/
        int COVER = 0;
        /***仿真*/
        int SIMULATION = 1;
        /***滑动*/
        int SLIDE = 2;
        /***滑动*/
        int SCROLL = 3;
        /***无动画*/
        int NONE = 4;
    }

    /***默认字体大小*/
    public @interface TextSize {
        int SIZE_MIN = 10;
        int SIZE_MAX = 40;
    }

    /***中文繁简体*/
    public @interface CNText {
        /***汉语-简体*/
        int CN_SIMPLE = 1;
        /***汉语-繁体*/
        int CN_TRADITION = 2;
    }

    /***背景色*/
    public @interface PageBgColor {
        /***白天*/
        int BG_COLOR_DAY = 0;
        int BG_COLOR_YELLOW = 1;
        int BG_COLOR_GREEN = 2;
        int BG_COLOR_PINK = 3;
        int BG_COLOR_SBLUE = 4;
        int BG_COLOR_BLUE = 5;
        int BG_COLOR_NIGHT = 6;

    }
}

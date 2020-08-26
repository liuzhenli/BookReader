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


    public @interface PageMode {
        int COVER = 0;
        int SIMULATION = 1;
        int SLIDE = 2;
        int SCROLL = 3;
        int NONE = 4;
    }
}

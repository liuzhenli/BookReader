package com.liuzhenli.common.utils;

import com.liuzhenli.common.SharedPreferencesUtil;

/**
 * @author Liuzhenli
 * @since 2019-07-06 16:28
 */
public class AppConfigManager {

    /***书源排序方式*/
    public interface SortType {
        /***手动排序*/
        int SORT_TYPE_HAND = 0;
        /***智能排序*/
        int SORT_TYPE_AUTO = 1;
        /***音序排序*/
        int SORT_TYPE_PINYIN = 2;
    }

    public static void setBookSourceSortType(int sortType) {
        SharedPreferencesUtil.getInstance().putInt("SourceSort", sortType);
    }

    public static int getBookSourceSortType() {
        return SharedPreferencesUtil.getInstance().getInt("SourceSort", SortType.SORT_TYPE_AUTO);
    }

    /***手机文件,最小值*/
    public static int getMinFileSize() {
        return SharedPreferencesUtil.getInstance().getInt("mine_file_size", Constant.MIN_FILE_SIZE);
    }

    /**
     * 设置文件最小值
     *
     * @param size 最小值
     */
    public static void setMineFileSize(int size) {
        SharedPreferencesUtil.getInstance().putInt("mine_file_size", size);
    }

    /**
     * 检查书架书的更新
     */
    public static boolean isRefreshBookShelf() {
        return SharedPreferencesUtil.getInstance().getBoolean("refresh_book_shelf", true);
    }

    /**
     * 设置书架是否开机更新
     *
     * @param refresh 是否更新
     */
    public static void setRefreshBookShelf(boolean refresh) {
        SharedPreferencesUtil.getInstance().putBoolean("refresh_book_shelf", refresh);
    }


    public static void setProtectEyeReadMode(boolean on) {
        SharedPreferencesUtil.getInstance().putBoolean("protect_eye_read_mode", on);
    }

    public static boolean tetProtectEyeReadMode() {
        return SharedPreferencesUtil.getInstance().getBoolean("protect_eye_read_mode", false);
    }
}

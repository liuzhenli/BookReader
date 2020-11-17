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
}

package com.liuzhenli.common.utils;

import android.text.TextUtils;

import com.liuzhenli.common.SharedPreferencesUtil;
import com.liuzhenli.common.constant.AppConstant;

/**
 * app 配置管理类
 *
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


    /***备份及恢复****/
    public static String getWebDavAddress() {
        return SharedPreferencesUtil.getInstance().getString("webDavAddress", AppConstant.DEFAULT_WEB_DAV_URL);
    }

    public static void saveWebDavAddress(String webDavAddress) {
        putString("webDavAddress", webDavAddress);
    }

    public static String getWebDavAccountName() {
        return SharedPreferencesUtil.getInstance().getString("webDavAccountName");
    }

    public static void saveWebDavAccountName(String webDavAccount) {
        putString("webDavAccountName", webDavAccount);
    }

    public static String getWebDavAddPwd() {
        return SharedPreferencesUtil.getInstance().getString("webDavAccountPassword");
    }

    public static void saveWebDavAddPwd(String password) {
        putString("webDavAccountPassword", password);
    }

    /***备份地址*/
    public static void setBackupPath(String backUpPath) {
        putString("backupPath", backUpPath);
    }

    /*** 备份地址*/
    public static String getBackupPath(String defaultPath) {
        return getString("backupPath", defaultPath);
    }

    public static void putString(String key, String value) {
        if (TextUtils.isEmpty(value)) {
            SharedPreferencesUtil.getInstance().remove(key);
        } else {
            SharedPreferencesUtil.getInstance().putString(key, value);
        }
    }

    public static String getString(String key, String defaultValue) {
        return SharedPreferencesUtil.getInstance().getString(key, defaultValue);
    }
}

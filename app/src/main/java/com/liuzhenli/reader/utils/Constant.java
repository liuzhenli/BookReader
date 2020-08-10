package com.liuzhenli.reader.utils;

import com.liuzhenli.common.constant.AppConstant;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Liuzhenli
 * @since 2019-07-06 19:05
 */
public class Constant extends AppConstant {
    public static final String API_BASE_URL = "http://www.baidu.com";
    public static final int TOKEN_EXPRIED = 10104;
    public static final String SP_TOKEN = "token";
    public static final String COMMON_CONFIG = "common_config";
    public static final String REFER = "http://wan.baidu.com";
    public static final String FEEDBACK = "https://support.qq.com/product/186909";

    public static String PATH_DATA = FileUtils.createRootPath(AppUtils.getAppContext()) + "/data";
    public static String BASE_PATH = FileUtils.createRootPath(AppUtils.getAppContext()) + "/book/";
    public static String FONT_PATH = FileUtils.createRootPath(AppUtils.getAppContext()) + "/font/";
    public static String IMG_PATH = FileUtils.createRootPath(AppUtils.getAppContext()) + "/img/";
    public static String UPDATE_PATH = FileUtils.createRootPath(AppUtils.getAppContext()) + "/update/";
    public static String LOG_PATH = FileUtils.createRootPath(AppUtils.getAppContext()) + "/log/";
    public static String TTS_PATH = FileUtils.createRootPath(AppUtils.getAppContext()) + "/tts/";
    public static String WIFI_BOOK_PATH = FileUtils.createWifiBookPath(AppUtils.getAppContext()) + "/wifiTransfer/";
    public static String POST_CACHE_PATH = FileUtils.createRootPath(AppUtils.getAppContext()) + "/tiezi/";


    public static final String PERCENTL_STR = "%";
    public static final String CHARSET_NAME = "UTF-8";

    @Retention(RetentionPolicy.SOURCE)
    public @interface FileAttr {
        String IMAGE = "image";
        String FILE = "file";
        String CHECKED = "checked";
        String SIZE = "size";
        String NAME = "name";
        String ZERO = "directory";
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Fileuffix {
        String TET = ".txt";
        String PDF = ".pdf";
        String EPUB = ".epub";
    }
}

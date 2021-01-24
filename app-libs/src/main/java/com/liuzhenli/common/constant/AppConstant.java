package com.liuzhenli.common.constant;

import com.google.gson.reflect.TypeToken;
import com.liuzhenli.common.BaseApplication;
import com.liuzhenli.common.BuildConfig;

import org.greenrobot.greendao.annotation.Convert;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import okhttp3.MediaType;

public class AppConstant {
    public interface BookOpenFrom {
        /***app外部打开*/
        final static int OPEN_FROM_OTHER = -1;
        /***本地书***/
        public final static int OPEN_FROM_APP = 0;
        /****app打开***/
        public final static int FROM_BOOKSHELF = 1;
        /****搜索打开***/
        public final static int OPEN_FROM_SEARCH = 2;
    }

    public static final String BOOK_ID = "bookId";
    public static final String CHAPTER_ID = "chapterId";
    public static final String LOCAL_BOOK_PATH = "localBookPath";

    public static final String ActionStartService = "startService";
    public static final String ActionDoneService = "doneService";

    public static final long TIME_OUT = BuildConfig.DEBUG ? 600 : 180;

    /*** Book Date Convert Format**/
    public static final String FORMAT_TIME = "HH:mm";
    public static final String FORMAT_FILE_DATE = "yyyy-MM-dd";
    /***BookCachePath (因为getCachePath引用了Context，所以必须是静态变量，不能够是静态常量)***/
    public static String BOOK_CACHE_PATH = BaseApplication.getInstance().getDownloadPath() + File.separator + "book_cache" + File.separator;
    public static final Pattern headerPattern = Pattern.compile("@Header:\\{.+?\\}", Pattern.CASE_INSENSITIVE);
    public static Type MAP_STRING = new TypeToken<Map<String, String>>() {
    }.getType();

    /**
     * webDav 默认地址坚果云
     */
    public static final String DEFAULT_WEB_DAV_URL = "https://dav.jianguoyun.com/dav/";

    public static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36";

    public static final Pattern JS_PATTERN = Pattern.compile("(<js>[\\w\\W]*?</js>|@js:[\\w\\W]*$)", Pattern.CASE_INSENSITIVE);
    public static final Pattern EXP_PATTERN = Pattern.compile("\\{\\{([\\w\\W]*?)\\}\\}");

    public static final ScriptEngine SCRIPT_ENGINE = new ScriptEngineManager().getEngineByName("rhino");

    public static final MediaType jsonMediaType = MediaType.parse("Content-Type, application/json");
    public static final String URL_BACKUP_GUIDE = "http://help.jianguoyun.com?p=2064";

    public interface DonateUrl {
        String ali = "https://qr.alipay.com/fkx16537qfnbficmp9dohb4";
        String zfbCode = "https://gitee.com/liuzhenli/HelloBook/raw/master/imgs/img_zhifubao.jpg";
        String wxCode = "https://gitee.com/liuzhenli/HelloBook/raw/master/imgs/img_qq.png";
        String qqCode = "https://gitee.com/liuzhenli/HelloBook/raw/master/imgs/img_weixin.png";
    }

    // public static final String URL_SOURCE_RULE = "https://gitee.com/liuzhenli/HelloBook/wikis/SourceRule?sort_id=3422218";
    public static final String URL_SOURCE_RULE = "https://gitee.com/liuzhenli/HelloBook/wikis/pages?sort_id=3422218&doc_id=1192633";
}

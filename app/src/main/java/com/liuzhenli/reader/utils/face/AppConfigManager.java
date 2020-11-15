package com.liuzhenli.reader.utils.face;

import android.content.Context;

import com.google.gson.Gson;
import com.liuzhenli.common.SharedPreferencesUtil;
import com.liuzhenli.reader.bean.CommonConfigData;
import com.liuzhenli.reader.utils.Constant;

/**
 * @author Liuzhenli
 * @since 2019-07-06 16:28
 */
public class AppConfigManager {
    public static void doCommonConfigData(Context context, CommonConfigData data) {
        Gson gson = new Gson();
        SharedPreferencesUtil.getInstance().putString(Constant.COMMON_CONFIG, gson.toJson(data, CommonConfigData.class));
    }

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
}

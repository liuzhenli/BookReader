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
public class DataUtils {
    public static void doCommonConfigData(Context context, CommonConfigData data) {
        Gson gson = new Gson();
        SharedPreferencesUtil.getInstance().putString(Constant.COMMON_CONFIG, gson.toJson(data, CommonConfigData.class));
    }

}

package com.micoredu.reader.analyzerule;


import static com.liuzhenli.common.constant.AppConstant.DEFAULT_USER_AGENT;

import android.content.SharedPreferences;


import com.liuzhenli.common.BaseApplication;
import com.liuzhenli.common.SharedPreferencesUtil;
import com.micoredu.reader.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by GKF on 2018/3/2.
 * 解析Headers
 */

public class AnalyzeHeaders {
    public static Map<String, String> getDefaultHeader() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("User-Agent", getDefaultUserAgent());
        return headerMap;
    }

    public static String getDefaultUserAgent() {
        return SharedPreferencesUtil.getInstance().getString(BaseApplication.getInstance().getString(R.string.pk_user_agent), DEFAULT_USER_AGENT);
    }
}

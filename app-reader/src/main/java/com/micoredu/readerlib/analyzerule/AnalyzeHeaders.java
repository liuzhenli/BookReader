package com.micoredu.readerlib.analyzerule;

import android.content.SharedPreferences;


import com.liuzhenli.common.BaseApplication;
import com.liuzhenli.common.SharedPreferencesUtil;
import com.micoredu.readerlib.R;
import com.micoredu.readerlib.bean.BookSourceBean;
import com.micoredu.readerlib.bean.CookieBean;
import com.micoredu.readerlib.helper.DbHelper;

import java.util.HashMap;
import java.util.Map;

import static android.text.TextUtils.isEmpty;
import static com.liuzhenli.common.constant.AppConstant.DEFAULT_USER_AGENT;

/**
 * 解析Headers
 */

public class AnalyzeHeaders {

    public static Map<String, String> getMap(BookSourceBean bookSourceBean) {
        Map<String, String> headerMap = new HashMap<>();
        if (bookSourceBean != null && !isEmpty(bookSourceBean.getHttpUserAgent())) {
            headerMap.put("User-Agent", bookSourceBean.getHttpUserAgent());
        } else {
            headerMap.put("User-Agent", getDefaultUserAgent());
        }
        if (bookSourceBean != null) {
            CookieBean cookie = DbHelper.getDaoSession().getCookieBeanDao().load(bookSourceBean.getBookSourceUrl());
            if (cookie != null) {
                headerMap.put("Cookie", cookie.getCookie());
            }
        }
        return headerMap;
    }

    private static String getDefaultUserAgent() {
        return SharedPreferencesUtil.getInstance().getString(BaseApplication.getInstance().getString(R.string.pk_user_agent), DEFAULT_USER_AGENT);
    }
}

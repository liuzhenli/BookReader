package com.micoredu.reader.analyzerule;


import com.liuzhenli.common.BaseApplication;
import com.liuzhenli.common.SharedPreferencesUtil;
import com.micoredu.reader.R;
import com.micoredu.reader.bean.BookSourceBean;
import com.micoredu.reader.bean.CookieBean;
import com.micoredu.reader.helper.AppReaderDbHelper;

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
            CookieBean cookie = AppReaderDbHelper.getInstance().getDatabase().getCookieDao().load(bookSourceBean.getBookSourceUrl());
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

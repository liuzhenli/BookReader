package com.micoredu.reader.analyzerule;

import com.liuzhenli.common.encript.MD5Utils;
import com.liuzhenli.common.utils.StringUtils;
import com.micoredu.reader.bean.CookieBean;
import com.micoredu.reader.helper.AppReaderDbHelper;
import com.micoredu.reader.helper.SSLSocketClient;
import com.micoredu.reader.observe.BaseModelImpl;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Response;

@SuppressWarnings({"unused", "WeakerAccess"})
public interface JsExtensions {

    /**
     * js实现跨域访问,不能删
     */
    default String ajax(String urlStr) {
        try {
            AnalyzeUrl analyzeUrl = new AnalyzeUrl(urlStr, AnalyzeHeaders.getDefaultHeader());
            Response<String> response = BaseModelImpl.getInstance().getResponseO(analyzeUrl)
                    .blockingFirst();
            return response.body();
        } catch (Exception e) {
            return e.getLocalizedMessage();
        }
    }

    /**
     * js实现跨域访问,不能删
     */
    default Response<String> getResponse(String urlStr) {
        try {
            AnalyzeUrl analyzeUrl = new AnalyzeUrl(urlStr, AnalyzeHeaders.getDefaultHeader());
            return BaseModelImpl.getInstance().getResponseO(analyzeUrl)
                    .blockingFirst();
        } catch (Exception e) {
            return Response.success(e.getLocalizedMessage());
        }
    }

    /**
     * js实现解码,不能删
     */
    default String base64Decoder(String base64) {
        return StringUtils.base64Decode(base64);
    }

    /**
     * 章节数转数字
     */
    default String toNumChapter(String s) {
        if (s == null) {
            return null;
        }
        Pattern pattern = Pattern.compile("(第)(.+?)(章)");
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            return matcher.group(1) + StringUtils.stringToInt(matcher.group(2)) + matcher.group(3);
        }
        return s;
    }

    /**
     * js实现重定向拦截,不能删
     */
    default Connection.Response get(String urlStr, Map<String, String> headers) throws IOException {
        return Jsoup.connect(urlStr)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .ignoreContentType(true)
                .followRedirects(false)
                .headers(headers)
                .method(Connection.Method.GET)
                .execute();
    }

    /**
     * js实现重定向拦截,不能删
     */
    default Connection.Response post(String urlStr, String body, Map<String, String> headers) throws IOException {
        return Jsoup.connect(urlStr)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .ignoreContentType(true)
                .followRedirects(false)
                .requestBody(body)
                .headers(headers)
                .method(Connection.Method.POST)
                .execute();
    }

    default void putCache(String key, String value) {
        CookieBean cookie = new CookieBean(key, value);
        AppReaderDbHelper.getInstance().getDatabase().getCookieDao().insertOrReplace(cookie);
    }

    default String getCache(String key) {
        CookieBean cookie = AppReaderDbHelper.getInstance().getDatabase().getCookieDao().load(key);
        if (cookie == null) {
            return null;
        }
        return cookie.getCookie();
    }

    default String md5Encode(String text) {
        return MD5Utils.strToMd5By32(text);
    }
}
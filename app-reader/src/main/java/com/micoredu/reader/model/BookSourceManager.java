package com.micoredu.reader.model;

import android.database.Cursor;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liuzhenli.common.SharedPreferencesUtil;
import com.liuzhenli.common.gson.GsonUtils;
import com.liuzhenli.common.utils.AppSharedPreferenceHelper;
import com.liuzhenli.common.utils.NetworkUtils;
import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.common.utils.StringUtils;
import com.micoredu.reader.analyzerule.AnalyzeHeaders;
import com.micoredu.reader.bean.BookSource3Bean;
import com.micoredu.reader.bean.BookSource3FindBean;
import com.micoredu.reader.bean.BookSourceBean;
import com.micoredu.reader.dao.BookSourceDao;
import com.micoredu.reader.helper.AppReaderDbHelper;
import com.micoredu.reader.impl.IHttpGetApi;
import com.micoredu.reader.observe.BaseModelImpl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;

/**
 * 所有书源
 */

public class BookSourceManager {

    /**
     * @return 用户筛选的书源
     */
    public static List<BookSourceBean> getSelectedBookSource() {

        int sourceSort = SharedPreferencesUtil.getInstance().getInt("SourceSort", AppSharedPreferenceHelper.SortType.SORT_TYPE_HAND);
        if (sourceSort == AppSharedPreferenceHelper.SortType.SORT_TYPE_HAND) {
            return AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().getSelectedBookSource();
        } else if (sourceSort == AppSharedPreferenceHelper.SortType.SORT_TYPE_AUTO) {
            //auto order 智能排序
            return AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().getSelectedBookSourceByWeight();
        } else {
            //音序排序
            return AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().getSelectedBookSourceByBookSourceName();
        }
    }

    /***全部书源*/
    public static List<BookSourceBean> getAllBookSource() {
        int sourceSort = SharedPreferencesUtil.getInstance().getInt("SourceSort", AppSharedPreferenceHelper.SortType.SORT_TYPE_HAND);
        if (sourceSort == AppSharedPreferenceHelper.SortType.SORT_TYPE_HAND) {
            return AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().getAllBookSource();
        } else if (sourceSort == AppSharedPreferenceHelper.SortType.SORT_TYPE_AUTO) {
            //auto order 智能排序
            return AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().getAllOrderByWeight();
        } else {
            //音序排序
            return AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().getAllOrderByBookSourceName();
        }
    }

    public static List<BookSourceBean> getRuleFindEnable() {
        return AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().getRuleFindEnable();
    }


    public static List<BookSourceBean> getAllBookSourceBySerialNumber() {
        return AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().getAllBookSource();
    }

    /**
     * 搜索本地书源
     *
     * @param keyword 关键字
     * @return 相关书源
     */
    public static List<BookSourceBean> getSourceByKey(String keyword) {
        int sourceSort = SharedPreferencesUtil.getInstance().getInt("SourceSort", AppSharedPreferenceHelper.SortType.SORT_TYPE_HAND);
        if (sourceSort == AppSharedPreferenceHelper.SortType.SORT_TYPE_HAND) {
            return AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().getSourceByKey(keyword);
        } else if (sourceSort == AppSharedPreferenceHelper.SortType.SORT_TYPE_PINYIN) {
            return AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().getSourceByKeyOrderByBookSourceName(keyword);
        } else {
            return AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().getSourceByKeyOrderByWeight(keyword);
        }
    }

    @Nullable
    public static BookSourceBean getBookSourceByUrl(String url) {
        if (url == null) {
            return null;
        }
        return AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().getByBookSourceUrl(url);
    }

    public static void removeBookSource(BookSourceBean sourceBean) {
        if (sourceBean == null) {
            return;
        }
        AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().delete(sourceBean);
    }

    public static void deleteBookSource(BookSourceBean bookSourceBean) {
        if (bookSourceBean != null) {
            AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().delete(bookSourceBean);
        }
    }

    public static String getBookSourceSort() {
        switch (SharedPreferencesUtil.getInstance().getInt("SourceSort", 0)) {
            case 1:
                return "Weight DESC";
            case 2:
                return "BookSourceName COLLATE LOCALIZED ASC";
            default:
                return "SerialNumber ASC";
        }
    }

    public static void addBookSource(List<BookSourceBean> bookSourceBeans) {
        for (BookSourceBean bookSourceBean : bookSourceBeans) {
            addBookSource(bookSourceBean);
        }
    }

    public static void addBookSource(BookSourceBean bookSourceBean) {
        if (TextUtils.isEmpty(bookSourceBean.getBookSourceName()) || TextUtils.isEmpty(bookSourceBean.getBookSourceUrl())) {
            return;
        }
        if (bookSourceBean.getBookSourceUrl().endsWith("/")) {
            bookSourceBean.setBookSourceUrl(bookSourceBean.getBookSourceUrl().replaceAll("/+$", ""));
        }
        BookSourceBean temp = AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().getByBookSourceUrl(bookSourceBean.getBookSourceUrl());
        if (temp != null) {
            bookSourceBean.setSerialNumber(temp.getSerialNumber());
        }
        if (bookSourceBean.getSerialNumber() < 0) {
            bookSourceBean.setSerialNumber((int) (AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().count() + 1));
        }
        AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().insertOrReplace(bookSourceBean);
    }

    public static void saveBookSource(BookSourceBean bookSourceBean) {
        if (bookSourceBean != null) {
            AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().insertOrReplace(bookSourceBean);
        }
    }

    public static void saveBookSource(List<BookSourceBean> bookSourceBeanList) {
        if (bookSourceBeanList != null) {
            AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().insertOrReplaceInTx(bookSourceBeanList);
        }
    }

    public static Single<Boolean> toTop(BookSourceBean sourceBean) {
        return Single.create((SingleOnSubscribe<Boolean>) e -> {
            List<BookSourceBean> beanList = getAllBookSourceBySerialNumber();
            for (int i = 0; i < beanList.size(); i++) {
                beanList.get(i).setSerialNumber(i + 1);
            }
            sourceBean.setSerialNumber(0);
            AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().insertOrReplaceInTx(beanList);
            AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().insertOrReplace(sourceBean);
            e.onSuccess(true);
        }).compose(RxUtil::toSimpleSingle);
    }

    public static List<String> getEnableGroupList() {
        List<String> groupList = new ArrayList<>();
        String sql = "SELECT DISTINCT  BookSourceGroup  FROM " + BookSourceDao.TABLENAME
                + " WHERE  Enable= 1";
        Cursor cursor = AppReaderDbHelper.getInstance().getDatabase().query(sql, null);
        if (!cursor.moveToFirst()) {
            return groupList;
        }
        do {
            String group = cursor.getString(0);
            if (!TextUtils.isEmpty(group) && !TextUtils.isEmpty(group.trim())) {
                for (String item : group.split("\\s*[,;，；]\\s*")) {
                    if (TextUtils.isEmpty(item) || groupList.contains(item)) {
                        continue;
                    }
                    groupList.add(item);
                }
            }
        } while (cursor.moveToNext());
        Collections.sort(groupList);
        return groupList;
    }

    public static List<String> getGroupList() {
        List<String> groupList = new ArrayList<>();
        String sql = "SELECT DISTINCT bookSourceGroup  FROM " + BookSourceDao.TABLENAME;
        Cursor cursor = AppReaderDbHelper.getInstance().getDatabase().query(sql, null);
        if (!cursor.moveToFirst()) {
            return groupList;
        }
        do {
            String group = cursor.getString(0);
            if (TextUtils.isEmpty(group) || TextUtils.isEmpty(group.trim())) {
                continue;
            }
            for (String item : group.split("\\s*[,;，；]\\s*")) {
                if (TextUtils.isEmpty(item) || groupList.contains(item)) {
                    continue;
                }
                groupList.add(item);
            }
        } while (cursor.moveToNext());
        Collections.sort(groupList);
        return groupList;
    }

    public static Observable<List<BookSourceBean>> importSource(String string) {
        if (StringUtils.isTrimEmpty(string)) {
            return null;
        }
        string = string.trim();
        if (NetworkUtils.isIPv4Address(string)) {
            string = String.format("http://%s:65501", string);
        }
        if (StringUtils.isJsonType(string)) {
            return importBookSourceFromJson(string.trim())
                    .compose(RxUtil::toSimpleSingle);
        }
        if (NetworkUtils.isUrl(string)) {
            return BaseModelImpl.getInstance().getRetrofitString(StringUtils.getBaseUrl(string), "utf-8")
                    .create(IHttpGetApi.class)
                    .get(string, AnalyzeHeaders.getDefaultHeader())
                    .flatMap(rsp -> importBookSourceFromJson(rsp.body()))
                    .compose(RxUtil::toSimpleSingle);
        }
        return Observable.error(new Exception("不是Json或Url格式"));
    }

    private static Observable<List<BookSourceBean>> importBookSourceFromJson(String json) {
        return Observable.create(e -> {
            List<BookSourceBean> bookSourceBeans = new ArrayList<>();
            if (StringUtils.isJsonArray(json)) {
                try {
                    //bookSourceBeans = GsonUtils.parseJArray(json, BookSourceBean.class);
                    bookSourceBeans = matchSourceList(json);
                    for (BookSourceBean bookSourceBean : bookSourceBeans) {
                        if (bookSourceBean.containsGroup("删除")) {
                            AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().delete(bookSourceBean.getBookSourceUrl());
                        } else {
                            try {
                                new URL(bookSourceBean.getBookSourceUrl());
                                bookSourceBean.setSerialNumber(0);
                                addBookSource(bookSourceBean);
                            } catch (Exception exception) {
                                AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().delete(bookSourceBean.getBookSourceUrl());
                            }
                        }
                    }
                    e.onNext(bookSourceBeans);
                    e.onComplete();
                    return;
                } catch (Exception ignored) {
                }
            }
            if (StringUtils.isJsonObject(json)) {
                try {
                    BookSourceBean bookSourceBean = GsonUtils.parseJObject(json, BookSourceBean.class);
                    addBookSource(bookSourceBean);
                    bookSourceBeans.add(bookSourceBean);
                    e.onNext(bookSourceBeans);
                    e.onComplete();
                    return;
                } catch (Exception ignored) {
                }
            }
            e.onError(new Throwable("格式不对"));
        });
    }

    public static void update(BookSourceBean data) {
        AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().update(data);
    }

    public static void update(List<BookSourceBean> datas) {
        AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().update(datas);
    }


    public static List<BookSourceBean> matchSourceList(String str) {
        Gson gson = new Gson();
        List<BookSource3Bean> bookSource3List = new ArrayList<>();
        List<BookSourceBean> bookSource2List = new ArrayList<>();
        int r2 = 0, r3 = 0;
        boolean b = str.charAt(0) == '[' && str.charAt(str.length() - 1) == ']';
        try {
            if (b) {
                bookSource3List = gson.fromJson(str, new TypeToken<List<BookSource3Bean>>() {
                }.getType());
            }
            r3 = gson.toJson(bookSource3List).length();
        } catch (Exception ignore) {
        }

        try {
            if (b) {
                bookSource2List = gson.fromJson(str, new TypeToken<List<BookSourceBean>>() {
                }.getType());
            }
            r2 = gson.toJson(bookSource2List).length();
            // r2 r3的计算在调用searchUrl2RuleSearchUrl() 等高级转换方法之前，是简化算法的粗糙的做法
            if (r2 > r3) {
                return bookSource2List;
            } else {
                bookSource2List.clear();
                for (BookSource3Bean source3Bean : bookSource3List) {
                    bookSource2List.add(source3Bean.toBookSourceBean());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookSource2List;
    }

    public static BookSourceBean matchSourceBean(String str) {
        Gson gson = new Gson();
        BookSource3Bean bookSource3Bean = new BookSource3Bean();
        BookSourceBean bookSource2Bean = new BookSourceBean();
        int r2 = 0, r3 = 0;
        boolean b = str.charAt(0) == '[' && str.charAt(str.length() - 1) == ']';
        //阅读3.0书源
        try {
            if (b) {
                List<BookSource3Bean> list = gson.fromJson(str, new TypeToken<List<BookSource3Bean>>() {
                }.getType());
                bookSource3Bean = list.get(0);
            } else {
                bookSource3Bean = gson.fromJson(str, BookSource3Bean.class);
            }
            r3 = gson.toJson(bookSource3Bean).length();
        } catch (Exception ignore) {
        }

        //2.0书源
        try {
            if (b) {
                List<BookSourceBean> list = gson.fromJson(str, new TypeToken<List<BookSourceBean>>() {
                }.getType());
                bookSource2Bean = list.get(0);
            } else {
                bookSource2Bean = gson.fromJson(str, BookSourceBean.class);
            }
            r2 = gson.toJson(bookSource2Bean).length();
            // r2 r3的计算在调用searchUrl2RuleSearchUrl() 等高级转换方法之前，是简化算法的粗糙的做法
            if (r2 > r3)
                return bookSource2Bean;
        } catch (Exception ignore) {
        }

        if (r3 > 0) {
            return bookSource3Bean.addGroupTag("阅读3.0书源").toBookSourceBean();
        }
        return bookSource2Bean;
    }


    public static String matchRuleFindUrl(String exploreUrl) {
        if (TextUtils.isEmpty(exploreUrl)) {
            return exploreUrl;
        }
        StringBuilder result = new StringBuilder(exploreUrl);
        try {
            Gson gson = new Gson();
            List<BookSource3FindBean> list = gson.fromJson(exploreUrl, new TypeToken<List<BookSource3FindBean>>() {
            }.getType());
            //3.0书源find
            if (list != null && list.size() > 0) {
                result = new StringBuilder();
                for (BookSource3FindBean findBean : list) {
                    if (!TextUtils.isEmpty(findBean.url)) {
                        result.append(findBean.toBookSource2Find());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

}

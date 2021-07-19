package com.micoredu.reader.model;

import android.database.Cursor;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.liuzhenli.common.SharedPreferencesUtil;
import com.liuzhenli.common.gson.GsonUtils;
import com.liuzhenli.common.utils.NetworkUtils;
import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.common.utils.StringUtils;
import com.micoredu.reader.analyzerule.AnalyzeHeaders;
import com.micoredu.reader.bean.BookSourceBean;
import com.micoredu.reader.dao.BookSourceDao;
import com.micoredu.reader.helper.AppReaderDbHelper;
import com.micoredu.reader.impl.IHttpGetApi;
import com.micoredu.reader.observe.BaseModelImpl;

import java.net.URL;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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
        return AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().getSelectedBookSource();
    }

    /***全部书源*/
    public static List<BookSourceBean> getAllBookSource() {
        return AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().getAllBookSource();
    }

    public static List<BookSourceBean> getSelectedBookSourceBySerialNumber() {
        return AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().getSelectedBookSourceBySerialNumber();
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
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<BookSourceBean> getSourceByKey(String keyword) {
        List<BookSourceBean> sourceList = AppReaderDbHelper.getInstance().getDatabase().getBookSourceDao().getSourceByKey(keyword);
        int sourceSort = SharedPreferencesUtil.getInstance().getInt("SourceSort", 0);
        sourceList.sort((o1, o2) -> {
            if (sourceSort == 1) {
                return Collator.getInstance(Locale.CHINESE).compare(o1.getWeight(), o2.getWeight());
            } else if (sourceSort == 2) {
                return Collator.getInstance(Locale.CHINESE).compare(o1.getBookSourceName(), o2.getBookSourceName());
            } else {
                return Collator.getInstance(Locale.CHINESE).compare(o1.getSerialNumber(), o2.getSerialNumber());
            }
        });
        return sourceList;
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
                    .get(string, AnalyzeHeaders.getMap(null))
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
                    bookSourceBeans = GsonUtils.parseJArray(json, BookSourceBean.class);
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

}

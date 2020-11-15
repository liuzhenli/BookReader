package com.micoredu.readerlib.model;

import android.database.Cursor;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.liuzhenli.common.SharedPreferencesUtil;
import com.liuzhenli.common.utils.GsonUtils;
import com.liuzhenli.common.utils.NetworkUtils;
import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.common.utils.StringUtils;
import com.liuzhenli.greendao.BookSourceBeanDao;
import com.micoredu.readerlib.analyzerule.AnalyzeHeaders;
import com.micoredu.readerlib.bean.BookSourceBean;
import com.micoredu.readerlib.helper.DbHelper;
import com.micoredu.readerlib.impl.IHttpGetApi;
import com.micoredu.readerlib.observe.BaseModelImpl;

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
        return DbHelper.getDaoSession().getBookSourceBeanDao().queryBuilder()
                .where(BookSourceBeanDao.Properties.Enable.eq(true))
                .orderRaw(BookSourceBeanDao.Properties.Weight.columnName + " DESC")
                .orderAsc(BookSourceBeanDao.Properties.SerialNumber)
                .list();
    }

    /***全部书源*/
    public static List<BookSourceBean> getAllBookSource() {
        return DbHelper.getDaoSession().getBookSourceBeanDao().queryBuilder()
                .orderRaw(getBookSourceSort())
                .orderAsc(BookSourceBeanDao.Properties.SerialNumber)
                .list();
    }

    public static List<BookSourceBean> getSelectedBookSourceBySerialNumber() {
        return DbHelper.getDaoSession().getBookSourceBeanDao().queryBuilder()
                .where(BookSourceBeanDao.Properties.Enable.eq(true))
                .orderAsc(BookSourceBeanDao.Properties.SerialNumber)
                .list();
    }

    public static List<BookSourceBean> getAllBookSourceBySerialNumber() {
        return DbHelper.getDaoSession().getBookSourceBeanDao().queryBuilder()
                .orderAsc(BookSourceBeanDao.Properties.SerialNumber)
                .list();
    }

    /***
     *根据分组选择可用书源
     * @param group 分组名字
     */
    public static List<BookSourceBean> getEnableSourceByGroup(String group) {
        return DbHelper.getDaoSession().getBookSourceBeanDao().queryBuilder()
                .where(BookSourceBeanDao.Properties.Enable.eq(true))
                .where(BookSourceBeanDao.Properties.BookSourceGroup.like("%" + group + "%"))
                .orderRaw(BookSourceBeanDao.Properties.Weight.columnName + " DESC")
                .list();
    }

    @Nullable
    public static BookSourceBean getBookSourceByUrl(String url) {
        if (url == null) return null;
        return DbHelper.getDaoSession().getBookSourceBeanDao().load(url);
    }

    public static void removeBookSource(BookSourceBean sourceBean) {
        if (sourceBean == null) {
            return;
        }
        DbHelper.getDaoSession().getBookSourceBeanDao().delete(sourceBean);
    }

    public static String getBookSourceSort() {
        switch (SharedPreferencesUtil.getInstance().getInt("SourceSort", 0)) {
            case 1:
                return BookSourceBeanDao.Properties.Weight.columnName + " DESC";
            case 2:
                return BookSourceBeanDao.Properties.BookSourceName.columnName + " COLLATE LOCALIZED ASC";
            default:
                return BookSourceBeanDao.Properties.SerialNumber.columnName + " ASC";
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
        BookSourceBean temp = DbHelper.getDaoSession().getBookSourceBeanDao().queryBuilder()
                .where(BookSourceBeanDao.Properties.BookSourceUrl.eq(bookSourceBean.getBookSourceUrl())).unique();
        if (temp != null) {
            bookSourceBean.setSerialNumber(temp.getSerialNumber());
        }
        if (bookSourceBean.getSerialNumber() < 0) {
            bookSourceBean.setSerialNumber((int) (DbHelper.getDaoSession().getBookSourceBeanDao().queryBuilder().count() + 1));
        }
        DbHelper.getDaoSession().getBookSourceBeanDao().insertOrReplace(bookSourceBean);
    }

    public static void saveBookSource(BookSourceBean bookSourceBean) {
        if (bookSourceBean != null) {
            DbHelper.getDaoSession().getBookSourceBeanDao().insertOrReplace(bookSourceBean);
        }
    }

    public static void saveBookSource(List<BookSourceBean> bookSourceBeanList) {
        if (bookSourceBeanList != null) {
            DbHelper.getDaoSession().getBookSourceBeanDao().insertOrReplaceInTx(bookSourceBeanList);
        }
    }

    public static void deleteBookSource(BookSourceBean bookSourceBean) {
        if (bookSourceBean != null) {
            DbHelper.getDaoSession().getBookSourceBeanDao().delete(bookSourceBean);
        }
    }

    public static Single<Boolean> toTop(BookSourceBean sourceBean) {
        return Single.create((SingleOnSubscribe<Boolean>) e -> {
            List<BookSourceBean> beanList = getAllBookSourceBySerialNumber();
            for (int i = 0; i < beanList.size(); i++) {
                beanList.get(i).setSerialNumber(i + 1);
            }
            sourceBean.setSerialNumber(0);
            DbHelper.getDaoSession().getBookSourceBeanDao().insertOrReplaceInTx(beanList);
            DbHelper.getDaoSession().getBookSourceBeanDao().insertOrReplace(sourceBean);
            e.onSuccess(true);
        }).compose(RxUtil::toSimpleSingle);
    }

    public static List<String> getEnableGroupList() {
        List<String> groupList = new ArrayList<>();
        String sql = "SELECT DISTINCT "
                + BookSourceBeanDao.Properties.BookSourceGroup.columnName
                + " FROM " + BookSourceBeanDao.TABLENAME
                + " WHERE " + BookSourceBeanDao.Properties.Enable.name + " = 1";
        Cursor cursor = DbHelper.getDaoSession().getDatabase().rawQuery(sql, null);
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
        String sql = "SELECT DISTINCT " + BookSourceBeanDao.Properties.BookSourceGroup.columnName + " FROM " + BookSourceBeanDao.TABLENAME;
        Cursor cursor = DbHelper.getDaoSession().getDatabase().rawQuery(sql, null);
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
                            DbHelper.getDaoSession().getBookSourceBeanDao().queryBuilder()
                                    .where(BookSourceBeanDao.Properties.BookSourceUrl.eq(bookSourceBean.getBookSourceUrl()))
                                    .buildDelete().executeDeleteWithoutDetachingEntities();
                        } else {
                            try {
                                new URL(bookSourceBean.getBookSourceUrl());
                                bookSourceBean.setSerialNumber(0);
                                addBookSource(bookSourceBean);
                            } catch (Exception exception) {
                                DbHelper.getDaoSession().getBookSourceBeanDao().queryBuilder()
                                        .where(BookSourceBeanDao.Properties.BookSourceUrl.eq(bookSourceBean.getBookSourceUrl()))
                                        .buildDelete().executeDeleteWithoutDetachingEntities();
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

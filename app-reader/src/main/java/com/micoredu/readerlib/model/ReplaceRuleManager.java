package com.micoredu.readerlib.model;

import android.text.TextUtils;

import com.liuzhenli.common.utils.GsonUtils;
import com.liuzhenli.common.utils.NetworkUtils;
import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.common.utils.StringUtils;
import com.liuzhenli.greendao.ReplaceRuleBeanDao;
import com.micoredu.readerlib.analyzerule.AnalyzeHeaders;
import com.micoredu.readerlib.bean.ReplaceRuleBean;
import com.micoredu.readerlib.helper.DbHelper;
import com.micoredu.readerlib.impl.IHttpGetApi;
import com.micoredu.readerlib.observe.BaseModelImpl;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;

/**
 * 替换规则管理
 */

public class ReplaceRuleManager {
    private static List<ReplaceRuleBean> replaceRuleBeansEnabled;

    public static List<ReplaceRuleBean> getEnabled() {
        if (replaceRuleBeansEnabled == null) {
            replaceRuleBeansEnabled = DbHelper.getDaoSession()
                    .getReplaceRuleBeanDao().queryBuilder()
                    .where(ReplaceRuleBeanDao.Properties.Enable.eq(true))
                    .orderAsc(ReplaceRuleBeanDao.Properties.SerialNumber)
                    .list();
        }
        return replaceRuleBeansEnabled;
    }

    public static Single<List<ReplaceRuleBean>> getAll() {
        return Single.create((SingleOnSubscribe<List<ReplaceRuleBean>>) emitter -> emitter.onSuccess(DbHelper.getDaoSession()
                .getReplaceRuleBeanDao().queryBuilder()
                .orderAsc(ReplaceRuleBeanDao.Properties.SerialNumber)
                .list())).compose(RxUtil::toSimpleSingle);
    }

    public static Single<Boolean> saveData(ReplaceRuleBean replaceRuleBean) {
        return Single.create((SingleOnSubscribe<Boolean>) emitter -> {
            if (replaceRuleBean.getSerialNumber() == 0) {
                replaceRuleBean.setSerialNumber((int) (DbHelper.getDaoSession().getReplaceRuleBeanDao().queryBuilder().count() + 1));
            }
            DbHelper.getDaoSession().getReplaceRuleBeanDao().insertOrReplace(replaceRuleBean);
            refreshDataS();
            emitter.onSuccess(true);
        }).compose(RxUtil::toSimpleSingle);
    }

    public static void delData(ReplaceRuleBean replaceRuleBean) {
        DbHelper.getDaoSession().getReplaceRuleBeanDao().delete(replaceRuleBean);
        refreshDataS();
    }

    public static void addDataS(List<ReplaceRuleBean> replaceRuleBeans) {
        if (replaceRuleBeans != null && replaceRuleBeans.size() > 0) {
            DbHelper.getDaoSession().getReplaceRuleBeanDao().insertOrReplaceInTx(replaceRuleBeans);
            refreshDataS();
        }
    }

    public static void delDataS(List<ReplaceRuleBean> replaceRuleBeans) {
        for (ReplaceRuleBean replaceRuleBean : replaceRuleBeans) {
            DbHelper.getDaoSession().getReplaceRuleBeanDao().delete(replaceRuleBean);
        }
        refreshDataS();
    }

    private static void refreshDataS() {
        replaceRuleBeansEnabled = DbHelper.getDaoSession()
                .getReplaceRuleBeanDao().queryBuilder()
                .where(ReplaceRuleBeanDao.Properties.Enable.eq(true))
                .orderAsc(ReplaceRuleBeanDao.Properties.SerialNumber)
                .list();
    }

    public static Observable<Boolean> importReplaceRule(String text) {
        if (TextUtils.isEmpty(text)) return null;
        text = text.trim();
        if (text.length() == 0) return null;
        if (StringUtils.isJsonType(text)) {
            return importReplaceRuleO(text)
                    .compose(RxUtil::toSimpleSingle);
        }
        if (NetworkUtils.isUrl(text)) {
            return BaseModelImpl.getInstance().getRetrofitString(StringUtils.getBaseUrl(text), "utf-8")
                    .create(IHttpGetApi.class)
                    .get(text, AnalyzeHeaders.getMap(null))
                    .flatMap(rsp -> importReplaceRuleO(rsp.body()))
                    .compose(RxUtil::toSimpleSingle);
        }
        return Observable.error(new Exception("不是Json或Url格式"));
    }

    private static Observable<Boolean> importReplaceRuleO(String json) {
        return Observable.create(e -> {
            try {
                List<ReplaceRuleBean> replaceRuleBeans = GsonUtils.parseJArray(json, ReplaceRuleBean.class);
                addDataS(replaceRuleBeans);
                e.onNext(true);
            } catch (Exception e1) {
                e1.printStackTrace();
                e.onNext(false);
            }
            e.onComplete();
        });
    }
}

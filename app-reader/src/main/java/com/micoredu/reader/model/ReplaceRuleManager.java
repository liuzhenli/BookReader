package com.micoredu.reader.model;

import android.text.TextUtils;

import com.liuzhenli.common.gson.GsonUtils;
import com.liuzhenli.common.utils.NetworkUtils;
import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.common.utils.StringUtils;
import com.micoredu.reader.analyzerule.AnalyzeHeaders;
import com.micoredu.reader.bean.ReplaceRuleBean;
import com.micoredu.reader.helper.AppReaderDbHelper;
import com.micoredu.reader.impl.IHttpGetApi;
import com.micoredu.reader.observe.BaseModelImpl;

import java.util.ArrayList;
import java.util.Collections;
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
            replaceRuleBeansEnabled = AppReaderDbHelper.getInstance().getDatabase()
                    .getReplaceRuleDao().getEnabled();
        }
        return replaceRuleBeansEnabled;
    }

    // 合并广告话术规则
    public static Single<Boolean> mergeAdRules(ReplaceRuleBean replaceRuleBean) {


        String rule = formateAdRule(replaceRuleBean.getRegex());

/*        String summary=replaceRuleBean.getReplaceSummary();
        if(summary==null)
            summary="";
        String sumary_pre=summary.split("-")[0];*/

        int sn = replaceRuleBean.getSerialNumber();
        if (sn == 0) {
            sn = (int) (AppReaderDbHelper.getInstance().getDatabase().getReplaceRuleDao().count() + 1);
            replaceRuleBean.setSerialNumber(sn);
        }

        List<ReplaceRuleBean> list = AppReaderDbHelper.getInstance().getDatabase().getReplaceRuleDao().getByProperties(1, replaceRuleBean.getReplaceSummary(), sn);
        if (list.size() < 1) {
            replaceRuleBean.setRegex(rule);
            return saveData(replaceRuleBean);
        } else {
            StringBuffer buffer = new StringBuffer(rule);
            for (ReplaceRuleBean li : list) {
                buffer.append('\n');
                buffer.append(li.getRegex());
//                    buffer.append(formateAdRule(rule.getRegex()));
            }
            replaceRuleBean.setRegex(formateAdRule(buffer.toString()));

            return Single.create((SingleOnSubscribe<Boolean>) emitter -> {

                AppReaderDbHelper.getInstance().getDatabase().getReplaceRuleDao().insertOrReplace(replaceRuleBean);
                for (ReplaceRuleBean li : list) {
                    AppReaderDbHelper.getInstance().getDatabase().getReplaceRuleDao().delete(li);
                }
                refreshDataS();
                emitter.onSuccess(true);
            }).compose(RxUtil::toSimpleSingle);

        }
    }

    // 把输入的规则进行预处理（分段、排序、去重）。保存的是普通多行文本。
    public static String formateAdRule(String rule) {

        if (rule == null)
            return "";
        String result = rule.trim();
        if (result.length() < 1)
            return "";

        String string = rule
//                用中文中的.视为。进行分段
                .replaceAll("(?<=([^a-zA-Z\\p{P}]{4,8}))\\.+(?![^a-zA-Z\\p{P}]{4,8})","\n")
//                用常见的适合分段的标点进行分段，句首句尾除外
//                .replaceAll("([^\\p{P}\n^])([…,，:：？。！?!~<>《》【】（）()]+)([^\\p{P}\n$])", "$1\n$3")
//                表达式无法解决句尾连续多个符号的问题
//                .replaceAll("[…,，:：？。！?!~<>《》【】（）()]+(?!\\s*\n|$)", "\n")
                .replaceAll("(?<![\\p{P}\n^])([…,，:：？。！?!~<>《》【】（）()]+)(?![\\p{P}\n$])", "\n")

                ;

        String[] lines = string.split("\n");
        List<String> list = new ArrayList<>();

        for (String s : lines) {
            s = s.trim()
//                    .replaceAll("\\s+", "\\s")
            ;
            if (!list.contains(s)) {
                list.add(s);
            }
        }
        Collections.sort(list);
        StringBuffer buffer = new StringBuffer(rule.length() + 1);
        for (int i = 0; i < list.size(); i++) {
            buffer.append('\n');
            buffer.append(list.get(i));
        }
        return buffer.toString().trim();
    }

    public static Single<List<ReplaceRuleBean>> getAll() {
        return Single.create((SingleOnSubscribe<List<ReplaceRuleBean>>) emitter -> emitter.onSuccess(AppReaderDbHelper.getInstance().getDatabase()
                .getReplaceRuleDao().getAll())).compose(RxUtil::toSimpleSingle);
    }

    public static Single<Boolean> saveData(ReplaceRuleBean replaceRuleBean) {
        return Single.create((SingleOnSubscribe<Boolean>) emitter -> {
            if (replaceRuleBean.getSerialNumber() == 0) {
                replaceRuleBean.setSerialNumber((int) (AppReaderDbHelper.getInstance().getDatabase().getReplaceRuleDao().count() + 1));
            }
            AppReaderDbHelper.getInstance().getDatabase().getReplaceRuleDao().insertOrReplace(replaceRuleBean);
            refreshDataS();
            emitter.onSuccess(true);
        }).compose(RxUtil::toSimpleSingle);
    }

    public static void delData(ReplaceRuleBean replaceRuleBean) {
        AppReaderDbHelper.getInstance().getDatabase().getReplaceRuleDao().delete(replaceRuleBean);
        refreshDataS();
    }

    public static void addDataS(List<ReplaceRuleBean> replaceRuleBeans) {
        if (replaceRuleBeans != null && replaceRuleBeans.size() > 0) {
            AppReaderDbHelper.getInstance().getDatabase().getReplaceRuleDao().insertOrReplaceInTx(replaceRuleBeans);
            refreshDataS();
        }
    }

    public static void delDataS(List<ReplaceRuleBean> replaceRuleBeans) {
        for (ReplaceRuleBean replaceRuleBean : replaceRuleBeans) {
            AppReaderDbHelper.getInstance().getDatabase().getReplaceRuleDao().delete(replaceRuleBean);
        }
        refreshDataS();
    }

    private static void refreshDataS() {
        replaceRuleBeansEnabled = AppReaderDbHelper.getInstance().getDatabase()
                .getReplaceRuleDao().getEnabled();
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
                    .get(text, AnalyzeHeaders.getDefaultHeader())
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

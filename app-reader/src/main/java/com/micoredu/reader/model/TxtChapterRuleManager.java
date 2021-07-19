package com.micoredu.reader.model;


import com.liuzhenli.common.BaseApplication;
import com.liuzhenli.common.gson.GsonUtils;
import com.liuzhenli.common.utils.IOUtils;
import com.micoredu.reader.bean.TxtChapterRuleBean;
import com.micoredu.reader.helper.AppReaderDbHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TxtChapterRuleManager {

    public static List<TxtChapterRuleBean> getAll() {
        List<TxtChapterRuleBean> beans = AppReaderDbHelper.getInstance().getDatabase().getTxtChapterRuleDao().loadAll();
        if (beans.isEmpty()) {
            return getDefault();
        }
        return beans;
    }

    public static List<TxtChapterRuleBean> getEnabled() {
        List<TxtChapterRuleBean> beans = AppReaderDbHelper.getInstance().getDatabase().getTxtChapterRuleDao().getEnabled();
        if (beans.isEmpty()) {
            return getAll();
        }
        return beans;
    }

    public static List<String> enabledRuleList() {
        List<TxtChapterRuleBean> beans = getEnabled();
        List<String> ruleList = new ArrayList<>();
        for (TxtChapterRuleBean chapterRuleBean : beans) {
            ruleList.add(chapterRuleBean.getRule());
        }
        return ruleList;
    }

    public static List<TxtChapterRuleBean> getDefault() {
        String json = null;
        try {
            InputStream inputStream = BaseApplication.getInstance().getAssets().open("txtChapterRule.json");
            json = IOUtils.toString(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<TxtChapterRuleBean> ruleBeanList = GsonUtils.parseJArray(json, TxtChapterRuleBean.class);
        if (ruleBeanList != null) {
            AppReaderDbHelper.getInstance().getDatabase().getTxtChapterRuleDao().insertOrReplaceInTx(ruleBeanList);
            return ruleBeanList;
        }
        return new ArrayList<>();
    }

    public static void del(TxtChapterRuleBean txtChapterRuleBean) {
        AppReaderDbHelper.getInstance().getDatabase().getTxtChapterRuleDao().delete(txtChapterRuleBean);
    }

    public static void del(List<TxtChapterRuleBean> ruleBeanList) {
        for (TxtChapterRuleBean ruleBean : ruleBeanList) {
            del(ruleBean);
        }
    }

    public static void save(TxtChapterRuleBean txtChapterRuleBean) {
        if (txtChapterRuleBean.getSerialNumber() == null) {
            List<TxtChapterRuleBean> list = AppReaderDbHelper.getInstance().getDatabase().getTxtChapterRuleDao().loadAll();
            txtChapterRuleBean.setSerialNumber(list == null ? 0 : list.size());
        }
        AppReaderDbHelper.getInstance().getDatabase().getTxtChapterRuleDao().insertOrReplace(txtChapterRuleBean);
    }

    public static void save(List<TxtChapterRuleBean> txtChapterRuleBeans) {
        AppReaderDbHelper.getInstance().getDatabase().getTxtChapterRuleDao().insertOrReplaceInTx(txtChapterRuleBeans);
    }
}

package com.micoredu.reader.bean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "txtChapterRule", primaryKeys = "name")
public class TxtChapterRuleBean {

    @NonNull
    private String name = "";
    private String rule;
    private Integer serialNumber;
    private Boolean enable;

    public TxtChapterRuleBean() {
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof TxtChapterRuleBean) {
            return Objects.equals(this.name, ((TxtChapterRuleBean) obj).name);
        }
        return false;
    }

    public TxtChapterRuleBean copy() {
        TxtChapterRuleBean ruleBean = new TxtChapterRuleBean();
        ruleBean.setName(name);
        ruleBean.setRule(rule);
        ruleBean.setEnable(enable);
        ruleBean.setSerialNumber(serialNumber);
        return ruleBean;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Boolean getEnable() {
        return enable == null ? true : enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}

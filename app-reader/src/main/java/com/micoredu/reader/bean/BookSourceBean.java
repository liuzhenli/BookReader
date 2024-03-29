package com.micoredu.reader.bean;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.google.gson.Gson;
import com.liuzhenli.common.utils.StringUtils;
import com.micoredu.reader.analyzerule.AnalyzeHeaders;
import com.micoredu.reader.helper.AppReaderDbHelper;
import com.micoredu.reader.model.BookSourceManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.text.TextUtils.isEmpty;
import static com.liuzhenli.common.constant.AppConstant.MAP_STRING;

/**
 * 书源信息
 */
@Entity(tableName = "bookSources", primaryKeys = "bookSourceUrl")
public class BookSourceBean implements Cloneable, Serializable {
    @Ignore
    private static final long serialVersionUID = -6111323241670458022L;
    @NonNull
    private String bookSourceUrl = "";
    private String bookSourceName;
    private String bookSourceGroup;
    private String bookSourceType;
    private String httpUserAgent;
    private String header;
    private String loginUrl;
    private String loginUi;
    private String loginCheckJs;
    private Long lastUpdateTime;
    /***手动排序编号*/
    private int serialNumber;
    /***智能排序的权重*/
    private int weight = 0;
    /***书源是否使用*/
    private boolean enable = true;
    /**
     * 发现规则  key:::value 的格式
     */
    private String ruleFindUrl;
    private String ruleFindList;
    private String ruleFindName;
    private String ruleFindAuthor;
    private String ruleFindKind;
    private String ruleFindIntroduce;
    private String ruleFindLastChapter;
    private String ruleFindCoverUrl;
    private String ruleFindNoteUrl;
    /*** 书源是否显示推荐*/
    private boolean ruleFindEnable;

    //搜索规则
    private String ruleSearchUrl;
    private String ruleSearchList;
    private String ruleSearchName;
    private String ruleSearchAuthor;
    private String ruleSearchKind;
    private String ruleSearchIntroduce;
    private String ruleSearchLastChapter;
    private String ruleSearchCoverUrl;
    private String ruleSearchNoteUrl;

    //详情页规则
    private String ruleBookUrlPattern;
    private String ruleBookInfoInit;
    private String ruleBookName;
    private String ruleBookAuthor;
    private String ruleCoverUrl;
    private String ruleIntroduce;
    private String ruleBookKind;
    private String ruleBookLastChapter;
    private String ruleChapterUrl;

    //目录页规则
    private String ruleChapterUrlNext;
    private String ruleChapterList;
    private String ruleChapterName;
    private String ruleContentUrl;
    private String ruleChapterVip;
    private String ruleChapterPay;


    //正文页规则
    private String ruleContentUrlNext;
    private String ruleBookContent;
    private String ruleBookContentReplace;
    private String payAction;

    @Ignore
    private transient ArrayList<String> groupList;

    public BookSourceBean() {
    }

    @Ignore
    public BookSourceBean(String bookSourceUrl, String bookSourceName, String bookSourceGroup, String bookSourceType, String httpUserAgent, String header,
                          String loginUrl, String loginUi, String loginCheckJs, Long lastUpdateTime, int serialNumber, int weight, boolean enable, String ruleFindUrl,
                          String ruleFindList, String ruleFindName, String ruleFindAuthor, String ruleFindKind, String ruleFindIntroduce, String ruleFindLastChapter,
                          String ruleFindCoverUrl, String ruleFindNoteUrl, boolean ruleFindEnable, String ruleSearchUrl, String ruleSearchList, String ruleSearchName, String ruleSearchAuthor,
                          String ruleSearchKind, String ruleSearchIntroduce, String ruleSearchLastChapter, String ruleSearchCoverUrl, String ruleSearchNoteUrl,
                          String ruleBookUrlPattern, String ruleBookInfoInit, String ruleBookName, String ruleBookAuthor, String ruleCoverUrl, String ruleIntroduce,
                          String ruleBookKind, String ruleBookLastChapter, String ruleChapterUrl, String ruleChapterUrlNext, String ruleChapterList,
                          String ruleChapterName, String ruleContentUrl, String ruleChapterVip, String ruleChapterPay, String ruleContentUrlNext,
                          String ruleBookContent, String ruleBookContentReplace, String payAction) {
        this.bookSourceUrl = bookSourceUrl;
        this.bookSourceName = bookSourceName;
        this.bookSourceGroup = bookSourceGroup;
        this.bookSourceType = bookSourceType;
        this.httpUserAgent = httpUserAgent;
        this.header = header;
        this.loginUrl = loginUrl;
        this.loginUi = loginUi;
        this.loginCheckJs = loginCheckJs;
        this.lastUpdateTime = lastUpdateTime;
        this.serialNumber = serialNumber;
        this.weight = weight;
        this.enable = enable;
        this.ruleFindUrl = ruleFindUrl;
        this.ruleFindList = ruleFindList;
        this.ruleFindName = ruleFindName;
        this.ruleFindAuthor = ruleFindAuthor;
        this.ruleFindKind = ruleFindKind;
        this.ruleFindIntroduce = ruleFindIntroduce;
        this.ruleFindLastChapter = ruleFindLastChapter;
        this.ruleFindCoverUrl = ruleFindCoverUrl;
        this.ruleFindNoteUrl = ruleFindNoteUrl;
        this.ruleFindEnable = ruleFindEnable;
        this.ruleSearchUrl = ruleSearchUrl;
        this.ruleSearchList = ruleSearchList;
        this.ruleSearchName = ruleSearchName;
        this.ruleSearchAuthor = ruleSearchAuthor;
        this.ruleSearchKind = ruleSearchKind;
        this.ruleSearchIntroduce = ruleSearchIntroduce;
        this.ruleSearchLastChapter = ruleSearchLastChapter;
        this.ruleSearchCoverUrl = ruleSearchCoverUrl;
        this.ruleSearchNoteUrl = ruleSearchNoteUrl;
        this.ruleBookUrlPattern = ruleBookUrlPattern;
        this.ruleBookInfoInit = ruleBookInfoInit;
        this.ruleBookName = ruleBookName;
        this.ruleBookAuthor = ruleBookAuthor;
        this.ruleCoverUrl = ruleCoverUrl;
        this.ruleIntroduce = ruleIntroduce;
        this.ruleBookKind = ruleBookKind;
        this.ruleBookLastChapter = ruleBookLastChapter;
        this.ruleChapterUrl = ruleChapterUrl;
        this.ruleChapterUrlNext = ruleChapterUrlNext;
        this.ruleChapterList = ruleChapterList;
        this.ruleChapterName = ruleChapterName;
        this.ruleContentUrl = ruleContentUrl;
        this.ruleChapterVip = ruleChapterVip;
        this.ruleChapterPay = ruleChapterPay;
        this.ruleContentUrlNext = ruleContentUrlNext;
        this.ruleBookContent = ruleBookContent;
        this.ruleBookContentReplace = ruleBookContentReplace;
        this.payAction = payAction;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BookSourceBean) {
            BookSourceBean bs = (BookSourceBean) obj;
            return stringEquals(bookSourceUrl, bs.bookSourceUrl)
                    && stringEquals(bookSourceName, bs.bookSourceName)
                    && stringEquals(bookSourceType, bs.bookSourceType)
                    && stringEquals(loginUrl, bs.loginUrl)
                    && stringEquals(bookSourceGroup, bs.bookSourceGroup)
                    && stringEquals(ruleBookName, bs.ruleBookName)
                    && stringEquals(ruleBookAuthor, bs.ruleBookAuthor)
                    && stringEquals(ruleChapterUrl, bs.ruleChapterUrl)
                    && stringEquals(ruleChapterUrlNext, ruleChapterUrlNext)
                    && stringEquals(ruleCoverUrl, bs.ruleCoverUrl)
                    && stringEquals(ruleIntroduce, bs.ruleIntroduce)
                    && stringEquals(ruleChapterList, bs.ruleChapterList)
                    && stringEquals(ruleChapterName, bs.ruleChapterName)
                    && stringEquals(ruleContentUrl, bs.ruleContentUrl)
                    && stringEquals(ruleContentUrlNext, bs.ruleContentUrlNext)
                    && stringEquals(ruleBookContent, bs.ruleBookContent)
                    && stringEquals(ruleSearchUrl, bs.ruleSearchUrl)
                    && stringEquals(ruleSearchList, bs.ruleSearchList)
                    && stringEquals(ruleSearchName, bs.ruleSearchName)
                    && stringEquals(ruleSearchAuthor, bs.ruleSearchAuthor)
                    && stringEquals(ruleSearchKind, bs.ruleSearchKind)
                    && stringEquals(ruleSearchLastChapter, bs.ruleSearchLastChapter)
                    && stringEquals(ruleSearchCoverUrl, bs.ruleSearchCoverUrl)
                    && stringEquals(ruleSearchNoteUrl, bs.ruleSearchNoteUrl)
                    && stringEquals(httpUserAgent, bs.httpUserAgent)
                    && stringEquals(ruleBookKind, bs.ruleBookKind)
                    && stringEquals(ruleBookLastChapter, bs.ruleBookLastChapter)
                    && stringEquals(ruleBookUrlPattern, bs.ruleBookUrlPattern)
                    && stringEquals(ruleBookContentReplace, bs.ruleBookContentReplace);
        }
        return false;
    }

    private Boolean stringEquals(String str1, String str2) {
        return Objects.equals(str1, str2) || (isEmpty(str1) && isEmpty(str2));
    }

    @Override
    public Object clone() {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(this);
            return gson.fromJson(json, BookSourceBean.class);
        } catch (Exception ignored) {
        }
        return this;
    }

    public String getBookSourceName() {
        return bookSourceName;
    }

    public void setBookSourceName(String bookSourceName) {
        this.bookSourceName = bookSourceName;
    }

    public String getBookSourceUrl() {
        return bookSourceUrl;
    }

    public void setBookSourceUrl(String bookSourceUrl) {
        this.bookSourceUrl = bookSourceUrl;
    }

    public int getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    // 换源时选择的源权重+500
    public void increaseWeightBySelection() {
        this.weight += 500;
    }

    public void increaseWeight(int increase) {
        this.weight += increase;
    }

    public boolean getEnable() {
        return this.enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getRuleBookName() {
        return this.ruleBookName;
    }

    public void setRuleBookName(String ruleBookName) {
        this.ruleBookName = ruleBookName;
    }

    public String getRuleBookAuthor() {
        return this.ruleBookAuthor;
    }

    public void setRuleBookAuthor(String ruleBookAuthor) {
        this.ruleBookAuthor = ruleBookAuthor;
    }

    public String getRuleChapterUrl() {
        return this.ruleChapterUrl;
    }

    public void setRuleChapterUrl(String ruleChapterUrl) {
        this.ruleChapterUrl = ruleChapterUrl;
    }

    public String getRuleCoverUrl() {
        return this.ruleCoverUrl;
    }

    public void setRuleCoverUrl(String ruleCoverUrl) {
        this.ruleCoverUrl = ruleCoverUrl;
    }

    public String getRuleIntroduce() {
        return this.ruleIntroduce;
    }

    public void setRuleIntroduce(String ruleIntroduce) {
        this.ruleIntroduce = ruleIntroduce;
    }

    public String getRuleBookContent() {
        return this.ruleBookContent;
    }

    public void setRuleBookContent(String ruleBookContent) {
        this.ruleBookContent = ruleBookContent;
    }

    public String getRuleSearchUrl() {
        return this.ruleSearchUrl;
    }

    public void setRuleSearchUrl(String ruleSearchUrl) {
        this.ruleSearchUrl = ruleSearchUrl;
    }

    public String getRuleContentUrl() {
        return this.ruleContentUrl;
    }

    public void setRuleContentUrl(String ruleContentUrl) {
        this.ruleContentUrl = ruleContentUrl;
    }

    public String getRuleSearchName() {
        return this.ruleSearchName;
    }

    public void setRuleSearchName(String ruleSearchName) {
        this.ruleSearchName = ruleSearchName;
    }

    public String getRuleSearchAuthor() {
        return this.ruleSearchAuthor;
    }

    public void setRuleSearchAuthor(String ruleSearchAuthor) {
        this.ruleSearchAuthor = ruleSearchAuthor;
    }

    public String getRuleSearchKind() {
        return this.ruleSearchKind;
    }

    public void setRuleSearchKind(String ruleSearchKind) {
        this.ruleSearchKind = ruleSearchKind;
    }

    public String getRuleSearchLastChapter() {
        return this.ruleSearchLastChapter;
    }

    public void setRuleSearchLastChapter(String ruleSearchLastChapter) {
        this.ruleSearchLastChapter = ruleSearchLastChapter;
    }

    public String getRuleSearchCoverUrl() {
        return this.ruleSearchCoverUrl;
    }

    public void setRuleSearchCoverUrl(String ruleSearchCoverUrl) {
        this.ruleSearchCoverUrl = ruleSearchCoverUrl;
    }

    public String getRuleSearchNoteUrl() {
        return this.ruleSearchNoteUrl;
    }

    public void setRuleSearchNoteUrl(String ruleSearchNoteUrl) {
        this.ruleSearchNoteUrl = ruleSearchNoteUrl;
    }

    public String getRuleSearchList() {
        return this.ruleSearchList;
    }

    public void setRuleSearchList(String ruleSearchList) {
        this.ruleSearchList = ruleSearchList;
    }

    public String getRuleChapterList() {
        return this.ruleChapterList;
    }

    public void setRuleChapterList(String ruleChapterList) {
        this.ruleChapterList = ruleChapterList;
    }

    public String getRuleChapterName() {
        return this.ruleChapterName;
    }

    public void setRuleChapterName(String ruleChapterName) {
        this.ruleChapterName = ruleChapterName;
    }

    public String getHttpUserAgent() {
        return this.httpUserAgent;
    }

    public void setHttpUserAgent(String httpHeaders) {
        this.httpUserAgent = httpHeaders;
    }

    public String getHeader() {
        return this.header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getRuleFindUrl() {
        return BookSourceManager.matchRuleFindUrl(this.ruleFindUrl);
    }

    public void setRuleFindUrl(String ruleFindUrl) {
        this.ruleFindUrl = ruleFindUrl;
    }

    public String getBookSourceGroup() {
        return this.bookSourceGroup;
    }

    public void setBookSourceGroup(String bookSourceGroup) {
        this.bookSourceGroup = bookSourceGroup;
        upGroupList();
        this.bookSourceGroup = TextUtils.join("; ", groupList);
    }

    public String getRuleChapterUrlNext() {
        return this.ruleChapterUrlNext;
    }

    public void setRuleChapterUrlNext(String ruleChapterUrlNext) {
        this.ruleChapterUrlNext = ruleChapterUrlNext;
    }

    public String getRuleContentUrlNext() {
        return this.ruleContentUrlNext;
    }

    public void setRuleContentUrlNext(String ruleContentUrlNext) {
        this.ruleContentUrlNext = ruleContentUrlNext;
    }

    public String getRuleBookUrlPattern() {
        return ruleBookUrlPattern;
    }

    public void setRuleBookUrlPattern(String ruleBookUrlPattern) {
        this.ruleBookUrlPattern = ruleBookUrlPattern;
    }

    public String getRuleBookKind() {
        return ruleBookKind;
    }

    public void setRuleBookKind(String ruleBookKind) {
        this.ruleBookKind = ruleBookKind;
    }

    public String getRuleBookLastChapter() {
        return ruleBookLastChapter;
    }

    public void setRuleBookLastChapter(String ruleBookLastChapter) {
        this.ruleBookLastChapter = ruleBookLastChapter;
    }

    private void upGroupList() {
        if (groupList == null)
            groupList = new ArrayList<>();
        else
            groupList.clear();
        if (!TextUtils.isEmpty(bookSourceGroup)) {
            for (String group : bookSourceGroup.split("\\s*[,;，；]\\s*")) {
                group = group.trim();
                if (TextUtils.isEmpty(group) || groupList.contains(group)) continue;
                groupList.add(group);
            }
        }
    }

    public void addGroup(String group) {
        if (groupList == null)
            upGroupList();
        if (!groupList.contains(group)) {
            groupList.add(group);
            updateModTime();
            bookSourceGroup = TextUtils.join("; ", groupList);
        }
    }

    public void removeGroup(String group) {
        if (groupList == null)
            upGroupList();
        if (groupList.contains(group)) {
            groupList.remove(group);
            updateModTime();
            bookSourceGroup = TextUtils.join("; ", groupList);
        }
    }

    public boolean containsGroup(String group) {
        if (groupList == null) {
            upGroupList();
        }
        return groupList.contains(group);
    }

    public Long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public void updateModTime() {
        this.lastUpdateTime = System.currentTimeMillis();
    }

    public String getLoginUrl() {
        return this.loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getBookSourceType() {
        return bookSourceType == null ? "" : bookSourceType;
    }

    public void setBookSourceType(String bookSourceType) {
        this.bookSourceType = bookSourceType;
    }

    public String getRuleSearchIntroduce() {
        return this.ruleSearchIntroduce;
    }

    public void setRuleSearchIntroduce(String ruleSearchIntroduce) {
        this.ruleSearchIntroduce = ruleSearchIntroduce;
    }

    public String getRuleFindList() {
        return this.ruleFindList;
    }

    public void setRuleFindList(String ruleFindList) {
        this.ruleFindList = ruleFindList;
    }

    public String getRuleFindName() {
        return this.ruleFindName;
    }

    public void setRuleFindName(String ruleFindName) {
        this.ruleFindName = ruleFindName;
    }

    public String getRuleFindAuthor() {
        return this.ruleFindAuthor;
    }

    public void setRuleFindAuthor(String ruleFindAuthor) {
        this.ruleFindAuthor = ruleFindAuthor;
    }

    public String getRuleFindKind() {
        return this.ruleFindKind;
    }

    public void setRuleFindKind(String ruleFindKind) {
        this.ruleFindKind = ruleFindKind;
    }

    public String getRuleFindIntroduce() {
        return this.ruleFindIntroduce;
    }

    public void setRuleFindIntroduce(String ruleFindIntroduce) {
        this.ruleFindIntroduce = ruleFindIntroduce;
    }

    public String getRuleFindLastChapter() {
        return this.ruleFindLastChapter;
    }

    public void setRuleFindLastChapter(String ruleFindLastChapter) {
        this.ruleFindLastChapter = ruleFindLastChapter;
    }

    public String getRuleFindCoverUrl() {
        return this.ruleFindCoverUrl;
    }

    public void setRuleFindCoverUrl(String ruleFindCoverUrl) {
        this.ruleFindCoverUrl = ruleFindCoverUrl;
    }

    public String getRuleFindNoteUrl() {
        return this.ruleFindNoteUrl;
    }

    public void setRuleFindNoteUrl(String ruleFindNoteUrl) {
        this.ruleFindNoteUrl = ruleFindNoteUrl;
    }

    public String getRuleBookInfoInit() {
        return this.ruleBookInfoInit;
    }

    public void setRuleBookInfoInit(String ruleBookInfoInit) {
        this.ruleBookInfoInit = ruleBookInfoInit;
    }

    public String getRuleBookContentReplace() {
        return ruleBookContentReplace;
    }

    public void setRuleBookContentReplace(String ruleBookContentReplace) {
        this.ruleBookContentReplace = ruleBookContentReplace;
    }

    @Override
    public String toString() {
        return "BookSourceBean{" +
                "bookSourceUrl='" + bookSourceUrl + '\'' +
                ", bookSourceName='" + bookSourceName + '\'' +
                ", bookSourceGroup='" + bookSourceGroup + '\'' +
                ", bookSourceType='" + bookSourceType + '\'' +
                ", loginUrl='" + loginUrl + '\'' +
                ", lastUpdateTime=" + lastUpdateTime +
                ", serialNumber=" + serialNumber +
                ", weight=" + weight +
                ", enable=" + enable +
                ", ruleFindUrl='" + ruleFindUrl + '\'' +
                ", ruleFindList='" + ruleFindList + '\'' +
                ", ruleFindName='" + ruleFindName + '\'' +
                ", ruleFindAuthor='" + ruleFindAuthor + '\'' +
                ", ruleFindKind='" + ruleFindKind + '\'' +
                ", ruleFindIntroduce='" + ruleFindIntroduce + '\'' +
                ", ruleFindLastChapter='" + ruleFindLastChapter + '\'' +
                ", ruleFindCoverUrl='" + ruleFindCoverUrl + '\'' +
                ", ruleFindNoteUrl='" + ruleFindNoteUrl + '\'' +
                ", ruleSearchUrl='" + ruleSearchUrl + '\'' +
                ", ruleSearchList='" + ruleSearchList + '\'' +
                ", ruleSearchName='" + ruleSearchName + '\'' +
                ", ruleSearchAuthor='" + ruleSearchAuthor + '\'' +
                ", ruleSearchKind='" + ruleSearchKind + '\'' +
                ", ruleSearchIntroduce='" + ruleSearchIntroduce + '\'' +
                ", ruleSearchLastChapter='" + ruleSearchLastChapter + '\'' +
                ", ruleSearchCoverUrl='" + ruleSearchCoverUrl + '\'' +
                ", ruleSearchNoteUrl='" + ruleSearchNoteUrl + '\'' +
                ", ruleBookUrlPattern='" + ruleBookUrlPattern + '\'' +
                ", ruleBookInfoInit='" + ruleBookInfoInit + '\'' +
                ", ruleBookName='" + ruleBookName + '\'' +
                ", ruleBookAuthor='" + ruleBookAuthor + '\'' +
                ", ruleCoverUrl='" + ruleCoverUrl + '\'' +
                ", ruleIntroduce='" + ruleIntroduce + '\'' +
                ", ruleBookKind='" + ruleBookKind + '\'' +
                ", ruleBookLastChapter='" + ruleBookLastChapter + '\'' +
                ", ruleChapterUrl='" + ruleChapterUrl + '\'' +
                ", ruleChapterUrlNext='" + ruleChapterUrlNext + '\'' +
                ", ruleChapterList='" + ruleChapterList + '\'' +
                ", ruleChapterName='" + ruleChapterName + '\'' +
                ", ruleContentUrl='" + ruleContentUrl + '\'' +
                ", ruleContentUrlNext='" + ruleContentUrlNext + '\'' +
                ", ruleBookContent='" + ruleBookContent + '\'' +
                ", httpUserAgent='" + httpUserAgent + '\'' +
                ", groupList=" + groupList +
                '}';
    }

    public boolean getRuleFindEnable() {
        return this.ruleFindEnable && !TextUtils.isEmpty(getRuleFindUrl());
    }

    public void setRuleFindEnable(boolean ruleFindEnable) {
        this.ruleFindEnable = ruleFindEnable;
    }

    public String getPayAction() {
        return this.payAction;
    }

    public void setPayAction(String payAction) {
        this.payAction = payAction;
    }

    public String getRuleChapterVip() {
        return this.ruleChapterVip;
    }

    public void setRuleChapterVip(String ruleChapterVip) {
        this.ruleChapterVip = ruleChapterVip;
    }

    public String getRuleChapterPay() {
        return this.ruleChapterPay;
    }

    public void setRuleChapterPay(String ruleChapterPay) {
        this.ruleChapterPay = ruleChapterPay;
    }

    public String getLoginUi() {
        return loginUi;
    }

    public void setLoginUi(String loginUi) {
        this.loginUi = loginUi;
    }

    public String getLoginCheckJs() {
        return loginCheckJs;
    }

    public void setLoginCheckJs(String loginCheckJs) {
        this.loginCheckJs = loginCheckJs;
    }


    public Map<String, String> getHeaderMap(Boolean hasLoginHeader) {
        Map<String, String> headerMap = new HashMap<>();
        String headers = getHttpUserAgent();
        if (!isEmpty(headers)) {
            if (StringUtils.isJsonObject(headers)) {
                Map<String, String> map = new Gson().fromJson(headers, MAP_STRING);
                headerMap.putAll(map);
            } else {
                headerMap.put("User-Agent", headers);
            }
        } else {
            headerMap.put("User-Agent", AnalyzeHeaders.getDefaultUserAgent());
        }
        CookieBean cookie = AppReaderDbHelper.getInstance().getDatabase().getCookieDao().load(bookSourceUrl);
        if (cookie != null) {
            headerMap.put("Cookie", cookie.getCookie());
        }
        if (hasLoginHeader) {
            headerMap.putAll(getLoginHeaderMap());
        }
        return headerMap;
    }


    /**
     * @return 登录头, Map格式
     */
    public Map<String, String> getLoginHeaderMap() {
        Map<String, String> headerMap = new HashMap<>();
        String header = getLoginHeader();
        if (header != null) {
            Map<String, String> map = new Gson().fromJson(header, MAP_STRING);
            if (map != null) {
                headerMap.putAll(map);
            }
        }
        return headerMap;
    }

    public String getLoginHeader() {
        CookieBean cookie = AppReaderDbHelper.getInstance().getDatabase().getCookieDao().load("loginHeader_" + bookSourceUrl);
        if (cookie == null) {
            return null;
        }
        return cookie.getCookie();
    }

}

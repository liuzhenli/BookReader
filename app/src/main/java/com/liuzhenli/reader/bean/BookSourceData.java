package com.liuzhenli.reader.bean;

import java.io.Serializable;

/**
 * Description:
 *
 * @author liuzhenli 2020/9/17
 * Email: 848808263@qq.com
 */
public class BookSourceData implements Serializable {

    /**
     * bookSourceComment :
     * bookSourceName : 连载网
     * bookSourceType : 0
     * bookSourceUrl : http://m.lianzais.com
     * customOrder : 0
     * enabled : true
     * enabledExplore : true
     * lastUpdateTime : 0
     * ruleBookInfo : {"author":"class.block_txt2@tag.p.2@a@text","coverUrl":"class.block_img2@img@src","intro":"class.intro_info@text","lastChapter":"{{@@class.block_txt2@tag.p.-1@a@text}}·{{@@class.block_txt2@tag.p.-2@text}}","name":"class.block_txt2@h2@text##《|》.*","tocUrl":"//*[contains(text(), \"最新章节\")]/@href"}
     * ruleContent : {"content":"id.nr1@html","nextContentUrl":"text.下一章@href"}
     * ruleExplore : {}
     * ruleSearch : {"author":"tag.a.2@text","bookList":"class.line","bookUrl":"tag.a.1@href","kind":"tag.a.0@text##\\[|\\]","name":"tag.a.1@text"}
     * ruleToc : {"chapterList":"class.chapter@li@a","chapterName":"text","chapterUrl":"href","nextTocUrl":"text.下一页@href"}
     * searchUrl : http://m.lianzais.com/modules/article/waps.php,{
     "charset": "gbk",
     "method": "POST",
     "body": "searchtype=articlename&searchkey={{key}}"}
     * weight : 0
     * bookSourceGroup :
     * bookUrlPattern :
     * exploreUrl : 总点击榜::/top/allvisit/{{page}}.html
     月点击榜::/top/monthvisit/{{page}}.html
     周点击榜::/top/weekvisit/{{page}}.html
     总推荐榜::/top/allvote/{{page}}.html
     月推荐榜::/top/monthvote/{{page}}.html
     周推荐榜::/top/weekvote/{{page}}.html
     总收藏榜::/top/goodnum/{{page}}.html
     总字数榜::/top/size/{{page}}.html
     强 推 榜::/top/toptime/{{page}}.html
     新 书 榜::/top/goodnew/{{page}}.html
     最新入库::/top/postdate/{{page}}.html
     最近更新::/top/lastupdate/{{page}}.html

     玄幻奇幻 • 全::/library/0_1_0_{{page}}.html
     玄幻奇幻 • 连::/library/0_1_1_{{page}}.html
     玄幻奇幻 • 完::/library/0_1_2_{{page}}.html
     武侠修真 • 全::/library/0_2_0_{{page}}.html
     武侠修真 • 连::/library/0_2_1_{{page}}.html
     武侠修真 • 完::/library/0_2_2_{{page}}.html
     都市生活 • 全::/library/0_3_0_{{page}}.html
     都市生活 • 连::/library/0_3_1_{{page}}.html
     都市生活 • 完::/library/0_3_2_{{page}}.html
     历史军事 • 全::/library/0_4_0_{{page}}.html
     历史军事 • 连::/library/0_4_1_{{page}}.html
     历史军事 • 完::/library/0_4_2_{{page}}.html
     游戏竞技 • 全::/library/0_5_0_{{page}}.html
     游戏竞技 • 连::/library/0_5_1_{{page}}.html
     游戏竞技 • 完::/library/0_5_2_{{page}}.html
     科幻未来 • 全::/library/0_6_0_{{page}}.html
     科幻未来 • 连::/library/0_6_1_{{page}}.html
     科幻未来 • 完::/library/0_6_2_{{page}}.html
     悬疑灵异 • 全::/library/0_7_0_{{page}}.html
     悬疑灵异 • 连::/library/0_7_1_{{page}}.html
     悬疑灵异 • 完::/library/0_7_2_{{page}}.html
     二 次 元 • 全::/library/0_8_0_{{page}}.html
     二 次 元 • 连::/library/0_8_1_{{page}}.html
     二 次 元 • 完::/library/0_8_2_{{page}}.html
     经典短篇 • 全::/library/0_9_0_{{page}}.html
     经典短篇 • 连::/library/0_9_1_{{page}}.html
     经典短篇 • 完::/library/0_9_2_{{page}}.html
     古代言情 • 全::/library/0_10_0_{{page}}.html
     古代言情 • 连::/library/0_10_1_{{page}}.html
     古代言情 • 完::/library/0_10_2_{{page}}.html
     现代言情 • 全::/library/0_11_0_{{page}}.html
     现代言情 • 连::/library/0_11_1_{{page}}.html
     现代言情 • 完::/library/0_11_2_{{page}}.html
     幻想奇缘 • 全::/library/0_12_0_{{page}}.html
     幻想奇缘 • 连::/library/0_12_1_{{page}}.html
     幻想奇缘 • 完::/library/0_12_2_{{page}}.html
     浪漫青春 • 全::/library/0_13_0_{{page}}.html
     浪漫青春 • 连::/library/0_13_1_{{page}}.html
     浪漫青春 • 完::/library/0_13_2_{{page}}.html
     网络情缘 • 全::/library/0_14_0_{{page}}.html
     网络情缘 • 连::/library/0_14_1_{{page}}.html
     网络情缘 • 完::/library/0_14_2_{{page}}.html
     科幻空间 • 全::/library/0_15_0_{{page}}.html
     科幻空间 • 连::/library/0_15_1_{{page}}.html
     科幻空间 • 完::/library/0_15_2_{{page}}.html
     恐怖灵异 • 全::/library/0_16_0_{{page}}.html
     恐怖灵异 • 连::/library/0_16_1_{{page}}.html
     恐怖灵异 • 完::/library/0_16_2_{{page}}.html
     N 次 元 • 全::/library/0_17_0_{{page}}.html
     N 次 元 • 连::/library/0_17_1_{{page}}.html
     N 次 元 • 完::/library/0_17_2_{{page}}.html
     言情美文 • 全::/library/0_18_0_{{page}}.html
     言情美文 • 连::/library/0_18_1_{{page}}.html
     言情美文 • 完::/library/0_18_2_{{page}}.html
     其他类型 • 全::/library/0_19_0_{{page}}.html
     其他类型 • 连::/library/0_19_1_{{page}}.html
     其他类型 • 完::/library/0_19_2_{{page}}.html
     * loginUrl : https://www.9txs.com/login.html
     * header : {
     "User-Agent": "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko, By Black Prism) Chrome/99.0 Safari/537.36"
     }
     * enable : false
     * httpUserAgent :
     * ruleBookAuthor : class.small.5@text#书籍作者：
     * ruleBookContent : id.content1@textNodes
     * ruleBookInfoInit :
     * ruleBookKind : class.wrap position@tag.span.0@tag.a.1@text
     * ruleBookLastChapter : class.small.6@tag.a.0@text
     * ruleBookName : class.detail_right@tag.h1.0@text#《.*?》全集
     * ruleBookUrlPattern :
     * ruleChapterList : class.pc_list.1@tag.li
     * ruleChapterName : tag.a.0@text
     * ruleChapterUrl : class.downButton.0@href
     * ruleChapterUrlNext :
     * ruleContentUrl : tag.a.0@href
     * ruleContentUrlNext :
     * ruleCoverUrl : class.detail_pic@tag.img.0@src
     * ruleFindAuthor : class.s@text#大小.*
     * ruleFindCoverUrl : tag.img.0@src
     * ruleFindIntroduce : class.u@text
     * ruleFindKind :
     * ruleFindLastChapter : tag.div.2@tag.a.0@text#最新章节：
     * ruleFindList : class.listBox@tag.li
     * ruleFindName : tag.a.0@text#《##》全集
     * ruleFindNoteUrl : tag.a.0@href
     * ruleFindUrl : 玄幻奇幻::/soft/sort01/{index_searchPage.html}
     武侠仙侠::/soft/sort02/{index_searchPage.html}
     女频言情::/soft/sort03/{index_searchPage.html}
     现代都市::/soft/sort04/{index_searchPage.html}
     历史军事::/soft/sort05/{index_searchPage.html}
     游戏竞技::/soft/sort06/{index_searchPage.html}
     科幻灵异::/soft/sort07/{index_searchPage.html}
     美文同人::/soft/sort08/{index_searchPage.html}
     剧本教程::/soft/sort09/{index_searchPage.html}
     名著杂志::/soft/sort010/{index_searchPage.html}
     技术其他::/soft/sort011/{index_searchPage.html}
     热门电子书::/s/hot/{index_searchPage.html}
     推荐电子书::/s/good/{index_searchPage.html}
     最新电子书::/s/new/{index_searchPage.html}
     * ruleIntroduce : class.showInfo.0@tag.p.0@text
     * ruleSearchAuthor : class.odd.1@text
     * ruleSearchCoverUrl :
     * ruleSearchIntroduce :
     * ruleSearchKind :
     * ruleSearchLastChapter : class.odd.2@text
     * ruleSearchList : class.grid@tag.tr
     * ruleSearchName : class.even@tag.a.0@text
     * ruleSearchNoteUrl : class.even@tag.a.0@href
     * ruleSearchUrl : https://www.xqishuta.com/search.html?searchkey=searchKey
     * serialNumber : 10010
     */

    public String bookSourceComment;
    public String bookSourceName;
    public int bookSourceType;
    public String bookSourceUrl;
    public int customOrder;
    public boolean enabled;
    public boolean enabledExplore;
    public int lastUpdateTime;
    public RuleBookInfoBean ruleBookInfo;
    public RuleContentBean ruleContent;
    public RuleExploreBean ruleExplore;
    public RuleSearchBean ruleSearch;
    public RuleTocBean ruleToc;
    public String searchUrl;
    public int weight;
    public String bookSourceGroup;
    public String bookUrlPattern;
    public String exploreUrl;
    public String loginUrl;
    public String header;
    public boolean enable;
    public String httpUserAgent;
    public String ruleBookAuthor;
    public String ruleBookContent;
    public String ruleBookInfoInit;
    public String ruleBookKind;
    public String ruleBookLastChapter;
    public String ruleBookName;
    public String ruleBookUrlPattern;
    public String ruleChapterList;
    public String ruleChapterName;
    public String ruleChapterUrl;
    public String ruleChapterUrlNext;
    public String ruleContentUrl;
    public String ruleContentUrlNext;
    public String ruleCoverUrl;
    public String ruleFindAuthor;
    public String ruleFindCoverUrl;
    public String ruleFindIntroduce;
    public String ruleFindKind;
    public String ruleFindLastChapter;
    public String ruleFindList;
    public String ruleFindName;
    public String ruleFindNoteUrl;
    public String ruleFindUrl;
    public String ruleIntroduce;
    public String ruleSearchAuthor;
    public String ruleSearchCoverUrl;
    public String ruleSearchIntroduce;
    public String ruleSearchKind;
    public String ruleSearchLastChapter;
    public String ruleSearchList;
    public String ruleSearchName;
    public String ruleSearchNoteUrl;
    public String ruleSearchUrl;
    public int serialNumber;

    public static class RuleBookInfoBean {
        /**
         * author : class.block_txt2@tag.p.2@a@text
         * coverUrl : class.block_img2@img@src
         * intro : class.intro_info@text
         * lastChapter : {{@@class.block_txt2@tag.p.-1@a@text}}·{{@@class.block_txt2@tag.p.-2@text}}
         * name : class.block_txt2@h2@text##《|》.*
         * tocUrl : //*[contains(text(), "最新章节")]/@href
         */

        public String author;
        public String coverUrl;
        public String intro;
        public String lastChapter;
        public String name;
        public String tocUrl;
    }

    public static class RuleContentBean {
        /**
         * content : id.nr1@html
         * nextContentUrl : text.下一章@href
         */

        public String content;
        public String nextContentUrl;
    }

    public static class RuleExploreBean {
    }

    public static class RuleSearchBean {
        /**
         * author : tag.a.2@text
         * bookList : class.line
         * bookUrl : tag.a.1@href
         * kind : tag.a.0@text##\[|\]
         * name : tag.a.1@text
         */

        public String author;
        public String bookList;
        public String bookUrl;
        public String kind;
        public String name;
    }

    public static class RuleTocBean {
        /**
         * chapterList : class.chapter@li@a
         * chapterName : text
         * chapterUrl : href
         * nextTocUrl : text.下一页@href
         */

        public String chapterList;
        public String chapterName;
        public String chapterUrl;
        public String nextTocUrl;
    }
}

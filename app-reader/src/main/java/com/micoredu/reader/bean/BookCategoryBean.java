package com.micoredu.reader.bean;

/**
 * Description: 书分类
 * 科普书籍:https://www.100ben.net/kepushuji/list_searchPage.html"
 *
 * @author liuzhenli 2020/10/20
 * Email: 848808263@qq.com
 */
public class BookCategoryBean {
    public String name;
    public String url;

    public BookCategoryBean(String name, String url) {
        this.name = name;
        this.url = url;
    }
}

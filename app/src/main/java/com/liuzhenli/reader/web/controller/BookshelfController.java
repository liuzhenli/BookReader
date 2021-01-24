package com.liuzhenli.reader.web.controller;

import android.text.TextUtils;


import com.liuzhenli.common.utils.GsonUtils;
import com.liuzhenli.reader.web.utils.ReturnData;
import com.micoredu.readerlib.bean.BookChapterBean;
import com.micoredu.readerlib.bean.BookContentBean;
import com.micoredu.readerlib.bean.BookShelfBean;
import com.micoredu.readerlib.helper.BookshelfHelper;
import com.micoredu.readerlib.helper.DbHelper;
import com.micoredu.readerlib.model.WebBookModel;

import java.util.List;
import java.util.Map;

public class BookshelfController {

    public ReturnData getBookshelf() {
        List<BookShelfBean> shelfBeans = BookshelfHelper.getAllBook();
        ReturnData returnData = new ReturnData();
        if (shelfBeans.isEmpty()) {
            return returnData.setErrorMsg("还没有添加小说");
        }
        return returnData.setData(shelfBeans);
    }

    public ReturnData getChapterList(Map<String, List<String>> parameters) {
        List<String> strings = parameters.get("url");
        ReturnData returnData = new ReturnData();
        if (strings == null) {
            return returnData.setErrorMsg("参数url不能为空，请指定书籍地址");
        }
        List<BookChapterBean> chapterList = BookshelfHelper.getChapterList(strings.get(0));
        return returnData.setData(chapterList);
    }

    public ReturnData getBookContent(Map<String, List<String>> parameters) {
        List<String> strings = parameters.get("url");
        ReturnData returnData = new ReturnData();
        if (strings == null) {
            return returnData.setErrorMsg("参数url不能为空，请指定内容地址");
        }
        BookChapterBean chapter = DbHelper.getDaoSession().getBookChapterBeanDao().load(strings.get(0));
        if (chapter == null) {
            return returnData.setErrorMsg("未找到");
        }
        BookShelfBean bookShelfBean = BookshelfHelper.getBook(chapter.getNoteUrl());
        if (bookShelfBean == null) {
            return returnData.setErrorMsg("未找到");
        }
        String content = BookshelfHelper.getChapterCache(bookShelfBean, chapter);
        if (!TextUtils.isEmpty(content)) {
            return returnData.setData(content);
        }
        try {
            BookContentBean bookContentBean = WebBookModel.getInstance().getBookContent(bookShelfBean, chapter, null).blockingFirst();
            return returnData.setData(bookContentBean.getDurChapterContent());
        } catch (Exception e) {
            return returnData.setErrorMsg(e.getMessage());
        }
    }

    public ReturnData saveBook(String postData) {
        BookShelfBean bookShelfBean = GsonUtils.parseJObject(postData, BookShelfBean.class);
        ReturnData returnData = new ReturnData();
        if (bookShelfBean != null) {
            DbHelper.getDaoSession().getBookShelfBeanDao().insertOrReplace(bookShelfBean);
            return returnData.setData("");
        }
        return returnData.setErrorMsg("格式不对");
    }

}

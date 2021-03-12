package com.liuzhenli.reader.task;

import com.liuzhenli.common.utils.StringUtils;
import com.micoredu.reader.bean.BookInfoBean;
import com.micoredu.reader.bean.BookShelfBean;
import com.micoredu.reader.bean.LocBookShelfBean;
import com.micoredu.reader.helper.BookshelfHelper;
import com.micoredu.reader.helper.DbHelper;
import com.microedu.reader.R;

import java.io.File;

import io.reactivex.Observable;

/**
 * describe: 数据书到数据库
 *
 * @author Liuzhenli on 2019-12-29 17:57
 * @since 1.0.0
 */
public class ImportBookTask {
    public static ImportBookTask getInstance() {
        return new ImportBookTask();
    }

    public Observable<LocBookShelfBean> importBook(final File file) {
        return Observable.create(e -> {
            //判断文件是否存在
            boolean isNew = false;
            BookShelfBean bookShelfBean = BookshelfHelper.getBook(file.getAbsolutePath());
            if (bookShelfBean == null) {
                isNew = true;
                bookShelfBean = new BookShelfBean();
                bookShelfBean.setHasUpdate(true);
                bookShelfBean.setFinalDate(System.currentTimeMillis());
                bookShelfBean.setDurChapter(0);
                bookShelfBean.setDurChapterPage(0);
                bookShelfBean.setGroup(3);
                bookShelfBean.setTag(BookShelfBean.LOCAL_TAG);
                bookShelfBean.setNoteUrl(file.getAbsolutePath());
                bookShelfBean.setAllowUpdate(false);

                BookInfoBean bookInfoBean = bookShelfBean.getBookInfoBean();
                String fileName = file.getName();
                int lastDotIndex = file.getName().lastIndexOf(".");
                if (lastDotIndex > 0)
                    fileName = fileName.substring(0, lastDotIndex);
                int authorIndex = fileName.indexOf("作者");
                if (authorIndex != -1) {
                    bookInfoBean.setAuthor(fileName.substring(authorIndex));
                    fileName = fileName.substring(0, authorIndex).trim();
                } else {
                    bookInfoBean.setAuthor("");
                }
                int smhStart = fileName.indexOf("《");
                int smhEnd = fileName.indexOf("》");
                if (smhStart != -1 && smhEnd != -1) {
                    bookInfoBean.setName(fileName.substring(smhStart + 1, smhEnd));
                } else {
                    bookInfoBean.setName(fileName);
                }
                bookInfoBean.setFinalRefreshData(file.lastModified());
                bookInfoBean.setCoverUrl("");
                bookInfoBean.setNoteUrl(file.getAbsolutePath());
                bookInfoBean.setTag(BookShelfBean.LOCAL_TAG);
                bookInfoBean.setOrigin(StringUtils.getString(R.string.local));

                DbHelper.getDaoSession().getBookInfoBeanDao().insertOrReplace(bookInfoBean);
                DbHelper.getDaoSession().getBookShelfBeanDao().insertOrReplace(bookShelfBean);
            }
            e.onNext(new LocBookShelfBean(isNew, bookShelfBean));
            e.onComplete();
        });
    }
}

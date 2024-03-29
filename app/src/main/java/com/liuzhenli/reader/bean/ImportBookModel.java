package com.liuzhenli.reader.bean;

import android.net.Uri;

import com.liuzhenli.common.utils.Constant;
import com.liuzhenli.common.utils.FileUtils;
import com.liuzhenli.common.utils.L;
import com.liuzhenli.common.utils.StringUtils;
import com.liuzhenli.common.utils.filepicker.entity.FileItem;
import com.liuzhenli.reader.ReaderApplication;
import com.micoredu.reader.bean.BookInfoBean;
import com.micoredu.reader.bean.BookShelfBean;
import com.micoredu.reader.bean.LocBookShelfBean;
import com.micoredu.reader.helper.BookshelfHelper;
import com.micoredu.reader.helper.AppReaderDbHelper;
import com.micoredu.reader.observe.BaseModelImpl;
import com.microedu.reader.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Observable;

/**
 * 本地书导入
 */
public class ImportBookModel extends BaseModelImpl {

    public static ImportBookModel getInstance() {
        return new ImportBookModel();
    }

    public Observable<LocBookShelfBean> importBook(final FileItem localFileBean) {
        return Observable.create(e -> {
            // if is content path file, copy to local path
            if (FileUtils.isContentFile(localFileBean.path)) {
                FileUtils.createDir(Constant.LOCAL_BOOK_PATH);
                File file = FileUtils.createFile(Constant.LOCAL_BOOK_PATH + localFileBean.name);

                InputStream inputStream = ReaderApplication.getInstance().getContentResolver().openInputStream(Uri.parse(localFileBean.path));
                OutputStream outputStream = new FileOutputStream(file);
                byte[] buf = new byte[1024 * 100];
                int n;
                while ((n = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, n);
                    outputStream.flush();
                }
                outputStream.close();
                inputStream.close();
            }

            //判断文件是否存在
            //File file = localFileBean.file;
            boolean isNew = false;

            BookShelfBean bookShelfBean = BookshelfHelper.getBook(localFileBean.path);
            if (bookShelfBean == null) {
                isNew = true;
                bookShelfBean = new BookShelfBean();
                //是否有更新
                bookShelfBean.setHasUpdate(true);
                //加入书架日期
                bookShelfBean.setFinalDate(System.currentTimeMillis());
                //当前章节
                bookShelfBean.setDurChapter(0);
                //当前章节的位置(页码)
                bookShelfBean.setDurChapterPage(0);
                bookShelfBean.setGroup(3);
                bookShelfBean.setTag(BookShelfBean.LOCAL_TAG);
                bookShelfBean.setNoteUrl(localFileBean.path);
                bookShelfBean.setAllowUpdate(false);

                BookInfoBean bookInfoBean = bookShelfBean.getBookInfoBean();
                String fileName = localFileBean.name;
                int lastDotIndex = fileName.lastIndexOf(".");
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
                bookInfoBean.setFinalRefreshData(localFileBean.time.getTime());
                bookInfoBean.setCoverUrl("");
                bookInfoBean.setNoteUrl(localFileBean.path);
                bookInfoBean.setTag(BookShelfBean.LOCAL_TAG);
                bookInfoBean.setOrigin(StringUtils.getString(R.string.local));

                AppReaderDbHelper.getInstance().getDatabase().getBookInfoDao().insertOrReplace(bookInfoBean);
                AppReaderDbHelper.getInstance().getDatabase().getBookShelfDao().insertOrReplace(bookShelfBean);
            }
            e.onNext(new LocBookShelfBean(isNew, bookShelfBean));
            e.onComplete();
        });
    }

}

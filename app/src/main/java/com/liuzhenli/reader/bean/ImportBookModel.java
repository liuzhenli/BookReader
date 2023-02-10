package com.liuzhenli.reader.bean;

import android.net.Uri;

import com.liuzhenli.common.utils.Constant;
import com.liuzhenli.common.utils.FileUtils;
import com.liuzhenli.common.utils.StringUtils;
import com.liuzhenli.common.utils.filepicker.entity.FileItem;
import com.liuzhenli.reader.ReaderApplication;
import com.micoredu.reader.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Observable;

/**
 * 本地书导入
 */
/*
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

            Book Book = BookHelp.getBook(localFileBean.path);
            if (Book == null) {
                isNew = true;
                Book = new Book();
                //是否有更新
                Book.setHasUpdate(true);
                //加入书架日期
                Book.setFinalDate(System.currentTimeMillis());
                //当前章节
                Book.setDurChapter(0);
                //当前章节的位置(页码)
                Book.setDurChapterPage(0);
                Book.setGroup(3);
                Book.setTag(Book.LOCAL_TAG);
                Book.setNoteUrl(localFileBean.path);
                Book.setAllowUpdate(false);

                BookInfo BookInfo = Book.getBookInfoBean();
                String fileName = localFileBean.name;
                int lastDotIndex = fileName.lastIndexOf(".");
                if (lastDotIndex > 0)
                    fileName = fileName.substring(0, lastDotIndex);
                int authorIndex = fileName.indexOf("作者");
                if (authorIndex != -1) {
                    BookInfo.setAuthor(fileName.substring(authorIndex));
                    fileName = fileName.substring(0, authorIndex).trim();
                } else {
                    BookInfo.setAuthor("");
                }
                int smhStart = fileName.indexOf("《");
                int smhEnd = fileName.indexOf("》");
                if (smhStart != -1 && smhEnd != -1) {
                    BookInfo.setName(fileName.substring(smhStart + 1, smhEnd));
                } else {
                    BookInfo.setName(fileName);
                }
                BookInfo.setFinalRefreshData(localFileBean.time.getTime());
                BookInfo.setCoverUrl("");
                BookInfo.setNoteUrl(localFileBean.path);
                BookInfo.setTag(Book.LOCAL_TAG);
                BookInfo.setOrigin(StringUtils.getString(R.string.local));

                AppReaderDbHelper.getInstance().getDatabase().getBookInfoDao().insertOrReplace(BookInfo);
                AppReaderDbHelper.getInstance().getDatabase().getBookShelfDao().insertOrReplace(Book);
            }
            e.onNext(new LocBookShelfBean(isNew, Book));
            e.onComplete();
        });
    }

}


 */
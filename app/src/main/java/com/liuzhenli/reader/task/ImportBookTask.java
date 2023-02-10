//package com.liuzhenli.reader.task;
//
//import com.liuzhenli.common.utils.StringUtils;
//import com.micoredu.reader.R;
//
//import java.io.File;
//
//import io.reactivex.Observable;
//
///**
// * describe: 数据书到数据库
// *
// * @author Liuzhenli on 2019-12-29 17:57
// * @since 1.0.0
// */
//public class ImportBookTask {
//    public static ImportBookTask getInstance() {
//        return new ImportBookTask();
//    }
//
//    public Observable<LocBookShelfBean> importBook(final File file) {
//        return Observable.create(e -> {
//            //判断文件是否存在
//            boolean isNew = false;
//            Book Book = BookHelp.getBook(file.getAbsolutePath());
//            if (Book == null) {
//                isNew = true;
//                Book = new Book();
//                Book.setHasUpdate(true);
//                Book.setFinalDate(System.currentTimeMillis());
//                Book.setDurChapter(0);
//                Book.setDurChapterPage(0);
//                Book.setGroup(3);
//                Book.setTag(Book.LOCAL_TAG);
//                Book.setNoteUrl(file.getAbsolutePath());
//                Book.setAllowUpdate(false);
//
//                BookInfo BookInfo = Book.getBookInfoBean();
//                String fileName = file.getName();
//                int lastDotIndex = file.getName().lastIndexOf(".");
//                if (lastDotIndex > 0)
//                    fileName = fileName.substring(0, lastDotIndex);
//                int authorIndex = fileName.indexOf("作者");
//                if (authorIndex != -1) {
//                    BookInfo.setAuthor(fileName.substring(authorIndex));
//                    fileName = fileName.substring(0, authorIndex).trim();
//                } else {
//                    BookInfo.setAuthor("");
//                }
//                int smhStart = fileName.indexOf("《");
//                int smhEnd = fileName.indexOf("》");
//                if (smhStart != -1 && smhEnd != -1) {
//                    BookInfo.setName(fileName.substring(smhStart + 1, smhEnd));
//                } else {
//                    BookInfo.setName(fileName);
//                }
//                BookInfo.setFinalRefreshData(file.lastModified());
//                BookInfo.setCoverUrl("");
//                BookInfo.setNoteUrl(file.getAbsolutePath());
//                BookInfo.setTag(Book.LOCAL_TAG);
//                BookInfo.setOrigin(StringUtils.getString(R.string.local));
//
//                AppReaderDbHelper.getInstance().getDatabase().getBookInfoDao().insertOrReplace(BookInfo);
//                AppReaderDbHelper.getInstance().getDatabase().getBookShelfDao().insertOrReplace(Book);
//            }
//            e.onNext(new LocBookShelfBean(isNew, Book));
//            e.onComplete();
//        });
//    }
//}

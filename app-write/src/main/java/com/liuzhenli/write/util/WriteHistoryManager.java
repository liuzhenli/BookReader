package com.liuzhenli.write.util;

import com.liuzhenli.common.SharedPreferencesUtil;
import com.liuzhenli.common.utils.FileUtils;
import com.liuzhenli.write.bean.WriteChapter;
import com.liuzhenli.write.bean.WriteHistory;
import com.liuzhenli.write.data.WriteChapterDao;
import com.liuzhenli.write.data.WriteHistoryDao;
import com.liuzhenli.write.helper.WriteDbHelper;

import java.io.File;

/**
 * Description:
 *
 * @author liuzhenli 2021/5/27
 * Email: 848808263@qq.com
 */
public class WriteHistoryManager {

    /*** In 5 seconds save one time is allowed*/
    private static final int TIME_INTEVAL = 5000;

    /*** 50 characters change is required*/
    private static final int WORD_INTEVAL = 50;

    /***max history file count*/
    public static int MAX_HISTORY_FILE_COUNT = 500;
    /***max draft file count*/
    public static int MAX_DRAFT_FILE_COUNT = 500;

    private static WriteHistoryManager instance;

    private long mBookId, mChapterId;

    /*** last save history time */
    private long mLastTime;

    /*** content wordCount*/
    private int mLastWordCount;
    private WriteChapterDao mDao;
    private WriteChapter mWriteChapter;

    private int mLastHistoryFileIndex;
    private int mLastDraftFileIndex;

    private WriteHistoryManager(long bookId, long chapterId) {
        init(bookId, chapterId);
    }

    private void init(long localBookId, long localChapterId) {
        this.mBookId = localBookId;
        this.mChapterId = localChapterId;
        mDao = WriteDbHelper.getInstance().getAppDatabase().getWriteChapterDao();
        mWriteChapter = mDao.getWriteChapter(mBookId,mChapterId);
        mLastHistoryFileIndex = SharedPreferencesUtil.getInstance().getInt("lastHistoryFileIndex");
    }

    public static synchronized WriteHistoryManager getInstance(long bookId, long chapterId) {
        if (instance == null) {
            synchronized (WriteHistoryManager.class) {
                if (instance == null) {
                    instance = new WriteHistoryManager(bookId, chapterId);
                } else {
                    instance.init(bookId, chapterId);
                }
            }
        }
        return instance;
    }

    public void saveHistory(String content, int wordCont) {
        if (System.currentTimeMillis() - mLastTime < TIME_INTEVAL && Math.abs(wordCont - mLastWordCount) < WORD_INTEVAL) {
            return;
        }
        QETag qEtag = new QETag();
        mLastWordCount = wordCont;
        mLastTime = System.currentTimeMillis();
        WriteHistory writeHistory = WriteDbHelper.getInstance().getAppDatabase().getWriteHistoryDao().getWriteHistory(mBookId,mChapterId);

        if (writeHistory == null) {
            String historyPath = WriteConstants.PATH_WRITE_BOOK + File.separator + "history";

            FileUtils.createDir(historyPath);
            if (mLastHistoryFileIndex >= MAX_HISTORY_FILE_COUNT) {
                mLastHistoryFileIndex = 0;
            }

            String historyFilePath = historyPath + "/" + mLastHistoryFileIndex + ".xml";

            try {
                FileUtils.INSTANCE.writeFile(historyFilePath, content);

                mLastTime = System.currentTimeMillis();

                WriteHistory history = new WriteHistory();
                history.setLocalBookId (mBookId);
                history.setLocalBookId(mChapterId);
                history.setTime(mLastTime);
                history.setETag(qEtag.calcETagWithString(content));
                history.setFileIndex(mLastDraftFileIndex);
                history.setType(WriteConstants.HistoryType.HISTORY);
                history.setWordCount( wordCont);
                WriteDbHelper.getInstance().getAppDatabase().getWriteHistoryDao().insert(history);
                mLastHistoryFileIndex++;

                mLastWordCount = wordCont;

                SharedPreferencesUtil.getInstance().putInt("lastHistoryFileIndex", mLastHistoryFileIndex);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}

package com.liuzhenli.write.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.liuzhenli.common.BaseApplication;
import com.liuzhenli.write.greendao.DaoMaster;
import com.liuzhenli.write.greendao.DaoSession;
import com.liuzhenli.write.greendao.WriteBookDao;
import com.liuzhenli.write.greendao.WriteChapterDao;

import org.greenrobot.greendao.database.Database;

import java.util.Locale;

/**
 * Description:Database helper
 *
 * @author liuzhenli 2021/5/11
 * Email: 848808263@qq.com
 */
public class WriteDbHelper {
    private static WriteDbHelper mDbHelper;
    private final DaoSession mDaoSession;

    private WriteDbHelper() {
        DaoMaster.OpenHelper helper = new DaoOpenHelper(BaseApplication.getInstance(), "write_book_db", null);
        SQLiteDatabase mDb = helper.getWritableDatabase();
        mDb.setLocale(Locale.CHINESE);
        DaoMaster daoMaster = new DaoMaster(mDb);
        mDaoSession = daoMaster.newSession();
    }


    public static WriteDbHelper getInstance() {
        if (mDbHelper == null) {
            synchronized (WriteDbHelper.class) {
                if (mDbHelper == null) {
                    mDbHelper = new WriteDbHelper();
                }
            }
        }
        return mDbHelper;
    }

    public DaoSession getDaoSession() {
        return getInstance().mDaoSession;
    }

    static class DaoOpenHelper extends DaoMaster.OpenHelper {
        public DaoOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            super.onUpgrade(db, oldVersion, newVersion);
            MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
                        @Override
                        public void onCreateAllTables(Database db, boolean ifNotExists) {
                            DaoMaster.createAllTables(db, ifNotExists);
                        }

                        @Override
                        public void onDropAllTables(Database db, boolean ifExists) {
                            DaoMaster.dropAllTables(db, ifExists);
                        }
                    },
                    WriteBookDao.class,
                    WriteChapterDao.class
            );
        }
    }
}

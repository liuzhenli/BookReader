package com.liuzhenli.write.helper;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.liuzhenli.common.BaseApplication;
import com.liuzhenli.write.data.AppWriteDatabase;

import org.jetbrains.annotations.NotNull;

/**
 * Description:Database helper
 *
 * @author liuzhenli 2021/5/11
 * Email: 848808263@qq.com
 */
public class WriteDbHelper {
    private static final String DATABASE_NAME = "write.db";

    private static WriteDbHelper mDbHelper;

    private AppWriteDatabase mDatabase;

    private WriteDbHelper() {
        mDatabase = Room.databaseBuilder(BaseApplication.getInstance(), AppWriteDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .addMigrations()
                .allowMainThreadQueries()//允许在主线程中查询
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull @NotNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                    }

                    @Override
                    public void onOpen(@NonNull @NotNull SupportSQLiteDatabase db) {
                        super.onOpen(db);
                    }
                })
                .build();
    }

    public AppWriteDatabase getAppDatabase() {
        return mDatabase;
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
}

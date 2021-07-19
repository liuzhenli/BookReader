package com.micoredu.reader.helper;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.liuzhenli.common.BaseApplication;
import com.micoredu.reader.dao.AppReaderDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;


/**
 * @author liuzhenli
 */
public class AppReaderDbHelper {

    public static final String DATABASE_NAME = "reader.db";
    private AppReaderDatabase mDatabase;


    public static class LazeLoader {
        private static final AppReaderDbHelper INSTANCE = new AppReaderDbHelper();

    }

    public static AppReaderDbHelper getInstance() {
        return LazeLoader.INSTANCE;
    }

    public AppReaderDatabase getDatabase() {
        return mDatabase;
    }

    private AppReaderDbHelper() {
        mDatabase = Room.databaseBuilder(BaseApplication.getInstance(), AppReaderDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .addMigrations()
                .allowMainThreadQueries()//允许在主线程中查询
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull @NotNull SupportSQLiteDatabase db) {
                        db.setLocale(Locale.CHINESE);
                    }

                    @Override
                    public void onOpen(@NonNull @NotNull SupportSQLiteDatabase db) {
                        super.onOpen(db);
                    }
                })
                .build();
    }


    public SupportSQLiteDatabase getSqliteDatabase() {
        return mDatabase.getOpenHelper().getReadableDatabase();
    }


}

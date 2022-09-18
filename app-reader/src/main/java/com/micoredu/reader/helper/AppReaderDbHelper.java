package com.micoredu.reader.helper;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
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
    private final AppReaderDatabase mDatabase;


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
        //"CREATE TABLE IF NOT EXISTS `readHistory` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `bookCover` TEXT, `type` TEXT, `bookName` TEXT, `authorName` TEXT, `noteUrl` TEXT, `dayMillis` INTEGER NOT NULL, `sumTime` INTEGER NOT NULL)"
        Migration migration1 = new Migration(1, 2) {
            @Override
            public void migrate(@NonNull SupportSQLiteDatabase database) {
                //"CREATE TABLE IF NOT EXISTS `readHistory` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `bookCover` TEXT, `type` TEXT, `bookName` TEXT, `authorName` TEXT, `noteUrl` TEXT, `dayMillis` INTEGER NOT NULL, `sumTime` INTEGER NOT NULL)"
                String sql = "ALTER TABLE readHistory Add id INTEGER PRIMARY KEY AUTOINCREMENT";
                database.execSQL(sql);
            }
        };

        Migration migration2 = new Migration(2, 3) {
            @Override
            public void migrate(@NonNull SupportSQLiteDatabase database) {
                database.execSQL("ALTER TABLE `bookSources` ADD `payAction` TEXT");
                database.execSQL("ALTER TABLE `bookSources` ADD `ruleChapterVip` TEXT");
                database.execSQL("ALTER TABLE `bookSources` ADD `ruleChapterPay` TEXT");
                database.execSQL("ALTER TABLE `bookSources` ADD `loginUi` TEXT");
                database.execSQL("ALTER TABLE `bookSources` ADD`loginCheckJs` TEXT");
                database.execSQL("ALTER TABLE `bookSources` ADD`header` TEXT");

                database.execSQL("ALTER TABLE `bookChapters` ADD `isVip` INTEGER NOT NULL DEFAULT 0");
                database.execSQL("ALTER TABLE `bookChapters` ADD `isPay` INTEGER NOT NULL DEFAULT 0");
            }
        };
        mDatabase = Room.databaseBuilder(BaseApplication.getInstance(), AppReaderDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .addMigrations(migration1, migration2)
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

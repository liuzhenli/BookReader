package com.liuzhenli.reader.ui.presenter;

import android.database.Cursor;

import androidx.sqlite.db.SupportSQLiteDatabase;

import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.common.observer.SampleProgressObserver;
import com.liuzhenli.reader.bean.DatabaseTable;
import com.liuzhenli.reader.ui.contract.DatabaseTableListContract;
import com.liuzhenli.write.helper.WriteDbHelper;
import com.micoredu.reader.helper.AppReaderDbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

/**
 * Description:
 *
 * @author liuzhenli 2021/2/25
 * Email: 848808263@qq.com
 */
public class DatabaseTableListPresenter extends RxPresenter<DatabaseTableListContract.View> implements DatabaseTableListContract.Presenter<DatabaseTableListContract.View> {

    @Inject
    public DatabaseTableListPresenter() {
    }

    @Override
    public void loadDatabase() {
        addSubscribe(RxUtil.subscribe(Observable.create(emitter -> {
            List<DatabaseTable> tables = new ArrayList<>();
            SupportSQLiteDatabase readDb = AppReaderDbHelper.getInstance().getSqliteDatabase();
            String sql = "select name from sqlite_master where type='table' order by name";
            Cursor cursor = readDb.query(sql, null);
            while (cursor.moveToNext()) {
                String tableName = cursor.getString(0);
                tables.add(new DatabaseTable(AppReaderDbHelper.DATABASE_NAME, tableName));
            }

            SupportSQLiteDatabase writeDb = WriteDbHelper.getInstance().getSqliteDatabase();
            sql = "select name from sqlite_master where type='table' order by name";
            cursor = writeDb.query(sql, null);
            while (cursor.moveToNext()) {
                String tableName = cursor.getString(0);
                tables.add(new DatabaseTable(WriteDbHelper.DATABASE_NAME, tableName));
            }

            cursor.close();
            emitter.onNext(tables);
        }), new SampleProgressObserver<List<DatabaseTable>>() {
            @Override
            public void onNext(@NonNull List<DatabaseTable> strings) {
                mView.showDatabase(strings);
            }
        }));
    }
}

package com.liuzhenli.reader.ui.presenter;

import android.database.Cursor;

import androidx.sqlite.db.SupportSQLiteDatabase;

import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.common.observer.SampleProgressObserver;
import com.liuzhenli.reader.ui.contract.DatabaseTableListContract;
import com.micoredu.reader.helper.AppReaderDbHelper;

import java.util.ArrayList;
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
            List<String> tables = new ArrayList<>();
            SupportSQLiteDatabase database = AppReaderDbHelper.getInstance().getSqliteDatabase();
            String sql = "select name from sqlite_master where type='table' order by name";
            Cursor cursor = database.query(sql, null);
            while (cursor.moveToNext()) {
                String tableName = cursor.getString(0);
                tables.add(tableName);
            }
            cursor.close();
            emitter.onNext(tables);
        }), new SampleProgressObserver<List<String>>() {
            @Override
            public void onNext(@NonNull List<String> strings) {
                mView.showDatabase(strings);
            }
        }));
    }
}

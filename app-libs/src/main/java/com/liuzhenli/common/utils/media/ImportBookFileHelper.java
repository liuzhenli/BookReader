package com.liuzhenli.common.utils.media;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2020/8/5
 * Email: 848808263@qq.com
 */
public class ImportBookFileHelper {

    public static void getBookFile(FragmentActivity activity, LoadBookCallBack callBack) {
        LoaderManager loaderManager = activity.getSupportLoaderManager();
        loaderManager.initLoader(0, null, callBack);
    }

    public interface ImportBookCallback {
        /**
         * 加载完成
         *
         * @param bookList 书列表
         */
        void onBookLoaded(List<File> bookList);
    }

    public static class LoadBookCallBack implements LoaderManager.LoaderCallbacks<Cursor> {
        private WeakReference<Context> mContext;
        private ImportBookCallback mCallback;

        public LoadBookCallBack(Context context, ImportBookCallback callback) {
            mContext = new WeakReference<>(context);
            mCallback = callback;
        }

        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            return new LocalFileLoader(mContext.get());
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            LocalFileLoader localFileLoader = (LocalFileLoader) loader;
            localFileLoader.parse(data, mCallback);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        }
    }
}

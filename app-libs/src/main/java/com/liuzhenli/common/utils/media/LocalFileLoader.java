package com.liuzhenli.common.utils.media;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.loader.content.CursorLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2020/8/5
 * Email: 848808263@qq.com
 */
public class LocalFileLoader extends CursorLoader {
    private static final String TAG = "LocalFileLoader";

    private static final Uri FILE_URI = Uri.parse("content://media/external/file");
    private static final String SELECTION = MediaStore.Files.FileColumns.DATA + " like ? or " + MediaStore.Files.FileColumns.DATA + " like ?";
    private static final String[] SEARCH_TYPE = new String[]{"%.txt", "%.epub"};
    private static final String SORT_ORDER = MediaStore.Files.FileColumns.DISPLAY_NAME + " ASC";
    private static final String[] FILE_PROJECTION = {
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DISPLAY_NAME
    };


    public LocalFileLoader(@NonNull Context context) {
        super(context);
        initLoader();
    }

    private void initLoader() {
        setUri(FILE_URI);
        setProjection(FILE_PROJECTION);
        setSelection(SELECTION);
        setSelectionArgs(SEARCH_TYPE);
        setSortOrder(SORT_ORDER);
    }

    public void parse(Cursor cursor, ImportBookFileHelper.ImportBookCallback callback) {
        List<File> files = new ArrayList<>();
        //cursor为空直接返回
        if (cursor == null) {
            callback.onBookLoaded(files);
        } else {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
                //判断有效路径
                if (!TextUtils.isEmpty(path)) {
                    File file = new File(path);
                    if (!file.isDirectory() && file.length() > 1024 * 100) {
                        files.add(file);
                    }
                }
            }
            if (callback != null) {
                callback.onBookLoaded(files);
            }
        }
    }
}

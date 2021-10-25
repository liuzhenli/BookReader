package com.liuzhenli.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liuzhenli.common.ui.FilePickerActivity;

/**
 * Description:
 *
 * @author liuzhenli 2021/10/25
 * Email: 848808263@qq.com
 */
public class FilePickerContract extends ActivityResultContract<FilePickerContract.FilePickerParam, Uri> {
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, FilePickerParam input) {
        Intent intent = new Intent(context, FilePickerActivity.class);
        intent.putExtra("mode", input.mode);
        intent.putExtra("title", input.title);
        intent.putExtra("allowExtensions", input.allowExtensions);
        intent.putExtra("otherActions", input.otherActions);
        return intent;
    }

    @Override
    public Uri parseResult(int resultCode, @Nullable Intent intent) {
        if (resultCode == Activity.RESULT_OK && intent != null) {
            return intent.getData();
        }
        return null;
    }

    public static class FilePickerParam {
        int mode = 0;
        String title;
        String[] allowExtensions = {};
        String[] otherActions;
    }
}

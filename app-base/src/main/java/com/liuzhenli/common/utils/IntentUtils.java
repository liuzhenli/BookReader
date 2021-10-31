package com.liuzhenli.common.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * Description:
 *
 * @author liuzhenli 2020/12/31
 * Email: 848808263@qq.com
 */
public class IntentUtils {
    public static final int REQUEST_CODE_OPEN_MOBILE_DIR = 1000;
    public static final int IMPORT_BOOK_SOURCE_LOCAL = 1001;
    public static final int IMPORT_BOOK_SOURCE_QRCODE = 1002;

    public static void openBrowser(Context context, String url) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
        // 官方解释 : Name of the component implementing an activity that can display the intent
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            final ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            L.d("111111 = " + componentName.getClassName());
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
        } else {
            ToastUtil.showToast(context, "链接错误或无浏览器");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void openMobileDir(Activity context, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivityForResult(intent, requestCode);
    }

    public static void selectFileSys(Activity context, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"text/*", "application/json"});
        intent.setType("*/*");//设置类型
        context.startActivityForResult(intent, requestCode);
    }
}

package com.liuzhenli.reader.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.liuzhenli.common.utils.LogUtils;

/**
 * Description:
 *
 * @author liuzhenli 2020/12/31
 * Email: 848808263@qq.com
 */
public class IntentUtils {
    public static void openBrowser(Context context, String url) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
        // 官方解释 : Name of the component implementing an activity that can display the intent
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            final ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            LogUtils.d("111111 = " + componentName.getClassName());
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
        } else {
            ToastUtil.showToast(context, "链接错误或无浏览器");
        }
    }
}

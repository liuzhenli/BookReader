package com.liuzhenli.common.utils;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.fragment.app.FragmentActivity;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;

/**
 * 权限申请工具
 *
 * @author Liuzhenli
 * @since 2019-07-07 18:28
 */
public class PermissionUtil {
    public static void requestPermission(Context context, PermissionObserver observer, final String... permissions) {
        XXPermissions.with(context).permission(permissions).request(observer);
    }

    /**
     * 跳转到手机权限设置页面
     */
    public static void jumpPermissionPage(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract static class PermissionObserver implements OnPermissionCallback {
    }
}


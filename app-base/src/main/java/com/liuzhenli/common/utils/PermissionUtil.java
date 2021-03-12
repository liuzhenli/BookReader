package com.liuzhenli.common.utils;



import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.fragment.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observer;

/**
 * 权限申请工具
 * @author Liuzhenli
 * @since 2019-07-07 18:28
 */
public class PermissionUtil{
    public  static  void requestPermission(FragmentActivity context, Observer observer, final String...permissions){
        RxPermissions rxPermissions = new RxPermissions(context);
        rxPermissions.request(permissions)
                .subscribe(observer);
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
}


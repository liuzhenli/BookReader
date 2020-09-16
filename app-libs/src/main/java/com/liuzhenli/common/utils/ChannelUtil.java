package com.liuzhenli.common.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.meituan.android.walle.WalleChannelReader;

/**
 * app 渠道信息
 */
public class ChannelUtil {

    static private String sChannelCode = "";
    static private String sChannelName;

    private static void readChannel(Context context) {
        try {
            sChannelName = WalleChannelReader.getChannel(context.getApplicationContext());
            sChannelCode = WalleChannelReader.get(context, "CHNL");
        } catch (Exception e) {
            // 如果读不到这个数，那么就当是无渠道
            e.printStackTrace();
        }
        if (sChannelName == null) {
            ApplicationInfo appInfo = null;
            try {
                appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                String umengChannel = appInfo.metaData.getString("UMENG_CHANNEL");
                String umengChnl = appInfo.metaData.getInt("CHNL") + "";
                if (StringUtils.isEmpty(umengChannel)) {
                    sChannelName = "none";
                } else {
                    sChannelName = umengChannel;
                }
                if (sChannelCode == null) {
                    if (StringUtils.isEmpty(umengChnl)) {
                        sChannelCode = "00";
                    } else {
                        sChannelCode = umengChnl;
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                sChannelCode = "00";
                sChannelName = "none";
            }
        }
    }

    public static String getChannelName(Context context) {
        if (sChannelName == null) {
            readChannel(context);
        }
        return sChannelName;
    }

    public static String getChannelCode(Context context) {
        if (TextUtils.isEmpty(sChannelCode)) {
            readChannel(context);
        }
        return sChannelCode;
    }
}

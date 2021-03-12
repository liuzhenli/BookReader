package com.liuzhenli.common.utils;

import android.content.ComponentName;
import android.content.pm.PackageManager;

import com.liuzhenli.common.BaseApplication;
import com.liuzhenli.common.R;

/**
 * 更换图标
 */

public class LauncherIcon {
    private static PackageManager packageManager = BaseApplication.getInstance().getPackageManager();
    private static ComponentName componentNameMain = new ComponentName(BaseApplication.getInstance(), "com.liuzhenli.reader.ui.activity.SplashActivity");
    private static ComponentName componentNameBookMain = new ComponentName(BaseApplication.getInstance(), "com.liuzhenli.reader.ui.activity.SplashActivity");

    public static void ChangeIcon(String icon) {
        if (true) {
            return;
        }

        if (icon.equals(BaseApplication.getInstance().getString(R.string.icon_book))) {
            if (packageManager.getComponentEnabledSetting(componentNameBookMain) != PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                //启用
                packageManager.setComponentEnabledSetting(componentNameBookMain,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                //禁用
                packageManager.setComponentEnabledSetting(componentNameMain,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            }
        } else {
            if (packageManager.getComponentEnabledSetting(componentNameMain) != PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                //启用
                packageManager.setComponentEnabledSetting(componentNameMain,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                //禁用
                packageManager.setComponentEnabledSetting(componentNameBookMain,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            }
        }
    }

    public static String getInUseIcon() {
        if (packageManager.getComponentEnabledSetting(componentNameBookMain) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            return BaseApplication.getInstance().getString(R.string.icon_book);
        }
        return BaseApplication.getInstance().getString(R.string.icon_main);
    }
}

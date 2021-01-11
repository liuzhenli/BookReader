package com.liuzhenli.reader.utils;

import android.text.TextUtils;

import com.liuzhenli.reader.bean.AppConfig;
import com.liuzhenli.reader.gson.GsonUtils;
import com.umeng.cconfig.UMRemoteConfig;
import com.umeng.cconfig.UMRemoteConfig;

/**
 * Description:
 *
 * @author liuzhenli 2021/1/10
 * Email: 848808263@qq.com
 */
public class AppConfigManager {
    public static AppConfigManager instence = new AppConfigManager();

    private AppConfigManager() {
    }

    public static AppConfigManager getInstance() {
        return instence;
    }

    public AppConfig getAppConfig() {
        String str = UMRemoteConfig.getInstance().getConfigValue("appConfig");
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return GsonUtils.toBean(str, AppConfig.class);
    }

    public int getNewVersion() {
        if (getAppConfig() != null) {
            if (getAppConfig().data != null && getAppConfig().data.versionInfo != null) {
                return getAppConfig().data.versionInfo.code;
            }
        }
        return 0;
    }

    public String getNewVersionIntro() {
        if (getAppConfig() != null) {
            if (getAppConfig().data != null && getAppConfig().data.versionInfo != null) {
                return getAppConfig().data.versionInfo.intro;
            }
        }
        return "";
    }
}

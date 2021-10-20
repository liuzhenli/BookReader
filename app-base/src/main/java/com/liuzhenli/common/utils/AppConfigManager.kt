package com.liuzhenli.common.utils

import android.text.TextUtils
import com.liuzhenli.common.BaseApplication
import com.liuzhenli.common.bean.AppConfig
import com.liuzhenli.common.gson.GsonUtils
import com.umeng.cconfig.UMRemoteConfig

/**
 * Description:
 *
 * @author liuzhenli 2021/10/15
 * Email: 848808263@qq.com
 */
open class AppConfigManager {
    companion object {
        @JvmStatic
        var instance = AppConfigManager()
    }
    private fun getAppConfig(): AppConfig? {
        val str = UMRemoteConfig.getInstance().getConfigValue("appConfig")
        L.e(str)
        return if (TextUtils.isEmpty(str)) {
            null
        } else GsonUtils.toBean(str, AppConfig::class.java)
    }

    /**
     * newest versioncode
     */
    fun getNewVersion(): Int? {
        return getAppConfig()?.data?.versionInfo?.code
    }

    /**
     * newest version introduction
     */
    fun getNewVersionIntro(): String? {
        return getAppConfig()?.data?.versionInfo?.intro
    }

     fun getDefaultBookSourceUrl(): String? {
        return getAppConfig()?.data?.sourceUrl
    }

    /**
     * switcher if current channel show donate
     */
    fun isShowDonate(): Boolean {
        if (getAppConfig() != null) {
            val split = getAppConfig()?.data?.pay?.filterChannel?.split(",")?.toTypedArray()
            if (split != null) {
                for (s in split) {
                    if (s == ChannelUtil.getChannelName(BaseApplication.getInstance())) {
                        return false
                    }
                }
            } else {
                return true
            }
        }
        return true
    }
}
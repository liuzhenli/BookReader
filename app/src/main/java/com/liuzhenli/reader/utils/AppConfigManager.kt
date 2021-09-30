package com.liuzhenli.reader.utils

import android.text.TextUtils
import com.liuzhenli.reader.bean.AppConfig
import com.umeng.cconfig.UMRemoteConfig
import com.liuzhenli.common.gson.GsonUtils
import com.liuzhenli.common.utils.ChannelUtil
import com.liuzhenli.common.BaseApplication
import com.liuzhenli.common.utils.IOUtils
import com.liuzhenli.reader.bean.Sayings
import java.io.IOException
import java.util.*

/**
 * Description:
 *
 * @author liuzhenli 2021/1/10
 * Email: 848808263@qq.com
 */
class AppConfigManager private constructor() {

    private fun getAppConfig(): AppConfig? {
        val str = UMRemoteConfig.getInstance().getConfigValue("appConfig")
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

    /**
     * get a saying random from sayings list
     */
    fun getSayings(): Sayings {
        try {
            val open = BaseApplication.getInstance().assets.open("sayings.json")
            val json = IOUtils.toString(open)
            open.close()
            val sayings = GsonUtils.parseJArray(json, Sayings::class.java)
            val random = Random()
            val i = random.nextInt(sayings.size)
            return sayings[i]
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Sayings("发奋识遍天下字，立志读尽人间书", "苏轼")
    }

    companion object {
        @JvmStatic
        var instance = AppConfigManager()
    }
}
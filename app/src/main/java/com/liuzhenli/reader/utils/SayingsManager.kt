package com.liuzhenli.reader.utils

import com.liuzhenli.common.gson.GsonUtils
import com.liuzhenli.common.BaseApplication
import com.liuzhenli.common.utils.IOUtils
import com.liuzhenli.reader.bean.Sayings
import splitties.init.appCtx
import java.io.IOException
import java.util.*

/**
 * Description:
 *
 * @author liuzhenli 2021/1/10
 * Email: 848808263@qq.com
 */
class SayingsManager private constructor()  {

    /**
     * get a saying random from sayings list
     */
    fun getSayings(): Sayings {
        try {
            val open = appCtx.assets.open("sayings.json")
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
        var instance = SayingsManager()
    }


}
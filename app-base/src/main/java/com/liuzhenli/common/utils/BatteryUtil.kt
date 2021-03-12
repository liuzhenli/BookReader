package com.liuzhenli.common.utils

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

/**
 * Description:
 *
 * @author liuzhenli 2020/7/26
 * Email: 848808263@qq.com
 */
object BatteryUtil {
    fun getBatteryLevel(context: Context): Int {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = context.registerReceiver(null, intentFilter)
        return batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
    }
}
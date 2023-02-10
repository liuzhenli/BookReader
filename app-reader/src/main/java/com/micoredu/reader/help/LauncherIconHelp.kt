package com.micoredu.reader.help

import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Build
import com.liuzhenli.common.utils.ToastUtil
import com.microedu.lib.reader.R
import splitties.init.appCtx

/**
 * Created by GKF on 2018/2/27.
 * 更换图标
 */
object LauncherIconHelp {
//    private val packageManager: PackageManager = appCtx.packageManager
//    private val componentNames = arrayListOf(
//        ComponentName(appCtx, Launcher1::class.java.name),
//    )

    fun changeIcon(icon: String?) {
//        if (icon.isNullOrEmpty()) return
//        if (Build.VERSION.SDK_INT < 26) {
//            ToastUtil.showToast(R.string.change_icon_error)
//            return
//        }
//        var hasEnabled = false
//        componentNames.forEach {
//            if (icon.equals(it.className.substringAfterLast("."), true)) {
//                hasEnabled = true
//                //启用
//                packageManager.setComponentEnabledSetting(
//                    it,
//                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                    PackageManager.DONT_KILL_APP
//                )
//            } else {
//                //禁用
//                packageManager.setComponentEnabledSetting(
//                    it,
//                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                    PackageManager.DONT_KILL_APP
//                )
//            }
//        }
//        if (hasEnabled) {
//            packageManager.setComponentEnabledSetting(
//                ComponentName(appCtx, WelcomeActivity::class.java.name),
//                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                PackageManager.DONT_KILL_APP
//            )
//        } else {
//            packageManager.setComponentEnabledSetting(
//                ComponentName(appCtx, WelcomeActivity::class.java.name),
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                PackageManager.DONT_KILL_APP
//            )
//        }
    }
}
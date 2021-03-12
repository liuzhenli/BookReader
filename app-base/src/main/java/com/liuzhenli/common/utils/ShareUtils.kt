package com.liuzhenli.common.utils

import android.content.Context
import android.content.Intent

/**
 * Description:
 *
 * @author  liuzhenli 2021/1/4
 * Email: 848808263@qq.com
 */


object ShareUtils {
    fun share(context: Context, text: String) {
        val intent = Intent("android.intent.action.SEND")
        intent.type = "text/plain"
        intent.putExtra("android.intent.extra.SUBJECT", "Spread word")
        intent.putExtra("android.intent.extra.TEXT", text)
        //对话框标题
        context.startActivity(Intent.createChooser(intent, "分享至"))
    }
}
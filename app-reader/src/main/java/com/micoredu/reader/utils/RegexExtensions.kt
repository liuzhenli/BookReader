package com.micoredu.reader.utils

import androidx.core.os.postDelayed
import com.liuzhenli.common.exception.RegexTimeoutException
import com.liuzhenli.common.utils.AppConst
import com.liuzhenli.common.utils.ToastUtil
import com.liuzhenli.common.utils.buildMainHandler
import com.liuzhenli.common.utils.restart
import com.micoredu.reader.help.CrashHandler
import com.micoredu.reader.help.coroutine.Coroutine
import com.script.SimpleBindings
import kotlinx.coroutines.suspendCancellableCoroutine
import splitties.init.appCtx
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

private val handler by lazy { buildMainHandler() }

/**
 * 带有超时检测的正则替换
 */
suspend fun CharSequence.replace(regex: Regex, replacement: String, timeout: Long): String {
    val charSequence = this@replace
    val isJs = replacement.startsWith("@js:")
    val replacement1 = if (isJs) replacement.substring(4) else replacement
    return suspendCancellableCoroutine { block ->
        val coroutine = Coroutine.async {
            try {
                val pattern = regex.toPattern()
                val matcher = pattern.matcher(charSequence)
                val stringBuffer = StringBuffer()
                while (matcher.find()) {
                    if (isJs) {
                        val bindings = SimpleBindings()
                        bindings["result"] = matcher.group()
                        val jsResult =
                            AppConst.SCRIPT_ENGINE.eval(replacement1, bindings).toString()
                        matcher.appendReplacement(stringBuffer, jsResult)
                    } else {
                        matcher.appendReplacement(stringBuffer, replacement1)
                    }
                }
                matcher.appendTail(stringBuffer)
                block.resume(stringBuffer.toString())
            } catch (e: Exception) {
                block.resumeWithException(e)
            }
        }
        handler.postDelayed(timeout) {
            if (coroutine.isActive) {
                val timeoutMsg = "替换超时,3秒后还未结束将重启应用\n替换规则$regex\n替换内容:${this}"
                val exception = RegexTimeoutException(timeoutMsg)
                block.cancel(exception)
                ToastUtil.showToast(timeoutMsg)
                CrashHandler.saveCrashInfo2File(exception)
                handler.postDelayed(3000) {
                    if (coroutine.isActive) {
                        appCtx.restart()
                    }
                }
            }
        }
    }
}

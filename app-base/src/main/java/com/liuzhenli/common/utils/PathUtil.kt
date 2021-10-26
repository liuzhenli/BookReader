package com.liuzhenli.common.utils

import android.net.Uri
import com.liuzhenli.common.utils.L

/**
 * Description:
 *
 * @author liuzhenli 2021/10/26
 * Email: 848808263@qq.com
 */
class PathUtil {
    companion object {
        fun getPathShow(path: String): String {
            L.e(path)
            return when {
                path.startsWith(Constant.DATA_PATH) -> {
                    path.substring(Constant.DATA_PATH.length, path.length)
                }
                path.startsWith(Constant.UPDATE_PATH) -> {
                    path.substring(Constant.DATA_USER.length, path.length)
                }
                FileUtils.isContentFile(path) -> {
                    val decodePath = Uri.decode(path)
                    decodePath.substring(Constant.DOC_ROOT.length, decodePath.length)
                }
                else -> {
                    path
                }
            }
        }
    }
}
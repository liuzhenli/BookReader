package com.liuzhenli.common.utils

import android.net.Uri
import android.text.TextUtils
import com.liuzhenli.common.utils.L
import kotlin.contracts.contract

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
                    if (decodePath.startsWith(Constant.DOC_ROOT_2)) {
                        var path =
                            decodePath.substring(Constant.DOC_ROOT_2.length, decodePath.length)
                        if (TextUtils.isEmpty(path)) {
                            path = "Documents/ShuFang"
                        }
                        path
                    } else if (decodePath.startsWith(Constant.DOC_ROOT)) {
                        decodePath.substring(Constant.DOC_ROOT.length, decodePath.length)
                    } else {
                        val split = decodePath.split(":")
                        val size = split.size
                        if (size > 1) {
                            split[size - 1]
                        } else {
                            decodePath
                        }
                    }
                }
                else -> {
                    path
                }
            }
        }
    }
}
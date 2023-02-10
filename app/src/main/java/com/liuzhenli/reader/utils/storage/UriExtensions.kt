package com.liuzhenli.reader.utils.storage

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import com.liuzhenli.common.utils.DocumentUtil
import com.liuzhenli.common.utils.PermissionUtil
import com.liuzhenli.common.utils.RealPathUtil
import com.liuzhenli.common.utils.ToastUtil
import java.io.File

fun Uri.isContentScheme() = this.scheme == "content"

/**
 * 读取URI
 */
fun Uri.read(activity: AppCompatActivity, success: (name: String, bytes: ByteArray) -> Unit) {
    try {
        if (isContentScheme()) {
            val doc = DocumentFile.fromSingleUri(activity, this)
            doc ?: error("未获取到文件")
            val name = doc.name ?: error("未获取到文件名")
            val fileBytes = DocumentUtil.readBytes(activity, doc.uri)
            fileBytes ?: error("读取文件出错")
            success.invoke(name, fileBytes)
        } else {
            PermissionUtil.requestPermission(
                activity,
                object : PermissionUtil.PermissionObserver() {
                    override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                        RealPathUtil.getPath(activity, this@read)?.let { path ->
                            val imgFile = File(path)
                            success.invoke(imgFile.name, imgFile.readBytes())
                        }
                    }
                },
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    } catch (e: Exception) {
        e.printStackTrace()
        ToastUtil.showToast(e.localizedMessage ?: "read uri error")
    }
}

/**
 * 读取URI
 */
fun Uri.read(fragment: Fragment, success: (name: String, bytes: ByteArray) -> Unit) {
    try {
        if (isContentScheme()) {
            val doc = DocumentFile.fromSingleUri(fragment.requireContext(), this)
            doc ?: error("未获取到文件")
            val name = doc.name ?: error("未获取到文件名")
            val fileBytes = DocumentUtil.readBytes(fragment.requireContext(), doc.uri)
            fileBytes ?: error("读取文件出错")
            success.invoke(name, fileBytes)
        } else {
            PermissionUtil.requestPermission(
                fragment.context,
                object : PermissionUtil.PermissionObserver() {
                    override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                        fragment.context?.let {
                            RealPathUtil.getPath(fragment.requireContext(), this@read)
                                ?.let { path ->
                                    val imgFile = File(path)
                                    success.invoke(imgFile.name, imgFile.readBytes())
                                }
                        }
                    }
                },
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    } catch (e: Exception) {
        e.printStackTrace()
        ToastUtil.showToast(e.localizedMessage ?: "read uri error")
    }
}

@Throws(Exception::class)
fun Uri.readBytes(context: Context): ByteArray? {
    if (this.isContentScheme()) {
        return DocumentUtil.readBytes(context, this)
    } else {
        val path = RealPathUtil.getPath(context, this)
        if (path?.isNotEmpty() == true) {
            return File(path).readBytes()
        }
    }
    return null
}

@Throws(Exception::class)
fun Uri.readText(context: Context): String? {
    readBytes(context)?.let {
        return String(it)
    }
    return null
}

@Throws(Exception::class)
fun Uri.writeBytes(
    context: Context,
    byteArray: ByteArray
): Boolean {
    if (this.isContentScheme()) {
        return DocumentUtil.writeBytes(context, byteArray, this)
    } else {
        val path = RealPathUtil.getPath(context, this)
        if (path?.isNotEmpty() == true) {
            File(path).writeBytes(byteArray)
            return true
        }
    }
    return false
}

@Throws(Exception::class)
fun Uri.writeText(context: Context, text: String): Boolean {
    return writeBytes(context, text.toByteArray())
}

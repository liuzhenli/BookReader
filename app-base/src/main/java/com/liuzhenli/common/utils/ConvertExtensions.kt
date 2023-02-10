@file:Suppress("unused")

package com.liuzhenli.common.utils

import android.R
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import java.io.*
import java.text.DecimalFormat
import java.util.*
import kotlin.math.log10
import kotlin.math.pow

/**
 * 数据类型转换、单位转换
 *
 * @author 李玉江[QQ:1023694760]
 * @since 2014-4-18
 */
@Suppress("MemberVisibilityCanBePrivate")
object ConvertUtils {
    const val GB: Long = 1073741824
    const val MB: Long = 1048576
    const val KB: Long = 1024

    fun toInt(obj: Any): Int {
        return kotlin.runCatching {
            Integer.parseInt(obj.toString())
        }.getOrDefault(-1)
    }

    fun toInt(bytes: ByteArray): Int {
        var result = 0
        var byte: Byte
        for (i in bytes.indices) {
            byte = bytes[i]
            result += (byte.toInt() and 0xFF).shl(8 * i)
        }
        return result
    }

    fun toFloat(obj: Any): Float {
        return kotlin.runCatching {
            java.lang.Float.parseFloat(obj.toString())
        }.getOrDefault(-1f)
    }


    @JvmOverloads
    fun toBitmap(bytes: ByteArray, width: Int = -1, height: Int = -1): Bitmap? {
        var bitmap: Bitmap? = null
        if (bytes.isNotEmpty()) {
            kotlin.runCatching {
                val options = BitmapFactory.Options()
                // 设置让解码器以最佳方式解码
                options.inPreferredConfig = null
                if (width > 0 && height > 0) {
                    options.outWidth = width
                    options.outHeight = height
                }
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
                bitmap!!.density = 96// 96 dpi
            }
        }
        return bitmap
    }


    fun toFileSizeString(fileSize: Long): String {
        val df = DecimalFormat("0.00")
        val fileSizeString: String = if (fileSize < KB) {
            fileSize.toString() + "B"
        } else if (fileSize < MB) {
            df.format(fileSize.toDouble() / KB) + "K"
        } else if (fileSize < GB) {
            df.format(fileSize.toDouble() / MB) + "M"
        } else {
            df.format(fileSize.toDouble() / GB) + "G"
        }
        return fileSizeString
    }

    private fun toDrawable(bitmap: Bitmap?): Drawable? {
        return if (bitmap == null) null else BitmapDrawable(Resources.getSystem(), bitmap)
    }

    fun toDrawable(bytes: ByteArray): Drawable? {
        return toDrawable(toBitmap(bytes))
    }

    fun formatFileSize(length: Long): String {
        if (length <= 0) return "0"
        val units = arrayOf("b", "kb", "M", "G", "T")
        //计算单位的，原理是利用lg,公式是 lg(1024^n) = nlg(1024)，最后 nlg(1024)/lg(1024) = n。
        val digitGroups = (log10(length.toDouble()) / log10(1024.0)).toInt()
        //计算原理是，size/单位值。单位值指的是:比如说b = 1024,KB = 1024^2
        return DecimalFormat("#,##0.##").format(length / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
    }

    @JvmOverloads
    fun toString(`is`: InputStream, charset: String = "utf-8"): String {
        val sb = StringBuilder()
        kotlin.runCatching {
            val reader = BufferedReader(InputStreamReader(`is`, charset))
            while (true) {
                val line = reader.readLine()
                if (line == null) {
                    break
                } else {
                    sb.append(line).append("\n")
                }
            }
            reader.close()
            `is`.close()
        }
        return sb.toString()
    }

    fun toString(objects: Array<Any?>?): String? {
        return Arrays.deepToString(objects)
    }

    fun toString(objects: Array<Any?>, tag: String?): String? {
        val sb = java.lang.StringBuilder()
        for (`object` in objects) {
            sb.append(`object`)
            sb.append(tag)
        }
        return sb.toString()
    }

    fun toByteArray(`is`: InputStream?): ByteArray? {
        if (`is` == null) {
            return null
        }
        try {
            val os = ByteArrayOutputStream()
            val buff = ByteArray(100)
            while (true) {
                val len = `is`.read(buff, 0, 100)
                if (len == -1) {
                    break
                } else {
                    os.write(buff, 0, len)
                }
            }
            val bytes = os.toByteArray()
            os.close()
            `is`.close()
            return bytes
        } catch (e: IOException) {
        }
        return null
    }

    fun toByteArray(bitmap: Bitmap?): ByteArray? {
        if (bitmap == null) {
            return null
        }
        val os = ByteArrayOutputStream()
        // 将Bitmap压缩成PNG编码，质量为100%存储，除了PNG还有很多常见格式，如jpeg等。
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
        val bytes = os.toByteArray()
        try {
            os.close()
        } catch (e: IOException) {
        }
        return bytes
    }

    fun toDarkenColor(@ColorInt color: Int): Int {
        return toDarkenColor(color, 0.8f)
    }

    fun toDarkenColor(@ColorInt color: Int, @FloatRange(from = 0.0, to = 1.0) value: Float): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] *= value //HSV指Hue、Saturation、Value，即色调、饱和度和亮度，此处表示修改亮度
        return Color.HSVToColor(hsv)
    }

    /**
     * 转换为6位十六进制颜色代码，不含“#”
     */
    fun toColorString(@ColorInt color: Int): String? {
        return toColorString(color, false)
    }

    /**
     * 转换为6位十六进制颜色代码，不含“#”
     */
    fun toColorString(@ColorInt color: Int, includeAlpha: Boolean): String? {
        var alpha = Integer.toHexString(Color.alpha(color))
        var red = Integer.toHexString(Color.red(color))
        var green = Integer.toHexString(Color.green(color))
        var blue = Integer.toHexString(Color.blue(color))
        if (alpha.length == 1) {
            alpha = "0$alpha"
        }
        if (red.length == 1) {
            red = "0$red"
        }
        if (green.length == 1) {
            green = "0$green"
        }
        if (blue.length == 1) {
            blue = "0$blue"
        }
        val colorString: String
        colorString = if (includeAlpha) {
            alpha + red + green + blue
        } else {
            red + green + blue
        }
        return colorString
    }

    /**
     * 对TextView、Button等设置不同状态时其文字颜色。
     * 参见：http://blog.csdn.net/sodino/article/details/6797821
     * Modified by liyujiang at 2015.08.13
     */
    fun toColorStateList(
        @ColorInt normalColor: Int, @ColorInt pressedColor: Int,
        @ColorInt focusedColor: Int, @ColorInt unableColor: Int
    ): ColorStateList? {
        val colors =
            intArrayOf(
                pressedColor,
                focusedColor,
                normalColor,
                focusedColor,
                unableColor,
                normalColor
            )
        val states = arrayOfNulls<IntArray>(6)
        states[0] = intArrayOf(R.attr.state_pressed, R.attr.state_enabled)
        states[1] = intArrayOf(R.attr.state_enabled, R.attr.state_focused)
        states[2] = intArrayOf(R.attr.state_enabled)
        states[3] = intArrayOf(R.attr.state_focused)
        states[4] = intArrayOf(R.attr.state_window_focused)
        states[5] = intArrayOf()
        return ColorStateList(states, colors)
    }

    fun toColorStateList(@ColorInt normalColor: Int, @ColorInt pressedColor: Int): ColorStateList? {
        return toColorStateList(normalColor, pressedColor, pressedColor, normalColor)
    }
}

val Int.hexString: String
    get() = Integer.toHexString(this)

fun Int.dpToPx(): Int = this.toFloat().dpToPx().toInt()

fun Int.spToPx(): Int = this.toFloat().spToPx().toInt()

fun Float.dpToPx(): Float = android.util.TypedValue.applyDimension(
    android.util.TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics
)

fun Float.spToPx(): Float = android.util.TypedValue.applyDimension(
    android.util.TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics
)


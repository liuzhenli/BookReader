package com.liuzhenli.common.utils

import android.annotation.SuppressLint
import com.liuzhenli.common.BaseApplication
import android.os.Build
import android.text.TextUtils
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import splitties.init.appCtx
import java.lang.Exception
import java.lang.StringBuilder
import java.nio.charset.StandardCharsets
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

object StringUtils {
    private const val TAG = "StringUtils"
    private const val HOUR_OF_DAY = 24
    private const val DAY_OF_YESTERDAY = 2
    private const val TIME_UNIT = 60
    private val ChnMap = getChnMap()

    //将时间转换成日期
    fun dateConvert(time: Long, pattern: String?): String {
        val date = Date(time)
        @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat(pattern)
        return format.format(date)
    }

    /**
     * 将日期转换成昨天、今天、明天
     */
    fun dateConvert(source: String?, pattern: String?): String {
        @SuppressLint("SimpleDateFormat") val format: DateFormat = SimpleDateFormat(pattern)
        val calendar = Calendar.getInstance()
        try {
            val date = format.parse(source)
            val curTime = calendar.timeInMillis
            calendar.time = date
            //将MISC 转换成 sec
            val difSec = Math.abs((curTime - date.time) / 1000)
            val difMin = difSec / 60
            val difHour = difMin / 60
            val difDate = difHour / 60
            val oldHour = calendar[Calendar.HOUR]
            //如果没有时间
            if (oldHour == 0) {
                //比日期:昨天今天和明天
                return if (difDate == 0L) {
                    "今天"
                } else if (difDate < DAY_OF_YESTERDAY) {
                    "昨天"
                } else {
                    @SuppressLint("SimpleDateFormat") val convertFormat: DateFormat =
                        SimpleDateFormat("yyyy-MM-dd")
                    convertFormat.format(date)
                }
            }
            return if (difSec < TIME_UNIT) {
                difSec.toString() + "秒前"
            } else if (difMin < TIME_UNIT) {
                difMin.toString() + "分钟前"
            } else if (difHour < HOUR_OF_DAY) {
                difHour.toString() + "小时前"
            } else if (difDate < DAY_OF_YESTERDAY) {
                "昨天"
            } else {
                @SuppressLint("SimpleDateFormat") val convertFormat: DateFormat =
                    SimpleDateFormat("yyyy-MM-dd")
                convertFormat.format(date)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }

    fun toFirstCapital(str: String): String {
        return str.substring(0, 1).uppercase(Locale.getDefault()) + str.substring(1)
    }

    fun getString(@StringRes id: Int): String {
        return appCtx.resources.getString(id)
    }

    fun getString(@StringRes id: Int, vararg formatArgs: Any?): String {
        return appCtx.getString(id, *formatArgs)
    }

    /**
     * 将文本中的半角字符，转换成全角字符
     */
    fun halfToFull(input: String): String {
        val c = input.toCharArray()
        for (i in c.indices) {
            if (c[i].code == 32)
            //半角空格
            {
                c[i] = 12288.toChar()
                continue
            }
            //根据实际情况，过滤不需要转换的符号
            //if (c[i] == 46) //半角点号，不转换
            // continue;

            if (c[i].code in 33..126)
            //其他符号都转换为全角
                c[i] = (c[i].code + 65248).toChar()
        }
        return String(c)
    }

    //功能：字符串全角转换为半角
    fun fullToHalf(input: String): String {
        val c = input.toCharArray()
        for (i in c.indices) {
            //全角空格
            if (c[i].code == 12288) {
                c[i] = 32.toChar()
                continue
            }
            if (c[i].code in 65281..65374) {
                c[i] = c[i] - 65248
            }
        }
        return String(c)
    }

    private fun getChnMap(): HashMap<Char, Int> {
        val map = HashMap<Char, Int>()
        var cnStr = "零一二三四五六七八九十"
        var c = cnStr.toCharArray()
        for (i in 0..10) {
            map[c[i]] = i
        }
        cnStr = "〇壹贰叁肆伍陆柒捌玖拾"
        c = cnStr.toCharArray()
        for (i in 0..10) {
            map[c[i]] = i
        }
        map['两'] = 2
        map['百'] = 100
        map['佰'] = 100
        map['千'] = 1000
        map['仟'] = 1000
        map['万'] = 10000
        map['亿'] = 100000000
        return map
    }

    fun chineseNumToInt(chNum: String): Int {
        var result = 0
        var tmp = 0
        var billion = 0
        val cn = chNum.toCharArray()

        // "一零二五" 形式
        if (cn.size > 1 && chNum.matches("^[〇零一二三四五六七八九壹贰叁肆伍陆柒捌玖]$".toRegex())) {
            for (i in cn.indices) {
                cn[i] = (48 + ChnMap[cn[i]]!!).toChar()
            }
            return String(cn).toInt()
        }

        // "一千零二十五", "一千二" 形式
        return try {
            for (i in cn.indices) {
                val tmpNum = ChnMap[cn[i]]!!
                if (tmpNum == 100000000) {
                    result += tmp
                    result *= tmpNum
                    billion = billion * 100000000 + result
                    result = 0
                    tmp = 0
                } else if (tmpNum == 10000) {
                    result += tmp
                    result *= tmpNum
                    tmp = 0
                } else if (tmpNum >= 10) {
                    if (tmp == 0) {
                        tmp = 1
                    }
                    result += tmpNum * tmp
                    tmp = 0
                } else {
                    tmp = if (i >= 2 && i == cn.size - 1 && ChnMap[cn[i - 1]]!! > 10) {
                        tmpNum * ChnMap[cn[i - 1]]!! / 10
                    } else {
                        tmp * 10 + tmpNum
                    }
                }
            }
            result += tmp + billion
            result
        } catch (e: Exception) {
            -1
        }
    }

    fun stringToInt(str: String?): Int {
        if (str != null) {
            val num = fullToHalf(str).replace("\\s".toRegex(), "")
            return try {
                num.toInt()
            } catch (e: Exception) {
                chineseNumToInt(num)
            }
        }
        return -1
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun base64Decode(str: String?): String {
        val bytes = Base64.decode(str, Base64.DEFAULT)
        return try {
            String(bytes, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            String(bytes)
        }
    }

    fun escape(src: String): String {
        var i: Int
        var j: Char
        val tmp = StringBuilder()
        tmp.ensureCapacity(src.length * 6)
        i = 0
        while (i < src.length) {
            j = src[i]
            if (Character.isDigit(j) || Character.isLowerCase(j)
                || Character.isUpperCase(j)
            ) {
                tmp.append(j)
            } else if (j.code < 256) {
                tmp.append("%")
                if (j.code < 16) {
                    tmp.append("0")
                }
                tmp.append(Integer.toString(j.code, 16))
            } else {
                tmp.append("%u")
                tmp.append(Integer.toString(j.code, 16))
            }
            i++
        }
        return tmp.toString()
    }

    @JvmStatic
    fun isJsonType(str: String): Boolean {
        var str = str
        var result = false
        if (!TextUtils.isEmpty(str)) {
            str = str.trim { it <= ' ' }
            if (str.startsWith("{") && str.endsWith("}")) {
                result = true
            } else if (str.startsWith("[") && str.endsWith("]")) {
                result = true
            }
        }
        return result
    }

    fun isJsonObject(text: String): Boolean {
        var text = text
        var result = false
        if (!TextUtils.isEmpty(text)) {
            text = text.trim { it <= ' ' }
            if (text.startsWith("{") && text.endsWith("}")) {
                result = true
            }
        }
        return result
    }

    @JvmStatic
    fun isJsonArray(text: String): Boolean {
        var text = text
        var result = false
        if (!TextUtils.isEmpty(text)) {
            text = text.trim { it <= ' ' }
            if (text.startsWith("[") && text.endsWith("]")) {
                result = true
            }
        }
        return result
    }

    @JvmStatic
    fun isTrimEmpty(text: String?): Boolean {
        if (text == null) {
            return true
        }
        return if (text.length == 0) {
            true
        } else text.trim { it <= ' ' }.length == 0
    }

    @JvmStatic
    fun startWithIgnoreCase(src: String?, obj: String?): Boolean {
        if (src == null || obj == null) {
            return false
        }
        return if (obj.length > src.length) {
            false
        } else src.substring(0, obj.length).equals(obj, ignoreCase = true)
    }

    fun endWithIgnoreCase(src: String?, obj: String?): Boolean {
        if (src == null || obj == null) {
            return false
        }
        return if (obj.length > src.length) {
            false
        } else src.substring(src.length - obj.length)
            .equals(obj, ignoreCase = true)
    }

    fun isContainNumber(company: String?): Boolean {
        val p = Pattern.compile("[0-9]")
        val m = p.matcher(company)
        return m.find()
    }

    fun isNumeric(str: String?): Boolean {
        val pattern = Pattern.compile("[0-9]*")
        val isNum = pattern.matcher(str)
        return isNum.matches()
    }

    // 移除字符串首尾空字符的高效方法(利用ASCII值判断,包括全角空格)
    fun trim(s: String): String {
        if (isEmpty(s)) {
            return ""
        }
        var start = 0
        val len = s.length
        var end = len - 1
        while (start < end && (s[start].code <= 0x20 || s[start] == '　')) {
            ++start
        }
        while (start < end && (s[end].code <= 0x20 || s[end] == '　')) {
            --end
        }
        if (end < len) {
            ++end
        }
        return if (start > 0 || end < len) s.substring(start, end) else s
    }

    fun repeat(str: String?, n: Int): String {
        val stringBuilder = StringBuilder()
        for (i in 0 until n) {
            stringBuilder.append(str)
        }
        return stringBuilder.toString()
    }

    fun removeUTFCharacters(data: String?): String? {
        if (data == null) {
            return null
        }
        val p = Pattern.compile("\\\\u(\\p{XDigit}{4})")
        val m = p.matcher(data)
        val buf = StringBuffer(data.length)
        while (m.find()) {
            val ch = m.group(1).toInt(16).toChar().toString()
            m.appendReplacement(buf, Matcher.quoteReplacement(ch))
        }
        m.appendTail(buf)
        return buf.toString()
    }

    @JvmStatic
    fun isEmpty(text: String?): Boolean {
        return text == null || text.length == 0
    }

    //只有一个为空
    fun isOneEmpty(vararg strings: String?): Boolean {
        for (s in strings) {
            if (isEmpty(s)) {
                return true
            }
        }
        return false
    }

    //英文字符计0.5， 其它计1 ，最后总长度向上取整
    @JvmStatic
    fun getWordLength(str: String): Int {
        var str = str
        var wordLength = 0
        str = replaceBlank(str)
        val chars = str.toCharArray()
        for (i in chars.indices) {
            val acsiiCode = chars[i].toInt()
            wordLength += if (acsiiCode < 128) {
                1
            } else {
                2
            }
        }
        return (wordLength + 1) / 2
    }

    /**
     * 过滤掉字符串中的空白
     */
    fun replaceBlank(str: String?): String {
        var dest = ""
        if (str != null) {
            val p = Pattern.compile("[\\s\\r\\n\\u3000\\u00a0]")
            val m = p.matcher(str)
            dest = m.replaceAll("")
        }
        return dest
    }//必须放在第一个转

    /***编辑器里面的内容，要对HTML实体进行转义 */
    private val htmlEntityMap: LinkedHashMap<String, String>
        get() {
            val entityMap = LinkedHashMap<String, String>()
            //必须放在第一个转
            entityMap["&"] = "&amp;"
            entityMap["<"] = "&lt;"
            entityMap[">"] = "&gt;"
            entityMap[" "] = "&nbsp;"
            entityMap["\""] = "&quot;"
            entityMap["'"] = "&apos;"
            entityMap["×"] = "&times;"
            entityMap["÷"] = "&divide;"
            entityMap["©"] = "&copy;"
            entityMap["¡"] = "&iexcl;"
            entityMap["¢"] = "&cent;"
            entityMap["£"] = "&pound;"
            entityMap["¤"] = "&curren;"
            entityMap["¥"] = "&yen;"
            entityMap["¦"] = "&brvbar;"
            entityMap["§"] = "&sect;"
            entityMap["¨"] = "&uml;"
            entityMap["ª"] = "&ordf;"
            entityMap["«"] = "&laquo;"
            entityMap["¬"] = "&not;"
            entityMap["»"] = "&raquo;"
            entityMap["¼"] = "&frac14;"
            entityMap["½"] = "&frac12;"
            entityMap["¾"] = "&frac34;"
            entityMap["¿"] = "&iquest;"
            return entityMap
        }

    @JvmStatic
    fun encodeHTMLEntity(content: String?): String {
        if (content == null) return ""
        var html: String = content

        // 转义的时候，先把 & 实体转义，再转义其他的实体
        val entityMap = LinkedHashMap<String, String>()
        entityMap["&"] = "&amp;"
        entityMap.putAll(htmlEntityMap)
        for ((key, value) in entityMap) {
            html = html.replace(key.toRegex(), value)
        }
        return html
    }

    fun wordCountFormat(wc: String?): String {
        if (wc == null) return ""
        var wordsS = ""
        if (isNumeric(wc)) {
            val words: Int = wc.toInt()
            if (words > 0) {
                wordsS = words.toString() + "字"
                if (words > 10000) {
                    val df = DecimalFormat("#.#")
                    wordsS = df.format(words * 1.0f / 10000f.toDouble()) + "万字"
                }
            }
        } else {
            wordsS = wc
        }
        return wordsS
    }

}
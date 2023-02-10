package com.liuzhenli.common.utils

import android.content.Context
import com.liuzhenli.common.utils.AppLog.put
import com.liuzhenli.common.utils.StringUtils.startWithIgnoreCase
import android.content.Intent
import android.net.NetworkInfo
import android.net.ConnectivityManager
import android.telephony.TelephonyManager
import android.net.wifi.WifiManager
import android.provider.Settings
import android.text.TextUtils
import cn.hutool.core.lang.Validator
import okhttp3.internal.publicsuffix.PublicSuffixDatabase
import retrofit2.Response
import java.lang.Exception
import java.lang.UnsupportedOperationException
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.net.URL
import java.util.*
import java.util.regex.Pattern

/**
 * Created by lfh on 2016/9/3.
 */
object NetworkUtils {


    /*** wifi network */
    private const val NETWORK_WIFI = 1

    /*** "4G" networks */
    private const val NETWORK_4G = 4

    /*** "3G" networks  */
    private const val NETWORK_3G = 3

    /*** "2G" networks */
    private const val NETWORK_2G = 2

    /*** unknown network */
    private const val NETWORK_UNKNOWN = 5

    /*** no network */
    private const val NETWORK_NO = -1
    private const val NETWORK_TYPE_GSM = 16
    private const val NETWORK_TYPE_TD_SCDMA = 17
    private const val NETWORK_TYPE_IWLAN = 18

    /**
     * 打开网络设置界面
     *
     * 3.0以下打开设置界面
     *
     * @param context 上下文
     */
    fun openWirelessSettings(context: Context) {
        context.startActivity(Intent(Settings.ACTION_SETTINGS))
    }

    /**
     * 获取活动网络信息
     *
     * @param context 上下文
     * @return NetworkInfo
     */
    private fun getActiveNetworkInfo(context: Context): NetworkInfo? {
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }

    /**
     * 判断网络是否可用
     *
     * 需添加权限 `<uses-permission android:name="android.permission
     * .ACCESS_NETWORK_STATE"/>`
     *
     * @param context 上下文
     * @return `true`: 可用<br></br>`false`: 不可用
     */
    @JvmStatic
    fun isNetWorkAvailable(context: Context): Boolean {
        val info = getActiveNetworkInfo(context)
        return info != null && info.isAvailable
    }

    /**
     * 判断网络是否连接
     *
     * 需添加权限 `<uses-permission android:name="android.permission
     * .ACCESS_NETWORK_STATE"/>`
     *
     * @param context 上下文
     * @return `true`: 是<br></br>`false`: 否
     */
    @JvmStatic
    fun isConnected(context: Context): Boolean {
        val info = getActiveNetworkInfo(context)
        return info != null && info.isConnected
    }

    /**
     * 判断网络是否是4G
     *
     * 需添加权限 `<uses-permission android:name="android.permission
     * .ACCESS_NETWORK_STATE"/>`
     *
     * @param context 上下文
     * @return `true`: 是<br></br>`false`: 不是
     */
    fun is4G(context: Context): Boolean {
        val info = getActiveNetworkInfo(context)
        return info != null && info.isAvailable && info.subtype == TelephonyManager.NETWORK_TYPE_LTE
    }

    /**
     * 判断wifi是否连接状态
     *
     * 需添加权限 `<uses-permission android:name="android.permission
     * .ACCESS_NETWORK_STATE"/>`
     *
     * @param context 上下文
     * @return `true`: 连接<br></br>`false`: 未连接
     */
    fun isWifiConnected(context: Context): Boolean {
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm != null && cm.activeNetworkInfo != null && cm.activeNetworkInfo!!
            .type == ConnectivityManager.TYPE_WIFI
    }

    /**
     * 获取移动网络运营商名称
     *
     * 如中国联通、中国移动、中国电信
     *
     * @param context 上下文
     * @return 移动网络运营商名称
     */
    fun getNetworkOperatorName(context: Context): String? {
        val tm = context
            .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm?.networkOperatorName
    }

    /**
     * 获取移动终端类型
     *
     * @param context 上下文
     * @return 手机制式
     *
     *  * [TelephonyManager.PHONE_TYPE_NONE] : 0 手机制式未知
     *  * [TelephonyManager.PHONE_TYPE_GSM] : 1 手机制式为GSM，移动和联通
     *  * [TelephonyManager.PHONE_TYPE_CDMA] : 2 手机制式为CDMA，电信
     *  * [TelephonyManager.PHONE_TYPE_SIP] : 3
     *
     */
    fun getPhoneType(context: Context): Int {
        val tm = context
            .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm?.phoneType ?: -1
    }

    /**
     * 获取当前的网络类型(WIFI,2G,3G,4G)
     *
     * 需添加权限 `<uses-permission android:name="android.permission
     * .ACCESS_NETWORK_STATE"/>`
     *
     * @param context 上下文
     * @return 网络类型
     *
     *  * [.NETWORK_WIFI] = 1;
     *  * [.NETWORK_4G] = 4;
     *  * [.NETWORK_3G] = 3;
     *  * [.NETWORK_2G] = 2;
     *  * [.NETWORK_UNKNOWN] = 5;
     *  * [.NETWORK_NO] = -1;
     *
     */
    fun getNetWorkType(context: Context): Int {
        var netType = NETWORK_NO
        val info = getActiveNetworkInfo(context)
        if (info != null && info.isAvailable) {
            netType = if (info.type == ConnectivityManager.TYPE_WIFI) {
                NETWORK_WIFI
            } else if (info.type == ConnectivityManager.TYPE_MOBILE) {
                when (info.subtype) {
                    NETWORK_TYPE_GSM, TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> NETWORK_2G
                    NETWORK_TYPE_TD_SCDMA, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> NETWORK_3G
                    NETWORK_TYPE_IWLAN, TelephonyManager.NETWORK_TYPE_LTE -> NETWORK_4G
                    else -> {
                        val subtypeName = info.subtypeName
                        if (subtypeName.equals("TD-SCDMA", ignoreCase = true)
                            || subtypeName.equals("WCDMA", ignoreCase = true)
                            || subtypeName.equals("CDMA2000", ignoreCase = true)
                        ) {
                            NETWORK_3G
                        } else {
                            NETWORK_UNKNOWN
                        }
                    }
                }
            } else {
                NETWORK_UNKNOWN
            }
        }
        return netType
    }

    /**
     * 获取当前的网络类型(WIFI,2G,3G,4G)
     *
     * 依赖上面的方法
     *
     * @param context 上下文
     * @return 网络类型名称
     *
     *  * NETWORK_WIFI
     *  * NETWORK_4G
     *  * NETWORK_3G
     *  * NETWORK_2G
     *  * NETWORK_UNKNOWN
     *  * NETWORK_NO
     *
     */
    fun getNetWorkTypeName(context: Context): String {
        return when (getNetWorkType(context)) {
            NETWORK_WIFI -> "NETWORK_WIFI"
            NETWORK_4G -> "NETWORK_4G"
            NETWORK_3G -> "NETWORK_3G"
            NETWORK_2G -> "NETWORK_2G"
            NETWORK_NO -> "NETWORK_NO"
            else -> "NETWORK_UNKNOWN"
        }
    }


    /**
     * Get local Ip address.
     */
    @JvmStatic
    val localIPAddress: InetAddress?
        get() {
            var enumeration: Enumeration<NetworkInterface>? = null
            try {
                enumeration = NetworkInterface.getNetworkInterfaces()
            } catch (e: SocketException) {
                e.printStackTrace()
            }
            if (enumeration != null) {
                while (enumeration.hasMoreElements()) {
                    val nif = enumeration.nextElement()
                    val inetAddresses = nif.inetAddresses
                    if (inetAddresses != null) {
                        while (inetAddresses.hasMoreElements()) {
                            val inetAddress = inetAddresses.nextElement()
                            if (!inetAddress.isLoopbackAddress && isIPv4Address(inetAddress.hostAddress)) {
                                return inetAddress
                            }
                        }
                    }
                }
            }
            return null
        }

    /**
     * 获取当前连接wifi的名称
     */
    fun getConnectWifiSsid(context: Context?): String? {
        if (context != null && isWifiConnected(context)) {
            val wifiManager =
                context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            if (wifiManager != null) {
                val wifiInfo = wifiManager.connectionInfo
                return wifiInfo.ssid
            }
        }
        return null
    }

    /**
     * 获取绝对地址
     */
    fun getAbsoluteURL(baseURL: URL?, relativePath: String): String {
        if (baseURL == null) return relativePath
        if (relativePath.startsWith("http://") || relativePath.startsWith("https://")) {
            return relativePath
        }
        if (Regex("data:.*?;base64,(.*)").matches(relativePath)) {
            return relativePath
        }
        if (relativePath.startsWith("javascript")) {
            return ""
        }
        var relativeUrl = relativePath
        try {
            val parseUrl = URL(baseURL, relativePath)
            relativeUrl = parseUrl.toString()
            return relativeUrl
        } catch (e: Exception) {
            put("网址拼接出错\n\${e.localizedMessage}", e)
        }
        return relativeUrl
    }

    /**
     * 获取绝对地址
     */
    fun getAbsoluteURL(baseURL: String?, relativePath: String): String {
        var relativePath = relativePath
        if (TextUtils.isEmpty(relativePath)) {
            return ""
        }
        if (TextUtils.isEmpty(baseURL)) {
            return relativePath
        }
        var header: String? = null
        if (startWithIgnoreCase(relativePath, "@header:")) {
            header = relativePath.substring(0, relativePath.indexOf("}") + 1)
            relativePath = relativePath.substring(header.length)
        }
        try {
            val absoluteUrl = URL(baseURL)
            val parseUrl = URL(absoluteUrl, relativePath)
            relativePath = parseUrl.toString()
            if (header != null) {
                relativePath = header + relativePath
            }
            return relativePath
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return relativePath
    }


    /**
     * Ipv4 address check.
     */
    private val IPV4_PATTERN = Pattern.compile(
        "^(" + "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}" +
                "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$"
    )

    /**
     * Check if valid IPV4 address.
     *
     * @param input the address string to check for validity.
     * @return True if the input parameter is a valid IPv4 address.
     */
    fun isIPv4Address(input: String?): Boolean {
        return IPV4_PATTERN.matcher(input).matches()
    }

    fun getUrl(response: Response<*>): String {
        val networkResponse = response.raw().networkResponse
        return networkResponse?.request?.url?.toString()
            ?: response.raw().request.url.toString()
    }

    fun getBaseUrl(url: String?): String? {
        if (url == null || !url.startsWith("http")) {
            return null
        }
        val index = url.indexOf("/", 9)
        return if (index == -1) {
            url
        } else url.substring(0, index)
    }


    /**
     * 获取域名，供cookie保存和读取，处理失败返回传入的url
     * http://1.2.3.4 => 1.2.3.4
     * https://www.example.com =>  example.com
     * http://www.biquge.com.cn => biquge.com.cn
     * http://www.content.example.com => example.com
     */
    fun getSubDomain(url: String): String {
        val baseUrl = getBaseUrl(url) ?: return url
        return kotlin.runCatching {
            val mURL = URL(baseUrl)
            val host: String = mURL.host
            //mURL.scheme https/http
            //判断是否为ip
            if (isIPAddress(host)) return host
            //PublicSuffixDatabase处理域名
            PublicSuffixDatabase.get().getEffectiveTldPlusOne(host) ?: host
        }.getOrDefault(baseUrl)
    }

    /**
     * Check if valid IP address.
     */
    fun isIPAddress(input: String?): Boolean {
        return isIPv4Address(input) || isIPv6Address(input)
    }

    /**
     * Check if valid IPV6 address.
     */
    fun isIPv6Address(input: String?): Boolean {
        return input != null && Validator.isIpv6(input)
    }

    private val notNeedEncoding: BitSet by lazy {
        val bitSet = BitSet(256)
        for (i in 'a'.code..'z'.code) {
            bitSet.set(i)
        }
        for (i in 'A'.code..'Z'.code) {
            bitSet.set(i)
        }
        for (i in '0'.code..'9'.code) {
            bitSet.set(i)
        }
        for (char in "+-_.$:()!*@&#,[]") {
            bitSet.set(char.code)
        }
        return@lazy bitSet
    }

    /**
     * 支持JAVA的URLEncoder.encode出来的string做判断。 即: 将' '转成'+'
     * 0-9a-zA-Z保留 <br></br>
     * ! * ' ( ) ; : @ & = + $ , / ? # [ ] 保留
     * 其他字符转成%XX的格式，X是16进制的大写字符，范围是[0-9A-F]
     */
    fun hasUrlEncoded(str: String): Boolean {
        var needEncode = false
        var i = 0
        while (i < str.length) {
            val c = str[i]
            if (notNeedEncoding.get(c.code)) {
                i++
                continue
            }
            if (c == '%' && i + 2 < str.length) {
                // 判断是否符合urlEncode规范
                val c1 = str[++i]
                val c2 = str[++i]
                if (isDigit16Char(c1) && isDigit16Char(c2)) {
                    i++
                    continue
                }
            }
            // 其他字符，肯定需要urlEncode
            needEncode = true
            break
        }

        return !needEncode
    }

    /**
     * 判断c是否是16进制的字符
     */
    private fun isDigit16Char(c: Char): Boolean {
        return c in '0'..'9' || c in 'A'..'F' || c in 'a'..'f'
    }


}
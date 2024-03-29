package com.liuzhenli.common.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;

import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;


import com.liuzhenli.common.BaseApplication;

import org.jsoup.internal.StringUtil;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.text.TextUtils.isEmpty;
import static java.util.regex.Pattern.compile;

@SuppressWarnings({"unused", "WeakerAccess"})
public class StringUtils {
    private static final String TAG = "StringUtils";
    private static final int HOUR_OF_DAY = 24;
    private static final int DAY_OF_YESTERDAY = 2;
    private static final int TIME_UNIT = 60;
    private final static HashMap<Character, Integer> ChnMap = getChnMap();

    //将时间转换成日期
    public static String dateConvert(long time, String pattern) {
        Date date = new Date(time);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 将日期转换成昨天、今天、明天
     */
    public static String dateConvert(String source, String pattern) {
        @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = format.parse(source);
            long curTime = calendar.getTimeInMillis();
            calendar.setTime(date);
            //将MISC 转换成 sec
            long difSec = Math.abs((curTime - date.getTime()) / 1000);
            long difMin = difSec / 60;
            long difHour = difMin / 60;
            long difDate = difHour / 60;
            int oldHour = calendar.get(Calendar.HOUR);
            //如果没有时间
            if (oldHour == 0) {
                //比日期:昨天今天和明天
                if (difDate == 0) {
                    return "今天";
                } else if (difDate < DAY_OF_YESTERDAY) {
                    return "昨天";
                } else {
                    @SuppressLint("SimpleDateFormat") DateFormat convertFormat = new SimpleDateFormat("yyyy-MM-dd");
                    return convertFormat.format(date);
                }
            }

            if (difSec < TIME_UNIT) {
                return difSec + "秒前";
            } else if (difMin < TIME_UNIT) {
                return difMin + "分钟前";
            } else if (difHour < HOUR_OF_DAY) {
                return difHour + "小时前";
            } else if (difDate < DAY_OF_YESTERDAY) {
                return "昨天";
            } else {
                @SuppressLint("SimpleDateFormat") DateFormat convertFormat = new SimpleDateFormat("yyyy-MM-dd");
                return convertFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String toFirstCapital(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String getString(@StringRes int id) {
        return BaseApplication.getInstance().getResources().getString(id);
    }

    public static String getString(@StringRes int id, Object... formatArgs) {
        return BaseApplication.getInstance().getString(id, formatArgs);
    }

    /**
     * 将文本中的半角字符，转换成全角字符
     */
    public static String halfToFull(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) //半角空格
            {
                c[i] = (char) 12288;
                continue;
            }
            //根据实际情况，过滤不需要转换的符号
            //if (c[i] == 46) //半角点号，不转换
            // continue;
            //其他符号都转换为全角
            if (c[i] > 32 && c[i] < 127) {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }

    //功能：字符串全角转换为半角
    public static String fullToHalf(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            //全角空格
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }

            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    private static HashMap<Character, Integer> getChnMap() {
        HashMap<Character, Integer> map = new HashMap<>();
        String cnStr = "零一二三四五六七八九十";
        char[] c = cnStr.toCharArray();
        for (int i = 0; i <= 10; i++) {
            map.put(c[i], i);
        }
        cnStr = "〇壹贰叁肆伍陆柒捌玖拾";
        c = cnStr.toCharArray();
        for (int i = 0; i <= 10; i++) {
            map.put(c[i], i);
        }
        map.put('两', 2);
        map.put('百', 100);
        map.put('佰', 100);
        map.put('千', 1000);
        map.put('仟', 1000);
        map.put('万', 10000);
        map.put('亿', 100000000);
        return map;
    }

    @SuppressWarnings("ConstantConditions")
    public static int chineseNumToInt(String chNum) {
        int result = 0;
        int tmp = 0;
        int billion = 0;
        char[] cn = chNum.toCharArray();

        // "一零二五" 形式
        if (cn.length > 1 && chNum.matches("^[〇零一二三四五六七八九壹贰叁肆伍陆柒捌玖]$")) {
            for (int i = 0; i < cn.length; i++) {
                cn[i] = (char) (48 + ChnMap.get(cn[i]));
            }
            return Integer.parseInt(new String(cn));
        }

        // "一千零二十五", "一千二" 形式
        try {
            for (int i = 0; i < cn.length; i++) {
                int tmpNum = ChnMap.get(cn[i]);
                if (tmpNum == 100000000) {
                    result += tmp;
                    result *= tmpNum;
                    billion = billion * 100000000 + result;
                    result = 0;
                    tmp = 0;
                } else if (tmpNum == 10000) {
                    result += tmp;
                    result *= tmpNum;
                    tmp = 0;
                } else if (tmpNum >= 10) {
                    if (tmp == 0) {
                        tmp = 1;
                    }
                    result += tmpNum * tmp;
                    tmp = 0;
                } else {
                    if (i >= 2 && i == cn.length - 1 && ChnMap.get(cn[i - 1]) > 10) {
                        tmp = tmpNum * ChnMap.get(cn[i - 1]) / 10;
                    } else {
                        tmp = tmp * 10 + tmpNum;
                    }
                }
            }
            result += tmp + billion;
            return result;
        } catch (Exception e) {
            return -1;
        }
    }

    public static int stringToInt(String str) {
        if (str != null) {
            String num = fullToHalf(str).replaceAll("\\s", "");
            try {
                return Integer.parseInt(num);
            } catch (Exception e) {
                return chineseNumToInt(num);
            }
        }
        return -1;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String base64Decode(String str) {
        byte[] bytes = Base64.decode(str, Base64.DEFAULT);
        try {
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return new String(bytes);
        }
    }

    public static String escape(String src) {
        int i;
        char j;
        StringBuilder tmp = new StringBuilder();
        tmp.ensureCapacity(src.length() * 6);
        for (i = 0; i < src.length(); i++) {
            j = src.charAt(i);
            if (Character.isDigit(j) || Character.isLowerCase(j)
                    || Character.isUpperCase(j)) {
                tmp.append(j);
            } else if (j < 256) {
                tmp.append("%");
                if (j < 16) {
                    tmp.append("0");
                }
                tmp.append(Integer.toString(j, 16));
            } else {
                tmp.append("%u");
                tmp.append(Integer.toString(j, 16));
            }
        }
        return tmp.toString();
    }

    public static boolean isJsonType(String str) {
        boolean result = false;
        if (!TextUtils.isEmpty(str)) {
            str = str.trim();
            if (str.startsWith("{") && str.endsWith("}")) {
                result = true;
            } else if (str.startsWith("[") && str.endsWith("]")) {
                result = true;
            }
        }
        return result;
    }

    public static boolean isJsonObject(String text) {
        boolean result = false;
        if (!TextUtils.isEmpty(text)) {
            text = text.trim();
            if (text.startsWith("{") && text.endsWith("}")) {
                result = true;
            }
        }
        return result;
    }

    public static boolean isJsonArray(String text) {
        boolean result = false;
        if (!TextUtils.isEmpty(text)) {
            text = text.trim();
            if (text.startsWith("[") && text.endsWith("]")) {
                result = true;
            }
        }
        return result;
    }

    public static boolean isTrimEmpty(String text) {
        if (text == null) {
            return true;
        }
        if (text.length() == 0) {
            return true;
        }
        return text.trim().length() == 0;
    }

    public static boolean startWithIgnoreCase(String src, String obj) {
        if (src == null || obj == null) {
            return false;
        }
        if (obj.length() > src.length()) {
            return false;
        }
        return src.substring(0, obj.length()).equalsIgnoreCase(obj);
    }

    public static boolean endWithIgnoreCase(String src, String obj) {
        if (src == null || obj == null) {
            return false;
        }
        if (obj.length() > src.length()) {
            return false;
        }
        return src.substring(src.length() - obj.length()).equalsIgnoreCase(obj);
    }

    public static boolean isContainNumber(String company) {
        Pattern p = compile("[0-9]");
        Matcher m = p.matcher(company);
        return m.find();
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static String getBaseUrl(String url) {
        if (url == null || !url.startsWith("http")) {
            return null;
        }
        int index = url.indexOf("/", 9);
        if (index == -1) {
            return url;
        }
        return url.substring(0, index);
    }

    // 移除字符串首尾空字符的高效方法(利用ASCII值判断,包括全角空格)
    public static String trim(String s) {
        if (isEmpty(s)) {
            return "";
        }
        int start = 0, len = s.length();
        int end = len - 1;
        while ((start < end) && ((s.charAt(start) <= 0x20) || (s.charAt(start) == '　'))) {
            ++start;
        }
        while ((start < end) && ((s.charAt(end) <= 0x20) || (s.charAt(end) == '　'))) {
            --end;
        }
        if (end < len) {
            ++end;
        }
        return ((start > 0) || (end < len)) ? s.substring(start, end) : s;
    }

    public static String repeat(String str, int n) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            stringBuilder.append(str);
        }
        return stringBuilder.toString();
    }

    public static String removeUTFCharacters(String data) {
        if (data == null) {
            return null;
        }
        Pattern p = compile("\\\\u(\\p{XDigit}{4})");
        Matcher m = p.matcher(data);
        StringBuffer buf = new StringBuffer(data.length());
        while (m.find()) {
            String ch = String.valueOf((char) Integer.parseInt(m.group(1), 16));
            m.appendReplacement(buf, Matcher.quoteReplacement(ch));
        }
        m.appendTail(buf);
        return buf.toString();
    }

    public static String formatHtml(String html) {
        if (TextUtils.isEmpty(html)) {
            return "";
        }
        return // 替换特定标签为换行符
                html.replaceAll("(?i)<(br[\\s/]*|/*p.*?|/*div.*?)>", "\n")
                        // 删除script标签对和空格转义符
                        .replaceAll("<[script>]*.*?>|&nbsp;", "")
                        // 移除空行,并增加段前缩进2个汉字
                        .replaceAll("\\s*\\n+\\s*", "\n　　")
                        //移除开头空行,并增加段前缩进2个汉字
                        .replaceAll("^[\\n\\s]+", "　　")
                        //移除尾部空行
                        .replaceAll("[\\n\\s]+$", "");
    }

    public static boolean isEmpty(String text) {
        return text == null || text.length() == 0;
    }

    //只有一个为空
    public static boolean isOneEmpty(String... strings) {
        for (String s : strings) {
            if (isEmpty(s)) {
                return true;
            }
        }

        return false;
    }


    //英文字符计0.5， 其它计1 ，最后总长度向上取整
    public static int getWordLength(String str) {
        int wordLength = 0;

        str = replaceBlank(str);

        char[] chars = str.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            int acsiiCode = (int) chars[i];
            if (acsiiCode < 128) {
                wordLength += 1;
            } else {
                wordLength += 2;
            }
        }

        return (wordLength + 1) / 2;
    }

    /**
     * 过滤掉字符串中的空白
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("[\\s\\r\\n\\u3000\\u00a0]");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


    /***编辑器里面的内容，要对HTML实体进行转义*/
    private static LinkedHashMap<String, String> getHtmlEntityMap() {
        LinkedHashMap<String, String> entityMap = new LinkedHashMap<String, String>();
        //必须放在第一个转
        entityMap.put("&", "&amp;");
        entityMap.put("<", "&lt;");
        entityMap.put(">", "&gt;");
        entityMap.put(" ", "&nbsp;");
        entityMap.put("\"", "&quot;");
        entityMap.put("'", "&apos;");
        entityMap.put("×", "&times;");
        entityMap.put("÷", "&divide;");
        entityMap.put("©", "&copy;");

        entityMap.put("¡", "&iexcl;");
        entityMap.put("¢", "&cent;");
        entityMap.put("£", "&pound;");
        entityMap.put("¤", "&curren;");
        entityMap.put("¥", "&yen;");
        entityMap.put("¦", "&brvbar;");
        entityMap.put("§", "&sect;");
        entityMap.put("¨", "&uml;");
        entityMap.put("ª", "&ordf;");
        entityMap.put("«", "&laquo;");
        entityMap.put("¬", "&not;");

        entityMap.put("»", "&raquo;");
        entityMap.put("¼", "&frac14;");
        entityMap.put("½", "&frac12;");
        entityMap.put("¾", "&frac34;");
        entityMap.put("¿", "&iquest;");

        return entityMap;
    }

    public static String encodeHTMLEntity(String content) {
        if (content == null)
            return "";

        String html = content;

        // 转义的时候，先把 & 实体转义，再转义其他的实体
        LinkedHashMap<String, String> entityMap = new LinkedHashMap<String, String>();
        entityMap.put("&", "&amp;");
        entityMap.putAll(getHtmlEntityMap());

        for (Map.Entry<String, String> entry : entityMap.entrySet()) {
            html = html.replaceAll(entry.getKey(), entry.getValue());
        }

        return html;
    }
}

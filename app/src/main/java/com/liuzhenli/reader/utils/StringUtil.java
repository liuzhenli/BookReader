package com.liuzhenli.reader.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-10-23 13:50
 */
public class StringUtil {
    /**
     * 过滤不可见的字符，但是逗号除外
     */
    public static String filterInvisiableChar(String str, String replaceChar) {
        String regEx = "[^\\x20-\\x2b\\x2d-\\x7e]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        return matcher.replaceAll(replaceChar).trim();
    }

}

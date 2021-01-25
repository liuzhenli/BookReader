package com.liuzhenli.common.exception;

/**
 * @author Liuzhenli
 * @since 2019-07-07 02:33
 */
public class Error {
    /**
     * 未知错误
     */
    public static final int UNKNOWN = 1000;
    /**
     * 解析错误
     */
    public static final int PARSE_ERROR = 1001;
    /**
     * 网络错误
     */
    public static final int NETWORK_ERROR = 1002;
    /**
     * 协议出错
     */
    public static final int HTTP_ERROR = 1003;

    /**
     * 其他已知错误,如没有更多数据
     */
    public static final int KNOWN_ERROR = 1004;
}

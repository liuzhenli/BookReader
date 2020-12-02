package com.liuzhenli.common.exception;

/**
 * view层的exception,用来显示异常状态
 *
 * @author Liuzhenli
 * @since 2019-07-06 11:00
 */
public class ApiException extends Exception {
    private int mCode;
    /**
     * 用于显示的异常信息
     */
    private String mDisplayMessage;

    public ApiException(int code, Throwable cause) {
        super(cause);
        this.mCode = code;
    }



    public void setDisplayMessage(String message) {
        this.mDisplayMessage = message;
    }

    public String getDisplayMessage() {
        return this.mDisplayMessage;
    }

    public int getCode() {
        return this.mCode;
    }

}

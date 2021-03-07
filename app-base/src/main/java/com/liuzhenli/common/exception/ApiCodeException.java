package com.liuzhenli.common.exception;


import com.liuzhenli.common.utils.Constant;

public class ApiCodeException extends RuntimeException {
    public int getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(int errorCode) {
        mErrorCode = errorCode;
    }

    private int mErrorCode;

    public ApiCodeException(int errorCode, String errorMessage) {
        super(errorMessage);
        mErrorCode = errorCode;
    }

    /**
     * 判断是否是token失效
     *
     * @return 失效返回true, 否则返回false;
     */
    public boolean isTokenExperiod() {
        return mErrorCode == Constant.TOKEN_EXPRIED;
    }
}
package com.liuzhenli.reader.exception;

/**
 * @author Liuzhenli
 * @since 2019-07-07 02:39
 */
public class ServerException extends RuntimeException {
    private int code;
    private String message;

    public ServerException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}

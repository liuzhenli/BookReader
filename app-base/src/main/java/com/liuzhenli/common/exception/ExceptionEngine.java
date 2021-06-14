package com.liuzhenli.common.exception;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.liuzhenli.common.utils.L;
import com.orhanobut.logger.BuildConfig;
import com.orhanobut.logger.Logger;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import retrofit2.adapter.rxjava2.HttpException;


/**
 * @author Liuzhenli
 * @since 2019-07-06 11:00
 */
public class ExceptionEngine {
    /**
     * 请求要求身份验证,未经授权,对于需要登录接口,可能会返回这个错误
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * 服务器已经理解,但是拒绝执行
     */
    public static final int FORBIDDEN = 403;
    /**
     * 请求失败
     */
    public static final int NOT_FOUND = 404;

    /**
     * 服务器无法完成对应的请求,一般来说这个问题来自服务器的乱码
     */
    public static final int INTERNAL_SERVER_ERROR = 500;
    /**
     * 由于服务器临时的维护或者过载,无法完成当前请求,过一会会恢复
     */
    public static final int SERVICE_UNAVAILABle = 503;
    /**
     * 超时
     */

    public static final int GATEWAY_TIMEOUT = 504;


    public static ApiException handleException(Throwable throwable) {
        ApiException ex = null;
        //http错误
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            String url = httpException.response().raw().request().url().toString();
            if (BuildConfig.DEBUG) {
                Logger.e("httpException  ", url);
            }
            ex = new ApiException(Error.HTTP_ERROR, throwable);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case INTERNAL_SERVER_ERROR:
                case SERVICE_UNAVAILABle:
                case GATEWAY_TIMEOUT:
                default:
                    ex.setDisplayMessage(String.format("网络错误:code(%s)", httpException.code()));
            }
        } else if (throwable instanceof ServerException) {
            ServerException serverException = (ServerException) throwable;
            ex = new ApiException(serverException.getCode(), serverException);
            //数据解析错误
        } else if (throwable instanceof JsonParseException
                || throwable instanceof JSONException
                || throwable instanceof ParseException) {
            ex = new ApiException(Error.PARSE_ERROR, throwable);
            if (BuildConfig.DEBUG) {
                ex.setDisplayMessage(L.getErrorString(throwable));
            } else {
                ex.setDisplayMessage("数据解析失败");
            }
            //网络连接错误
        } else if (throwable instanceof ConnectException) {
            ex = new ApiException(Error.NETWORK_ERROR, throwable);
            ex.setDisplayMessage("连接失败");
        } else if (throwable instanceof SocketTimeoutException) {
            ex = new ApiException(Error.NETWORK_ERROR, throwable);
            ex.setDisplayMessage("网络连接超时");
        } else if (throwable instanceof SocketException) {
            ex = new ApiException(Error.NETWORK_ERROR, throwable);
            ex.setDisplayMessage("网络错误");
        } else if (throwable instanceof ApiCodeException) {
            ex = new ApiException(((ApiCodeException) throwable).getErrorCode(), throwable);
            ex.setDisplayMessage(throwable.getMessage());
        } else if (throwable instanceof UnknownHostException) {
            ex = new ApiException(Error.NETWORK_ERROR, throwable);
            ex.setDisplayMessage("网络错误");
        } else if (throwable instanceof SSLHandshakeException) {
            ex = new ApiException(Error.NETWORK_ERROR, throwable);
            ex.setDisplayMessage("网络错误");
        } else {
            ex = new ApiException(Error.UNKNOWN, throwable);
            if (BuildConfig.DEBUG) {
                ex.setDisplayMessage(L.getErrorString(throwable));
            } else {
                ex.setDisplayMessage("发生未知错误");
            }
        }


        return ex;
    }
}

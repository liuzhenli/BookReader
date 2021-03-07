package com.liuzhenli.common.network.support;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.liuzhenli.common.BaseApplication;
import com.liuzhenli.common.base.BaseBean;
import com.liuzhenli.common.exception.ApiCodeException;
import com.liuzhenli.common.exception.CrashHandler;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;


/**
 * @author liuzhenli
 */
public class RewriteCacheControlInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    /**
     * 24小时制
     */
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();


        boolean networkAvailable = CrashHandler.getInstance().isNetworkAvailable(BaseApplication.getInstance());
        if (!networkAvailable) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            Log.i("cache", "no network");
        }
        Response originalResponse = chain.proceed(request);
        if (networkAvailable) {
/*
            if (originalResponse.code()!=200){
                try {
                    FileUtils.writeFile(Constant.LOG_PATH, format.format(System.currentTimeMillis()) + ":"+ originalResponse.code()+ ":"+request.url().toString() +"\n", true);
                }catch (Exception e){

                }
            }
*/
            ResponseBody responseBody = originalResponse.body();
            BufferedSource source = responseBody.source();
            // Buffer the entire body.
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            BaseBean httpStatus = null;
            try {
                httpStatus = new Gson().fromJson(buffer.clone().readString(charset), BaseBean.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (httpStatus != null) {

                if (httpStatus.isCodeInvalid()) {
                    throw new ApiCodeException(httpStatus.code, httpStatus.msg);
                }
                if (BaseApplication.isDebug) {
                    Log.e("request---ulr--:\n", request.url().toString());
                }
            }
            String cacheControl = request.cacheControl().toString();
            if (TextUtils.isEmpty(cacheControl)) {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "no-cache")
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            }
        } else {
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                    .removeHeader("Pragma")
                    .build();
        }


    }
}

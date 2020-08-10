package com.liuzhenli.reader.network.support;


import android.util.Log;

import com.google.gson.Gson;
import com.liuzhenli.common.utils.NetworkUtils;
import com.liuzhenli.reader.ReaderApplication;
import com.liuzhenli.reader.utils.AccountManager;
import com.liuzhenli.reader.utils.AppUtils;
import com.liuzhenli.reader.utils.DeviceUtil;
import com.liuzhenli.reader.utils.StringUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Retrofit2 Cookie拦截器。用于保存和设置Cookies
 *
 * @author yuyh.
 * @since 16/8/6.
 */
public final class HeaderInterceptor implements Interceptor {

    private Map<String, String> commonParams = new HashMap<>();

    private void initCommonParams() {
        commonParams.put("_filterData", "1");
        //android 2
        commonParams.put("platform", "2");
        commonParams.put("_versions", AppUtils.getAppVersionCode(ReaderApplication.getInstance()) + "");
        commonParams.put("merchant", AppUtils.getChannelValue(ReaderApplication.getInstance()));
    }

    private void initTangYuanCommonParams() {
        String brand = StringUtil.filterInvisiableChar(android.os.Build.BRAND, "*");
        String model = StringUtil.filterInvisiableChar(android.os.Build.MODEL, "*");
        String osVersionNum = StringUtil.filterInvisiableChar(android.os.Build.VERSION.RELEASE, "*");

        String deviceModel = brand + "-" + model;
        String deviceOSVersion = "Android" + osVersionNum;
        String appVersion = AppUtils.getAppVersionName(ReaderApplication.getInstance())
                + " rv-" + AppUtils.getAppVersionCode(ReaderApplication.getInstance());
        String deviceId = DeviceUtil.getPhoneUid();
        String appChannel = AppUtils.getChannelValue(ReaderApplication.getInstance());

        String userAgentFormat = "%1$s,%2$s,%3$s,%4$s,%5$s,%6$s";
        String userAgent = String.format(userAgentFormat, "Android", deviceModel, deviceOSVersion, appVersion, appChannel, deviceId);
        commonParams.put("User-Agent", userAgent);
        Log.e("userAgent--", userAgent);

        // 所有请求，都添加一条自定义的 Header
        long currentTime = System.currentTimeMillis() / 1000;
        commonParams.put("X-TY-Request", "ts=" + currentTime + "; network=" + NetworkUtils.getNetWorkType(ReaderApplication.getInstance()));
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        //initCommonParams();
        initTangYuanCommonParams();
        Request oldRequest = chain.request();
        Request.Builder newRequestBuild = null;
        String method = oldRequest.method();
        Request newRequest;

        RequestBody body = oldRequest.body();
        if ("GET".equals(method) || "DELETE".equals(method) || body instanceof MultipartBody) {

            HttpUrl.Builder commonParamsUrlBuilder = oldRequest.url()
                    .newBuilder()
                    .scheme(oldRequest.url().scheme())
                    .host(oldRequest.url().host());
            for (Map.Entry<String, String> item : commonParams.entrySet()) {
                commonParamsUrlBuilder.addQueryParameter(item.getKey(), item.getValue());
            }
            newRequestBuild = oldRequest.newBuilder()
                    .method(oldRequest.method(), body)
                    .url(commonParamsUrlBuilder.build());

        } else {
            if (body instanceof FormBody) {
                FormBody.Builder newFormBody = new FormBody.Builder();
                FormBody oldFormBody = (FormBody) body;
                for (int i = 0; i < oldFormBody.size(); i++) {
                    String paramName = oldFormBody.encodedName(i);
                    newFormBody.addEncoded(paramName, oldFormBody.encodedValue(i));

                }
                for (Map.Entry<String, String> item : commonParams.entrySet()) {
                    newFormBody.add(item.getKey(), item.getValue());
                }


                newRequestBuild = oldRequest.newBuilder().method(oldRequest.method(), newFormBody.build());
            } else if (body instanceof RequestBody) {
                //buffer流
                HashMap<String, Object> rootMap = null;
                Gson mGson = new Gson();
                Buffer buffer = new Buffer();
                body.writeTo(buffer);
                String oldParamsJson = buffer.readUtf8();
                //原始参数
                rootMap = mGson.fromJson(oldParamsJson, HashMap.class);
                if (rootMap == null) {
                    rootMap = new HashMap<>();
                }

                for (Map.Entry<String, String> item : commonParams.entrySet()) {
                    rootMap.put(item.getKey(), item.getValue());
                }
                //装换成json字符串
                String newJsonParams = mGson.toJson(rootMap);

                newRequestBuild = oldRequest.newBuilder().post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), newJsonParams));
            }
        }


        if (AccountManager.getInstance().isLogin()) {
            newRequestBuild = newRequestBuild
                    .addHeader("Cookie", "accessToken=" + AccountManager.getInstance().getToken());
        }
        newRequest = newRequestBuild
                .addHeader("Connection", "close")
                .build();

        return chain.proceed(newRequest);
    }
}

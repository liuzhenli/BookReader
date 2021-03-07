package com.liuzhenli.common.network.support;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.liuzhenli.common.BaseApplication;
import com.liuzhenli.common.utils.ApiManager;
import com.liuzhenli.common.utils.AppUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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

import static com.liuzhenli.common.utils.Constant.USER_AGENT;

/**
 * Retrofit2 Cookie拦截器。用于保存和设置Cookies
 *
 * @author yuyh.
 * @since 16/8/6.
 */
public final class HeaderInterceptor implements Interceptor {

    private Map<String, String> commonParams = new HashMap<>();

    private void initCommonParams() {
        commonParams.put("_versions", AppUtils.getAppVersionCode(BaseApplication.getInstance()) + "");
        commonParams.put("merchant", AppUtils.getChannelValue(BaseApplication.getInstance()));
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        initCommonParams();
        Request oldRequest = chain.request();
        Request.Builder newRequestBuild = null;
        String method = oldRequest.method();
        Request newRequest = null;
        HttpUrl newUrl = null;
        /*需要替换的url*/
        List<String> urlNameList = oldRequest.headers("url_name");
        if (urlNameList.size() > 0) {
            oldRequest.newBuilder().removeHeader("url_name");
            String urlName = urlNameList.get(0);
            String url = ApiManager.getInstance().getUrl(urlName);
            if (!TextUtils.isEmpty(url)) {
                newUrl = HttpUrl.parse(url);
            } else {
                newUrl = oldRequest.url();
            }
        } else {
            newUrl = oldRequest.url();
        }

        RequestBody body = oldRequest.body();
        HttpUrl.Builder commonParamsUrlBuilder = oldRequest.url()
                .newBuilder()
                .scheme(newUrl.scheme())
                .encodedPath(newUrl.encodedPath())
                .host(newUrl.host())
                .port(newUrl.port());

        if ("GET".equals(method) || "DELETE".equals(method)) {
            for (Map.Entry<String, String> item : commonParams.entrySet()) {
                commonParamsUrlBuilder.addQueryParameter(item.getKey(), item.getValue());
            }
            newRequestBuild = oldRequest.newBuilder().method(oldRequest.method(), body);
        } else if (body instanceof MultipartBody) {
            MultipartBody requestBody = (MultipartBody) oldRequest.body();
            MultipartBody.Builder multipartBodybuilder = new MultipartBody.Builder();
            //在加新参数
            for (Map.Entry<String, String> item : commonParams.entrySet()) {
                MultipartBody.Part part = MultipartBody.Part.createFormData(item.getKey(), item.getValue());
                multipartBodybuilder.addPart(part);
            }
            newRequestBuild = oldRequest.newBuilder().method(oldRequest.method(), body);
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
            } else if (body != null) {
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


        if (newRequestBuild != null) {

            newRequestBuild.addHeader("User-Agent", USER_AGENT);
            newRequestBuild.addHeader("Pragma", "no-cache");
            newRequestBuild.addHeader("Cache-Control", "no-cache");
            newRequestBuild.addHeader("Accept", "*/*");
            newRequestBuild.addHeader("Sec-Fetch-Site", "none");
            newRequestBuild.addHeader("Sec-Fetch-Mode", "cors");
            newRequestBuild.addHeader("Sec-Fetch-Dest", "empty");
            newRequestBuild.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
            newRequestBuild.addHeader("Cookie", "_d_id=fd820288f284d6444b09f90993e6f3;_d_id=fd850288f284d66a0a09f90993e6f3");

            newRequest = newRequestBuild.url(commonParamsUrlBuilder.build())
                    .addHeader("Connection", "keep-alive")
                    .build();
            return chain.proceed(newRequest);
        }
        return chain.proceed(newRequest);
    }
}

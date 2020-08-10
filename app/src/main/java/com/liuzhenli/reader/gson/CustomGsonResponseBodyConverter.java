package com.liuzhenli.reader.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

final class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
//        String response = value.string();
//        BaseBean httpStatus = gson.fromJson(response, BaseBean.class);
//        if (httpStatus!=null) {
//            if (!TextUtils.isEmpty(httpStatus.token)){
//                SharedPreferencesUtil.getInstance().putString("token",httpStatus.token);
//            }
//            if (httpStatus.isCodeInvalid()) {
//                value.close();
//                throw new ApiCodeException(httpStatus.status.code, httpStatus.status.msg);
//            }
//
//
//        }
//
//        MediaType contentType = value.contentType();
//        Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
//        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
//        Reader reader = new InputStreamReader(inputStream, charset);
//        JsonReader jsonReader = gson.newJsonReader(reader);
////        JsonReader jsonReader = gson.newJsonReader(value.charStream());
//        try {
//            return adapter.read(jsonReader);
//        } finally {
//            value.close();
//        }


        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}
package com.liuzhenli.reader.network;

import com.liuzhenli.reader.base.BaseBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author Liuzhenli
 * @since 2019-07-06 19:37
 */
public interface ApiService {
    @FormUrlEncoded
    @POST("https://i.itangyuan.com/login/phone.json")
    Observable<BaseBean> getLoginData(@FieldMap Map<String, String> body);
}

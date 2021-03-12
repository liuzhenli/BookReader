package com.micoredu.reader.network;

import com.liuzhenli.common.base.BaseBean;
import com.liuzhenli.common.network.BaseApiService;
import com.liuzhenli.common.utils.ApiManager;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author Liuzhenli
 * @since 2019-07-06 19:37
 */
public interface ReaderApiService extends BaseApiService {

    @Headers({"url_name:" + ApiManager.apiName.BOOK_SOURCE})
    @GET(ApiManager.apiName.BOOK_SOURCE)
    Observable<ResponseBody> getBookSource(@Query("id")String id);
}

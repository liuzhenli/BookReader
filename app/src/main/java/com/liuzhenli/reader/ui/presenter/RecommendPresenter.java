package com.liuzhenli.reader.ui.presenter;

import android.util.Log;

import com.liuzhenli.common.utils.GsonUtils;
import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.common.utils.StringUtils;
import com.liuzhenli.reader.base.RxPresenter;
import com.liuzhenli.reader.bean.BookSourceData;
import com.liuzhenli.reader.network.Api;
import com.liuzhenli.reader.observer.SampleProgressObserver;
import com.liuzhenli.reader.ui.contract.RecommendContract;
import com.liuzhenli.reader.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;

/**
 * Description:
 *
 * @author liuzhenli 2020/9/12
 * Email: 848808263@qq.com
 */
public class RecommendPresenter extends RxPresenter<RecommendContract.View> implements RecommendContract.Presenter<RecommendContract.View> {

    private Api mApi;

    @Inject
    public RecommendPresenter(Api api) {
        this.mApi = api;
    }


    @Override
    public void getSource() {

        String id = "2824-2823-2822-2821-2820-2819-2818-2817-2816-2815-2784-2545-2355-2814-2813-2812-2811-2810-2809-2808-2807-2806-2805-2804-2801-2803-2802-2793-2800-2799-2798-2538-2797-2296-2796-2795-2794-2792-2791-2790-2789-2788-2787-2786-2785-2783-2782-2781-2780-2779";
        DisposableObserver subscribe = RxUtil.subscribe(mApi.getBookSource(id), new SampleProgressObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody data) {
                List<BookSourceData> bookSourceList = new ArrayList<>();
                byte[] bytes = new byte[0];
                try {
                    bytes = data.bytes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String s = new String(bytes);
                if (StringUtils.isJsonArray(s)) {
                    bookSourceList = GsonUtils.parseJArray(s, BookSourceData.class);
                }
                mView.showSource(bookSourceList);
            }
        });
        addSubscribe(subscribe);
    }
}

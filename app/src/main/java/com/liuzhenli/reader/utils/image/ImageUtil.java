package com.liuzhenli.reader.utils.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.reader.observer.SampleProgressObserver;

import java.io.File;

import io.reactivex.Observable;

/**
 * @author Liuzhenli
 * @since 2019-07-06 10:26
 */
public class ImageUtil {
    /**
     * @param context activity fragment的contextFile file = Glide.with(mContext)
     *                .load(url)
     *                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
     *                .get();
     *                return file.getAbsolutePath();
     * @param url     图片地址
     * @param view    imageView
     */
    public void setImage(Context context, String url, ImageView view) {
        Glide.with(context).load(url).into(view);
    }


    /**
     * 下载图片
     */
    public void downloadImage(Context context, String url, SampleProgressObserver<String> observer) {
        RxUtil.subscribe(Observable.fromCallable(() -> {
            File file = Glide.with(context).load(url).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
            return file.getAbsolutePath();
        }), observer);
    }
}

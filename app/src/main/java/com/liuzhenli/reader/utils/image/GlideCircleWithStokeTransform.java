package com.liuzhenli.reader.utils.image;

import android.content.Context;
import android.graphics.Bitmap;

import android.content.res.Resources;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * Description:
 *
 * @author liuzhenli 2020/10/22
 * Email: 848808263@qq.com
 */

public class GlideCircleWithStokeTransform extends BitmapTransformation {

    private Paint mBorderPaint;
    private float mBorderWidth;
    private int mBorderColor;
    private final String ID = GlideCircleWithStokeTransform.this.getClass().getName();


    public GlideCircleWithStokeTransform(Context context, int borderWidth, int borderColor) {
        super();
        mBorderWidth = Resources.getSystem().getDisplayMetrics().density * borderWidth + 0.5f;
        mBorderColor = borderColor;
        mBorderPaint = new Paint();
        mBorderPaint.setDither(true);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(borderColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorderWidth);
    }


    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        int size = (int) (Math.min(source.getWidth(), source.getHeight()) - (mBorderWidth / 2));
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        // TODO this could be acquired from the pool too
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r - mBorderWidth / 2, paint);
        if (mBorderPaint != null) {
            //除2有一点边线
            float borderRadius = r - mBorderWidth + 0.5f;
            canvas.drawCircle(r, r, borderRadius, mBorderPaint);
        }
        return result;
    }

    public String getId() {
        return getClass().getName() + this.mBorderWidth + "#" + this.mBorderColor;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GlideCircleWithStokeTransform) {
            GlideCircleWithStokeTransform transform = (GlideCircleWithStokeTransform) obj;
            return transform.mBorderColor == this.mBorderColor && transform.mBorderWidth == this.mBorderWidth;
        }
        return false;
    }


}
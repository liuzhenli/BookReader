package com.micoredu.reader.widgets;

import static com.liuzhenli.common.utils.AppUtils.getAssets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.microedu.lib.reader.R;

/**
 * Description:book cover view
 *
 * @author liuzhenli 2022/3/13
 * Email: 848808263@qq.com
 */
public class BookCover extends androidx.appcompat.widget.AppCompatImageView {

    private int width;
    private int height;
    private int nameHeight;
    private int authorHeight;
    boolean loadFailed = true;

    private String bookName, authorName;

    private final Paint bookNamePaint = new Paint();
    private final Paint authorPaint = new Paint();

    public BookCover(@NonNull Context context) {
        this(context, null);
    }

    public BookCover(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookCover(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/xwkt.ttf");
//        bookNamePaint.setTypeface(typeface);
        bookNamePaint.setAntiAlias(true);
        bookNamePaint.setTextAlign(Paint.Align.CENTER);
//        bookNamePaint.setTextSkewX(-0.2f);

//        authorPaint.setTypeface(typeface);
        authorPaint.setAntiAlias(true);
        authorPaint.setTextAlign(Paint.Align.CENTER);
//        authorPaint.setTextSkewX(-0.1f);
        setBackgroundColor(getResources().getColor(R.color.bg_color));
    }

    private void setText(String bookName, String authorName) {
        if (bookName.length() > 1) {
            bookName = bookName.charAt(0) + "";
        }

        if (authorName.length() > 8) {
            authorName = authorName.substring(0, 8) + "...";
        }
        this.bookName = bookName;
        this.authorName = authorName;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();

        bookNamePaint.setTextSize(width / 3f);
        bookNamePaint.setStrokeWidth(bookNamePaint.getTextSize() / 10);

        authorPaint.setTextSize(width / 8f);
        authorPaint.setStrokeWidth(authorPaint.getTextSize() / 10);
        nameHeight = height / 2;
        authorHeight = (int) (nameHeight + authorPaint.getFontSpacing() + 50);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measuredHeight = measuredWidth * 7 / 5;
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (width >= 10 && height > 10) {
            @SuppressLint("DrawAllocation")
            Path path = new Path();
            //四个圆角
            path.moveTo(10f, 0f);
            path.lineTo(width - 10, 0f);
            path.quadTo(width, 0f, width, 10f);
            path.lineTo(width, height - 10);
            path.quadTo(width, height, width - 10, height);
            path.lineTo(10f, height);
            path.quadTo(0f, height, 0f, height - 10);
            path.lineTo(0f, 10f);
            path.quadTo(0f, 0f, 10f, 0f);

            canvas.clipPath(path);
        }
        super.onDraw(canvas);

        if (loadFailed) {
            bookNamePaint.setColor(Color.WHITE);
            bookNamePaint.setStyle(Paint.Style.STROKE);
            canvas.drawText(bookName, width / 2f, nameHeight, bookNamePaint);
            bookNamePaint.setColor(Color.RED);
            bookNamePaint.setStyle(Paint.Style.FILL);
            canvas.drawText(bookName, width / 2f, nameHeight, bookNamePaint);

            authorPaint.setColor(Color.WHITE);
            authorPaint.setStyle(Paint.Style.STROKE);
            canvas.drawText(authorName, width / 2f, authorHeight, authorPaint);
            authorPaint.setColor(Color.RED);
            authorPaint.setStyle(Paint.Style.FILL);
            canvas.drawText(authorName, width / 2f, authorHeight, authorPaint);
        }
    }


    public void setInfo(String bookName, String authorName, String cover) {
        setText(bookName, authorName);
        Glide.with(getContext()).load(cover).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                loadFailed = true;
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                loadFailed = false;
                return false;
            }
        }).into(this);
    }
}

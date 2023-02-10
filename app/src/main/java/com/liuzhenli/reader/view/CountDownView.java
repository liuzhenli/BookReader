package com.liuzhenli.reader.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.micoredu.reader.R;

import io.reactivex.annotations.Nullable;


/**
 * 倒计时view
 *
 * @author liuzhenli
 * @date 2018/1/9
 */

public class CountDownView extends View {

    private Paint mPaintText;
    private Paint mPatinCircle;
    private Paint mPaintRing;
    private float mCircleRadius;
    private float mRingWidth;
    private int mCircleColor;
    private int mRingColor;
    private int mTextColor;
    private int progressStrokeWidth;
    private String mCircleText;
    private OnClickListener onClickListener;
    private long duration;
    private long startTime;
    private boolean mClickSkip = false;

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initVariable();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CountDownView, 0, 0);
        mCircleText = typedArray.getString(R.styleable.CountDownView_centerText);

        float mRadius = typedArray.getDimension(R.styleable.CountDownView_cir_radius, 80);
        mRingWidth = typedArray.getDimension(R.styleable.CountDownView_strokeWidth, 10);
        mCircleColor = typedArray.getColor(R.styleable.CountDownView_circleColor, 0xFFFFFFFF);
        mRingColor = typedArray.getColor(R.styleable.CountDownView_ringColor, 0xFFFFFF00);
        mTextColor = typedArray.getColor(R.styleable.CountDownView_textColor, 0xFF666666);
        progressStrokeWidth = (int) mRingWidth;
        mCircleRadius = mRadius + mRingWidth;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mCircleRadius, mPatinCircle);
        mCircleText = (mCircleText == null || mCircleText.equals("")) ? getResources().getString(R.string.skip) : mCircleText;
        float textWidth = mPaintText.measureText(mCircleText);
        Paint.FontMetrics fontMetrics = mPaintText.getFontMetrics();
        float textHeight = fontMetrics.bottom - fontMetrics.top;
        canvas.drawText(mCircleText, getWidth() / 2 - textWidth / 2, (getHeight() - textHeight) / 2 - fontMetrics.top, mPaintText);
        drawArc(canvas);
    }

    private void drawArc(Canvas canvas) {
        if (duration == 0) {
            return;
        }
        long currentTime = System.currentTimeMillis();
        float factor;
        if (currentTime < startTime) {
            factor = 0;
        } else {
            factor = (currentTime - startTime) / (float) duration;
        }
        if (mClickSkip) {
            factor = 1;
        }
        int curProgress = (int) (factor * 360);
        RectF oval = new RectF(progressStrokeWidth / 2,
                progressStrokeWidth / 2,
                getWidth() - progressStrokeWidth / 2,
                getWidth() - progressStrokeWidth / 2);
        canvas.drawArc(oval, -90, curProgress, false, mPaintRing);
        if (factor < 1) {
            invalidate();
        } else {
            if (onClickListener != null) {
                onClickListener.onClick(this);
            }
        }
    }

    /**
     * 绘制时长
     *
     * @param duration 时长 毫秒
     */
    public void startProgress(long duration) {
        this.duration = duration;
        this.startTime = System.currentTimeMillis();
        postInvalidate();
    }

    private void initVariable() {
        //文字
        mPaintText = new Paint();
        mPaintText.setTextSize(getResources().getDimension(R.dimen.txt_size_12));
        mPaintText.setColor(mTextColor);

        //背景圆
        mPatinCircle = new Paint();
        mPatinCircle.setColor(mCircleColor);

        //环形
        mPaintRing = new Paint();
        mPaintRing.setStyle(Paint.Style.STROKE);
        mPaintRing.setStrokeWidth(mRingWidth);
        mPaintRing.setColor(mRingColor);
        //去掉圆环锯齿
        mPaintRing.setAntiAlias(true);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
        this.onClickListener = l;
    }

    public void setHasClickClip(boolean clickSkip) {
        this.mClickSkip = clickSkip;
    }
}

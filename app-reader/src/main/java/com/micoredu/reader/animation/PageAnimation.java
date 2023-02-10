package com.micoredu.reader.animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import androidx.annotation.NonNull;

import com.micoredu.reader.ReaderLibManager;
import com.microedu.lib.reader.R;
import com.micoredu.reader.utils.ReadConfigManager;
import com.micoredu.reader.utils.ReaderConfig;


/**
 * 翻页动画抽象类
 */

public abstract class PageAnimation {
    //动画速度
    protected int animationSpeed = ReaderConfig.ANIMATION_SPEED;
    //正在使用的View
    protected View mView;
    protected ReadConfigManager readBookControl = ReadConfigManager.getInstance();
    //滑动装置
    protected Scroller mScroller;
    //监听器
    protected OnPageChangeListener mListener;
    //移动方向
    protected Direction mDirection = Direction.NONE;

    //屏幕的尺寸
    protected int mScreenWidth;
    protected int mScreenHeight;
    protected int mMarginTop;
    //视图的尺寸
    protected int mViewWidth;
    protected int mViewHeight;
    //起始点
    protected float mStartX;
    protected float mStartY;
    //触碰点
    protected float mTouchX;
    protected float mTouchY;
    //上一个触碰点
    protected float mLastX;
    protected float mLastY;
    private boolean isMoving = false;
    protected boolean isRunning = false;
    private boolean touchInit = false;
    //是否取消翻页
    protected boolean isCancel = false;
    //可以使用 mLast代替
    protected int mMoveX = 0;
    protected int mMoveY = 0;
    //是否移动了
    protected boolean isMove = false;
    //是否翻阅下一页。true表示翻到下一页，false表示上一页。
    protected boolean isNext = false;
    //是否没下一页或者上一页
    protected boolean noNext = false;

    PageAnimation(int w, int h, View view, OnPageChangeListener listener) {
        this(w, h, 0, 0, 0, view, listener);
    }

    PageAnimation(int w, int h, int marginWidth, int marginTop, int marginBottom, View view, OnPageChangeListener listener) {
        mScreenWidth = w;
        mScreenHeight = h;

        //屏幕的间距
        mMarginTop = marginTop;

        mViewWidth = mScreenWidth - marginWidth * 2;
        mViewHeight = mScreenHeight - mMarginTop - marginBottom;

        mView = view;
        mListener = listener;

        mScroller = new Scroller(mView.getContext(), new LinearInterpolator());
    }

    public Scroller getScroller() {
        return mScroller;
    }

    public void setStartPoint(float x, float y) {
        mStartX = x;
        mStartY = y;

        mLastX = mStartX;
        mLastY = mStartY;
    }

    public void setTouchPoint(float x, float y) {
        mLastX = mTouchX;
        mLastY = mTouchY;

        mTouchX = x;
        mTouchY = y;
    }

    public void setTouchInitFalse() {
        touchInit = false;
    }

    public void initTouch(int x, int y) {
        if (!touchInit) {
            //移动的点击位置
            mMoveX = 0;
            mMoveY = 0;
            //是否移动
            isMove = false;
            //是否存在下一章
            noNext = false;
            //是下一章还是前一章
            isNext = false;
            //是否正在执行动画
            isRunning = false;
            //取消
            isCancel = false;
            //设置起始位置的触摸点
            setStartPoint(x, y);
            touchInit = true;
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    void movingFinish() {
        isMoving = false;
        isRunning = false;
    }

    /**
     * 开启翻页动画
     */
    public void startAnim() {
        isRunning = true;
        isMoving = true;
        mView.invalidate();
    }

    public void setDirection(Direction direction) {
        mDirection = direction;
    }

    public void clear() {
        mView = null;
    }

    /**
     * 点击事件的处理
     */
    public abstract void onTouchEvent(MotionEvent event);

    /**
     * 绘制图形
     */
    public abstract void draw(Canvas canvas);

    /**
     * 滚动动画
     * 必须放在computeScroll()方法中执行
     */
    public void scrollAnim() {
        if (mScroller.computeScrollOffset()) {
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();

            setTouchPoint(x, y);

            mView.postInvalidate();
        } else if (isMoving) {
            if (changePage()) {
                mListener.changePage(mDirection);
                setDirection(PageAnimation.Direction.NONE);
            }
            movingFinish();
        }
    }

    /**
     * 取消动画
     */
    public abstract void abortAnim();

    public abstract boolean changePage();

    /**
     * 获取背景板
     * pageOnCur: 位于当前页的位置, 小于0上一页, 0 当前页, 大于0下一页
     */
    public abstract Bitmap getBgBitmap(int pageOnCur);

    /**
     * 翻页模式
     */
    public enum Mode {
        COVER(ReaderLibManager.getAppResources().getString(R.string.page_mode_COVER)),
        SIMULATION(ReaderLibManager.getAppResources().getString(R.string.page_mode_SIMULATION)),
        SLIDE(ReaderLibManager.getAppResources().getString(R.string.page_mode_SLIDE)),
        SCROLL(ReaderLibManager.getAppResources().getString(R.string.page_mode_SCROLL)),
        NONE(ReaderLibManager.getAppResources().getString(R.string.page_mode_NONE));

        private String name;

        Mode(String name) {
            this.name = name;
        }

        public static Mode getPageMode(int pageMode) {
            switch (pageMode) {
                case ReaderConfig.PageMode.COVER:
                    return COVER;
                case ReaderConfig.PageMode.SIMULATION:
                    return SIMULATION;
                case ReaderConfig.PageMode.SLIDE:
                    return SLIDE;
                case ReaderConfig.PageMode.SCROLL:
                    return SCROLL;
                case ReaderConfig.PageMode.NONE:
                    return NONE;
                default:
                    return COVER;
            }
        }

        public static String[] getAllPageMode() {
            return new String[]{COVER.name, SIMULATION.name, SLIDE.name, SCROLL.name, NONE.name};
        }

        @NonNull
        @Override
        public String toString() {
            return this.name;
        }
    }

    /**
     * 翻页方向
     */
    public enum Direction {
        NONE(true), NEXT(true), PREV(true);

        public final boolean isHorizontal;

        Direction(boolean isHorizontal) {
            this.isHorizontal = isHorizontal;
        }
    }

    public interface OnPageChangeListener {

        void resetScroll();

        boolean hasPrev();

        boolean hasNext(int pageOnCur);

        void drawContent(Canvas canvas, float offset);

        void drawBackground(Canvas canvas);

        void changePage(Direction direction);

    }

}

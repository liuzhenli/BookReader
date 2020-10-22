package com.liuzhenli.reader.view;

import android.content.Context;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

/**
 * 带颜色渐变和缩放的指示器标题
 * 参考:  博客: http://hackware.lucode.net
 * @author  liuzhenli
 */
public class ScaleTransitionPagerTitleView extends ColorTransitionPagerTitleView {
    /**
     * 文字选中和未选中的比例
     */
    private float mMinScale = 0.8f;

    public ScaleTransitionPagerTitleView(Context context) {
        super(context);
    }

    public ScaleTransitionPagerTitleView(Context context, float scale) {
        super(context);
        this.mMinScale = scale;
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        // 实现颜色渐变
        super.onEnter(index, totalCount, enterPercent, leftToRight);
        setScaleX(mMinScale + (1.0f - mMinScale) * enterPercent);
        setScaleY(mMinScale + (1.0f - mMinScale) * enterPercent);
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        // 实现颜色渐变
        super.onLeave(index, totalCount, leavePercent, leftToRight);
        setScaleX(1.0f + (mMinScale - 1.0f) * leavePercent);
        setScaleY(1.0f + (mMinScale - 1.0f) * leavePercent);
    }

    public float getMinScale() {
        return mMinScale;
    }

    public void setMinScale(float minScale) {
        mMinScale = minScale;
    }
}

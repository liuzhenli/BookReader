package com.liuzhenli.common.widget.filter.adapter;

import android.view.View;
import android.widget.FrameLayout;

public interface MenuAdapter {

    /**
     * 设置筛选条目个数
     */
    int getMenuCount();

    /**
     * 设置每个筛选器默认Title
     */
    String getMenuTitle(int position);

    /**
     * 设置每个筛选条目距离底部距离
     */
    int getBottomMargin(int position);


    /**
     * 设置每个筛选条目的View
     */
    View getView(int position, FrameLayout parentContainer);
}

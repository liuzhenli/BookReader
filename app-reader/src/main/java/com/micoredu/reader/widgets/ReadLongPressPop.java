package com.micoredu.reader.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;


import com.liuzhenli.common.utils.DensityUtil;
import com.micoredu.reader.R;
import com.micoredu.reader.helper.ReadConfigManager;

public class ReadLongPressPop extends FrameLayout {


    /**
     * 翻页相关
     */
    FrameLayout flReplace;
    FrameLayout flCp;
    FrameLayout flReplaceAd;
    FrameLayout flSearch;
    FrameLayout flShare;


    private ReadConfigManager readBookControl = ReadConfigManager.getInstance();
    private OnBtnClickListener clickListener;

    public ReadLongPressPop(Context context) {
        super(context);
        init(context);
    }

    public ReadLongPressPop(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ReadLongPressPop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Path path = new Path();
        path.addRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), DensityUtil.dip2px(getContext(), 8), DensityUtil.dip2px(getContext(), 8), Path.Direction.CW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            canvas.clipPath(path);
        } else {
            canvas.clipPath(path, Region.Op.REPLACE);
        }

        super.dispatchDraw(canvas);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.pop_read_long_press, null);
        addView(view);
        flReplace = findViewById(R.id.fl_replace);
        flCp = findViewById(R.id.fl_cp);
        flReplaceAd = findViewById(R.id.fl_replace_ad);
        flSearch = findViewById(R.id.fl_search);
        flShare = findViewById(R.id.fl_share);

        view.setOnClickListener(null);
    }

    public void setListener(@NonNull OnBtnClickListener clickListener) {
        //this.activity = readBookActivity;
        this.clickListener = clickListener;
        initData();
        bindEvent();
    }

    private void initData() {

    }

    private void bindEvent() {

        //复制
        flCp.setOnClickListener(v -> clickListener.copySelect());
        //替换
        flReplace.setOnClickListener(v -> clickListener.replaceSelect());
        //分享
        flShare.setOnClickListener(v -> clickListener.share());
        //标记广告
        flReplaceAd.setOnClickListener(v -> clickListener.replaceSelectAd());
        //搜索
        flSearch.setOnClickListener(v -> clickListener.search());
    }

    public interface OnBtnClickListener {
        void copySelect();

        void search();

        void replaceSelect();

        void replaceSelectAd();

        void share();
    }
}
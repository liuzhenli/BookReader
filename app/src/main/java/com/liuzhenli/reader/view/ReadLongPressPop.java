package com.liuzhenli.reader.view;

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


import com.liuzhenli.reader.utils.DensityUtil;
import com.micoredu.readerlib.helper.ReadConfigManager;
import com.microedu.reader.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadLongPressPop extends FrameLayout {


    /**
     * 翻页相关
     */
    @BindView(R.id.fl_replace)
    FrameLayout flReplace;
    @BindView(R.id.fl_cp)
    FrameLayout flCp;
    @BindView(R.id.fl_replace_ad)
    FrameLayout flReplaceAd;


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
        path.addRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), DensityUtil.dip2px(getContext(), 4), DensityUtil.dip2px(getContext(), 4), Path.Direction.CW);
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
        ButterKnife.bind(this, view);
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

        //标记广告
        flReplaceAd.setOnClickListener(v -> clickListener.replaceSelectAd());
    }

    public interface OnBtnClickListener {
        void copySelect();

        void replaceSelect();

        void replaceSelectAd();

    }
}
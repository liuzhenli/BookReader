package com.liuzhenli.common.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.liuzhenli.common.R;

public class FixHeightBottomSheetDialog extends BottomSheetDialog {

    private View mContentView;

    public FixHeightBottomSheetDialog(@NonNull Context context) {
        super(context);
    }

    public FixHeightBottomSheetDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fixHeight();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        int screenHeight = getScreenHeight(getOwnerActivity());
//        int statusBarHeight = getStatusBarHeight(getContext());
//        int dialogHeight = screenHeight - statusBarHeight;
//        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
    }
    private static int getScreenHeight(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    private static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        this.mContentView = view ;
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        this.mContentView = view;
    }

    private void fixHeight(){
        if(null == mContentView){
            return;
        }
        mContentView.post(() -> {
            //contentView是自定义的显示在BottomSheetDialog上的view
            //R.id.design_bottom_sheet基本是固定的,不用担心后面API的更改
            BottomSheetBehavior behavior=BottomSheetBehavior.from(findViewById(com.google.android.material.R.id.design_bottom_sheet));
            behavior.setHideable(false);//此处设置表示禁止BottomSheetBehavior的执行
        });

        View parent = (View) mContentView.getParent();
        parent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= 16) {
                    parent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    parent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
//                mContentView.measure(0, 0);
                behavior.setPeekHeight(parent.getMeasuredHeight());
                parent.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.transparent));

                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) parent.getLayoutParams();
                params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                parent.setLayoutParams(params);
            }
        });
    }
}
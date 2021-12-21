package com.liuzhenli.common.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RotateDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liuzhenli.common.R;


public class LoadingDialog extends Dialog {

    private TextView mProgress;

    public LoadingDialog(Context context) {
        this(context, R.style.loading_dialog);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public LoadingDialog instance(Activity activity) {
        ViewGroup v = (ViewGroup) View.inflate(activity, R.layout.common_progress_view, null);
        mProgress = v.findViewById(R.id.tv_progress);

        ProgressBar pb = v.findViewById(R.id.common_progress);
        if (pb != null) {
            RotateDrawable rd = (RotateDrawable) pb.getIndeterminateDrawable();
            GradientDrawable gd = (GradientDrawable) rd.getDrawable();
            gd.setColors(new int[]{Color.WHITE, pb.getContext().getResources().getColor(R.color.color_widget_red)});
            gd.setShape(GradientDrawable.RING);
        }
        setContentView(v,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
        );
        return this;
    }

    public void setProgress(String progress) {
        mProgress.setText(progress);
    }
}
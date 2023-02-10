package com.liuzhenli.reader.view.loading;

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

import com.micoredu.reader.R;

public class CustomDialog extends Dialog {

    private TextView mProgress;

    public CustomDialog(Context context) {
        this(context, R.style.loading_dialog);
    }

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public CustomDialog instance(Activity activity) {
        ViewGroup v = (ViewGroup) View.inflate(activity, R.layout.common_progress_view, null);
        mProgress = (TextView) v.findViewById(R.id.tv_progress);

        ProgressBar pb = (ProgressBar) v.findViewById(R.id.common_progress);
        if (pb != null) {

            RotateDrawable rd = (RotateDrawable) pb.getIndeterminateDrawable();
            GradientDrawable gd = (GradientDrawable) rd.getDrawable();
            gd.setColors(new int[]{Color.WHITE, pb.getContext().getResources().getColor(R.color.main)});
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
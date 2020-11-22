package com.liuzhenli.reader.view.filter.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.TextView;

/**
 * description:
 */

public class FilterCheckedTextView extends TextView implements Checkable {
    private boolean mChecked;
    private boolean mCanCheck = true;

    public FilterCheckedTextView(Context context) {
        this(context, null);
    }

    public FilterCheckedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterCheckedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked != mChecked) {
            mChecked = checked;
            refreshDrawableState();
        }
    }

    public void setCanCheck(boolean canCheck) {
        this.mCanCheck = canCheck;
    }

    @Override
    public boolean isChecked() {
        if (mCanCheck) {
            return mChecked;
        }
        return false;
    }

    @Override
    public void toggle() {
        if (mCanCheck) {
            setChecked(!mChecked);
        }
    }

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked() && mCanCheck) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }
}


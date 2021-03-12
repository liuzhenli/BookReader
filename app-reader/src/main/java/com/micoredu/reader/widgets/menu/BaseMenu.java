package com.micoredu.reader.widgets.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Description:设置菜单
 *
 * @author liuzhenli 2020/8/16
 * Email: 848808263@qq.com
 */
public abstract class BaseMenu extends FrameLayout {
    public BaseMenu(@NonNull Context context) {
        this(context, null);
    }

    public BaseMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseMenu(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(null);
        LayoutInflater.from(getContext()).inflate(getLayoutResId(),this);
        init(context, attrs, defStyleAttr);
    }

    protected abstract int getLayoutResId();

    protected abstract void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr);

    protected abstract void changeTheme();
}

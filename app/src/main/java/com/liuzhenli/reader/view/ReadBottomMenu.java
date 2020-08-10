package com.liuzhenli.reader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liuzhenli.common.utils.ClickUtils;
import com.microedu.reader.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description:阅读页底部菜单栏
 *
 * @author liuzhenli 2020/7/27
 * Email: 848808263@qq.com
 */
public class ReadBottomMenu extends FrameLayout {
    @BindView(R.id.tv_menu)
    TextView mTvMenu;

    public ReadBottomMenu(@NonNull Context context) {
        this(context, null);
    }

    public ReadBottomMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReadBottomMenu(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        setOnClickListener(null);
    }


    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_read_menu_bottom, this);
        ButterKnife.bind(this);
        ClickUtils.click(mTvMenu, o -> {
            if (listener != null) {
                listener.onMenuClick();
            }
        });
    }

    private OnElementClickListener listener;

    public void setOnMenuElementClickListener(OnElementClickListener listener) {
        this.listener = listener;
    }

    public interface OnElementClickListener {
        void onMenuClick();
    }
}

package com.liuzhenli.reader.view.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.microedu.reader.R;

import butterknife.BindView;

/**
 * Description:阅读页底部菜单栏
 *
 * @author liuzhenli 2020/7/27
 * Email: 848808263@qq.com
 */
public class ReadTopBarMenu extends BaseMenu {

    @BindView(R.id.toolbar)
    Toolbar mToolBar;
    @BindView(R.id.tv_toolbar_title)
    TextView mTvTitle;
    @BindView(R.id.tv_top_menu_chapter_id)
    TextView mTvChapterTitle;

    public ReadTopBarMenu(@NonNull Context context) {
        this(context, null);
    }

    public ReadTopBarMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReadTopBarMenu(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_read_menu_topbar;
    }

    @Override
    protected void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

    }

    public Toolbar getToolBar() {
        return mToolBar;
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setChapterTitle(String title) {
        mTvChapterTitle.setText(title);
    }

    public void setOnMenuElementClickListener(OnElementClickListener listener) {
    }

    @Override
    protected void changeTheme() {

    }

    public interface OnElementClickListener {
        /**
         * 点击目录
         */
        void onBackClick();
    }
}

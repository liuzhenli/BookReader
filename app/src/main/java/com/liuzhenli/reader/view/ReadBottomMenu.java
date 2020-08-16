package com.liuzhenli.reader.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;
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
    @BindView(R.id.tv_pre_chapter)
    View mVPreChapter;
    @BindView(R.id.tv_next_chapter)
    View mVNextChapter;
    @BindView(R.id.tv_change_night_skin)
    View mVBrightness;
    @BindView(R.id.tv_reader_night_mode)
    TextView mTvNightMode;
    @BindView(R.id.tv_reader_setting)
    TextView mVSetting;
    @BindView(R.id.view_listen_book)
    TextView mVListenBook;
    @BindView(R.id.sb_chapters)
    SeekBar mVChapter;

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

        //菜单
        ClickUtils.click(mTvMenu, o -> {
            if (listener != null) {
                listener.onMenuClick();
            }
        });
        //前一章
        ClickUtils.click(mVPreChapter, o -> {
            if (listener != null) {
                listener.onPreChapterClick();
            }
        });
        //下一章
        ClickUtils.click(mVNextChapter, o -> {
            if (listener != null) {
                listener.onNextChapterClick();
            }
        });
        //亮度
        ClickUtils.click(mVBrightness, o -> {
            if (listener != null) {
                listener.onBrightnessClick();
            }
        });
        //夜间模式
        ClickUtils.click(mTvNightMode, o -> {
            if (listener != null) {
                listener.onNightModeClick();
            }
        });
        //设置
        ClickUtils.click(mVSetting, o -> {
            if (listener != null) {
                listener.onSettingClick();
            }
        });
        //听书
        ClickUtils.click(mVListenBook, o -> {
            if (listener != null) {
                listener.onListenBookClick();
            }
        });
        mVChapter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (listener != null) {
                    listener.onChapterProgressed(progress, false);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (listener != null) {
                    listener.onChapterProgressed(seekBar.getProgress(), true);
                }
            }
        });
    }

    private OnElementClickListener listener;

    public void setOnMenuElementClickListener(OnElementClickListener listener) {
        this.listener = listener;
    }

    public interface OnElementClickListener {
        /**
         * 点击目录
         */
        void onMenuClick();

        /**
         * 点击上一章
         */
        void onPreChapterClick();

        /**
         * 点击下一章
         */
        void onNextChapterClick();

        /**
         * 亮度
         */
        void onBrightnessClick();

        /**
         * 夜间模式
         */
        void onNightModeClick();

        /**
         * 设置
         */
        void onSettingClick();

        /**
         * 听书
         */
        void onListenBookClick();

        /**
         * 拖动章节
         */
        void onChapterProgressed(int progress, boolean isStop);
    }
}

package com.liuzhenli.reader.view.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liuzhenli.common.utils.ClickUtils;
import com.micoredu.readerlib.helper.ReadConfigManager;
import com.microedu.reader.R;

/**
 * Description:阅读页底部菜单栏
 *
 * @author liuzhenli 2020/7/27
 * Email: 848808263@qq.com
 */
public class ReadBottomMenu extends BaseMenu {

    TextView mTvMenu;
    View mVPreChapter;
    View mVNextChapter;
    View mVBrightness;
    TextView mTvNightMode;
    TextView mVSetting;
    TextView mVListenBook;
    SeekBar mVChapter;
    private OnElementClickListener listener;

    public ReadBottomMenu(@NonNull Context context) {
        this(context, null);
    }

    public ReadBottomMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReadBottomMenu(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_read_menu_bottom;
    }

    @Override
    protected void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        mTvMenu = findViewById(R.id.tv_menu);
        mVPreChapter = findViewById(R.id.tv_pre_chapter);
        mVNextChapter = findViewById(R.id.tv_next_chapter);
        mVBrightness = findViewById(R.id.tv_change_night_skin);
        mTvNightMode = findViewById(R.id.tv_reader_night_mode);
        mVSetting = findViewById(R.id.tv_reader_setting);
        mVListenBook = findViewById(R.id.view_listen_book);
        mVChapter = findViewById(R.id.sb_chapters);


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


    public void setOnMenuElementClickListener(OnElementClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void changeTheme() {
        if (ReadConfigManager.getInstance().getIsNightTheme()) {
            mTvNightMode.setText("日间模式");
        } else {
            mTvNightMode.setText("夜间模式");
        }
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

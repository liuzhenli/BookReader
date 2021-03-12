package com.micoredu.reader.widgets.menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.common.utils.FillContentUtil;
import com.liuzhenli.common.utils.ToastUtil;
import com.micoredu.reader.R;
import com.micoredu.reader.helper.ReadConfigManager;
import com.micoredu.reader.utils.ReaderConfig;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.text.DecimalFormat;


/**
 * Description:设置菜单  文字大小,间距,背景,翻页
 *
 * @author liuzhenli 2020/8/16
 * Email: 848808263@qq.com
 */
public class ReadSettingMenu extends BaseMenu {
    View mViewRoot;
    TextView tvPreChapter;
    /***缩小字体*/
    QMUIRoundButton tvMenuTextMin;
    TextView tvSettingMenuTextSize;
    QMUIRoundButton tvMenuTextEnlarge;
    QMUIRoundButton tvMenuTextSimple;
    QMUIRoundButton tvMenuTextStyle;
    QMUIRoundButton tvMenuTextTypeFace;
    TextView tvSettingLetterSpace;
    TextView tvSettingTextBackground;
    RadioGroup rbPageBg;

    TextView tvSettingPageMode;
    /****仿真*/
    QMUIRoundButton tvSettingPageModePaper;
    /****覆盖*/
    QMUIRoundButton tvSettingPageModeCover;
    /***滑动*/
    QMUIRoundButton tvSettingPageModeSlide;
    /***上下*/
    QMUIRoundButton tvSettingPageModeVertical;
    /***无动画*/
    QMUIRoundButton tvSettingPageModeEmpty;
    QMUIRoundButton tvSettingReset;

    QMUIRoundButton mHSpaceMin;
    TextView mHSpaceSize;
    QMUIRoundButton mHSpaceEnlarge;

    QMUIRoundButton mVSpaceMin;
    TextView mVSpaceSize;
    QMUIRoundButton mVSpaceEnlarge;

    private DecimalFormat decimalFormat = new DecimalFormat(".#");


    private ReadSettingCallBack callBack;

    public ReadSettingMenu(@NonNull Context context) {
        this(context, null);
    }

    public ReadSettingMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReadSettingMenu(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_read_menu_setting;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        mViewRoot = findViewById(R.id.view_setting_menu_root);
        tvPreChapter = findViewById(R.id.tv_pre_chapter);
        /***缩小字体*/
        tvMenuTextMin = findViewById(R.id.tv_menu_text_min);
        tvSettingMenuTextSize = findViewById(R.id.tv_setting_menu_text_size);
        tvMenuTextEnlarge = findViewById(R.id.tv_menu_text_enlarge);
        tvMenuTextSimple = findViewById(R.id.tv_menu_text_simple);
        tvMenuTextStyle = findViewById(R.id.tv_menu_text_style);
        tvMenuTextTypeFace = findViewById(R.id.tv_menu_text_type_face);
        tvSettingLetterSpace = findViewById(R.id.tv_setting_letter_space);
        tvSettingTextBackground = findViewById(R.id.tv_setting_text_background);
        rbPageBg = findViewById(R.id.rb_menu_setting_bg);

        tvSettingPageMode = findViewById(R.id.tv_setting_page_mode);
        tvSettingPageModePaper = findViewById(R.id.tv_setting_page_mode_paper);
        tvSettingPageModeCover = findViewById(R.id.tv_setting_page_mode_cover);
        tvSettingPageModeSlide = findViewById(R.id.tv_setting_page_mode_slide);
        tvSettingPageModeVertical = findViewById(R.id.tv_setting_page_mode_vertical);
        tvSettingPageModeEmpty = findViewById(R.id.tv_setting_page_mode_empty);
        tvSettingReset = findViewById(R.id.tv_setting_reset);
        mHSpaceMin = findViewById(R.id.tv_menu_h_space_min);
        mHSpaceSize = findViewById(R.id.tv_setting_menu_h_space_size);
        mHSpaceEnlarge = findViewById(R.id.tv_menu_h_space_enlarge);

        mVSpaceMin = findViewById(R.id.tv_menu_v_space_min);
        mVSpaceSize = findViewById(R.id.tv_setting_menu_v_space_size);
        QMUIRoundButton mVSpaceEnlarge = findViewById(R.id.tv_menu_v_space_enlarge);


        fillDefaultContent();
        //仿真模式
        ClickUtils.click(tvSettingPageModePaper, o -> setPageMode(ReaderConfig.PageMode.SIMULATION));
        ClickUtils.click(tvSettingPageModeCover, o -> setPageMode(ReaderConfig.PageMode.COVER));
        ClickUtils.click(tvSettingPageModeVertical, o -> setPageMode(ReaderConfig.PageMode.SCROLL));
        ClickUtils.click(tvSettingPageModeSlide, o -> setPageMode(ReaderConfig.PageMode.SLIDE));
        ClickUtils.click(tvSettingPageModeEmpty, o -> setPageMode(ReaderConfig.PageMode.NONE));

        //减小字号
        ClickUtils.click(tvMenuTextMin, o -> {
            if (callBack != null) {
                int textSize = ReadConfigManager.getInstance().getTextSize();
                if (textSize > ReaderConfig.TextSize.SIZE_MIN) {
                    textSize -= 2;
                    ReadConfigManager.getInstance().setTextSize(textSize);
                    FillContentUtil.setNumberText(tvSettingMenuTextSize, textSize);
                    callBack.onTextStyleChanged();
                } else {
                    ToastUtil.showToast("已经是最小字体了~~");
                }
            }
        });

        //增大字号
        ClickUtils.click(tvMenuTextEnlarge, o -> {
            if (callBack != null) {
                int textSize = ReadConfigManager.getInstance().getTextSize();
                if (textSize < ReaderConfig.TextSize.SIZE_MAX) {
                    textSize += 2;
                    FillContentUtil.setNumberText(tvSettingMenuTextSize, textSize);
                    ReadConfigManager.getInstance().setTextSize(textSize);
                    callBack.onTextStyleChanged();
                } else {
                    ToastUtil.showToast("已经是最大字体了~~");
                }
            }
        });

        //字体简体--繁体
        ClickUtils.click(tvMenuTextSimple, o -> {
            if (ReadConfigManager.getInstance().getTextConvert() != ReaderConfig.CNText.CN_TRADITION) {
                ReadConfigManager.getInstance().setTextConvert(ReaderConfig.CNText.CN_TRADITION);
            } else {
                ReadConfigManager.getInstance().setTextConvert(ReaderConfig.CNText.CN_SIMPLE);
            }
            setCnText();
            if (callBack != null) {
                callBack.onTextStyleChanged();
            }
        });
        //字体加粗,常态
        ClickUtils.click(tvMenuTextStyle, o -> {
            ReadConfigManager.getInstance().setTextBold(!ReadConfigManager.getInstance().getTextBold());
            if (callBack != null) {
                callBack.onTextStyleChanged();
            }
            setTextBold();
        });
        //设置字体
        ClickUtils.click(tvMenuTextTypeFace, o -> {
            if (callBack != null) {
                callBack.onTypeFaceClicked();
            }
        });

        //文字间距
        ClickUtils.click(mHSpaceMin, o -> {
            float lineMultiplier = ReadConfigManager.getInstance().getTextLetterSpacing();
            int tem = (int) (100 * lineMultiplier);
            if (tem <= 0) {
                ToastUtil.showToast("不能再小了哦");
                return;
            }
            tem -= 5;
            ReadConfigManager.getInstance().setTextLetterSpacing(tem / 100.f);
            mHSpaceSize.setText(tem / 100.f + "");
            if (callBack != null) {
                callBack.onTextStyleChanged();
            }
        });
        //文字间距
        ClickUtils.click(mHSpaceEnlarge, o -> {
            float lineMultiplier = ReadConfigManager.getInstance().getTextLetterSpacing();
            int tem = (int) (100 * lineMultiplier);
            tem += 5;
            ReadConfigManager.getInstance().setTextLetterSpacing(tem / 100.f);
            mHSpaceSize.setText(tem / 100.f + "");
            if (callBack != null) {
                callBack.onTextStyleChanged();
            }
        });

        //设置行间距
        ClickUtils.click(mVSpaceMin, o -> {
            float lineMultiplier = ReadConfigManager.getInstance().getLineMultiplier();
            int tem = (int) (10 * lineMultiplier);
            if (tem <= 0) {
                ToastUtil.showToast("不能再小了哦");
                return;
            }
            tem -= 2;
            ReadConfigManager.getInstance().setLineMultiplier(tem / 10.f);
            mVSpaceSize.setText(tem / 10.f + "");
            if (callBack != null) {
                callBack.onTextStyleChanged();
            }
        });
        //设置行间距
        ClickUtils.click(mVSpaceEnlarge, o -> {
            float lineMultiplier = ReadConfigManager.getInstance().getLineMultiplier();
            int tem = (int) (10 * lineMultiplier);
            tem += 1;
            ReadConfigManager.getInstance().setLineMultiplier(tem / 10.f);
            mVSpaceSize.setText(tem / 10.f + "");
            if (callBack != null) {
                callBack.onTextStyleChanged();
            }
        });


        //设置背景
        rbPageBg.setOnCheckedChangeListener((group, checkedId) -> {
            setPageBackGround(checkedId);
            if (callBack != null) {
                callBack.onBackGroundChanged();
            }
        });
    }

    /**
     * 默认菜单选项设置
     */
    @SuppressLint("SetTextI18n")
    private void fillDefaultContent() {
        FillContentUtil.setNumberText(tvSettingMenuTextSize, ReadConfigManager.getInstance().getTextSize());
        setCnText();
        setTextBold();
        setPageMode(ReadConfigManager.getInstance().getPageMode());
        mHSpaceSize.setText(ReadConfigManager.getInstance().getTextLetterSpacing() + "");
        mVSpaceSize.setText(ReadConfigManager.getInstance().getLineMultiplier() + "");
        tvSettingReset.setVisibility(GONE);
    }

    /***繁体,简体*/
    private void setCnText() {
        if (ReadConfigManager.getInstance().getTextConvert() == ReaderConfig.CNText.CN_TRADITION) {
            tvMenuTextSimple.setText("简体");
        } else {
            tvMenuTextSimple.setText("繁体");
        }
    }

    static final int[] CHECKED_STATE_SET = new int[]{android.R.attr.state_checked};
    static final int[] SELECTED_STATE_SET = new int[]{android.R.attr.state_selected};
    static final int[] NOT_PRESSED_OR_FOCUSED_STATE_SET = new int[]{-android.R.attr.state_pressed, -android.R.attr.state_focused};

    private void setPageBackGround(int checkedId) {
        final int[][] states = new int[2][];
        final int[] color = new int[2];
        states[0] = NOT_PRESSED_OR_FOCUSED_STATE_SET;
        states[1] = CHECKED_STATE_SET;
        color[0] = R.color.text_color_99;
        color[1] = R.color.main;
        //0 白天
        if (checkedId == R.id.tv_menu_background_0) {
            ReadConfigManager.getInstance().setIsNightTheme(false);
            ReadConfigManager.getInstance().setTextDrawableIndex(ReaderConfig.PageBgColor.BG_COLOR_DAY);
            //1 黄色
        } else if (checkedId == R.id.tv_menu_background_1) {
            ReadConfigManager.getInstance().setIsNightTheme(false);
            ReadConfigManager.getInstance().setTextDrawableIndex(ReaderConfig.PageBgColor.BG_COLOR_YELLOW);
            //2 绿色
        } else if (checkedId == R.id.tv_menu_background_2) {
            ReadConfigManager.getInstance().setIsNightTheme(false);
            ReadConfigManager.getInstance().setTextDrawableIndex(ReaderConfig.PageBgColor.BG_COLOR_GREEN);
            //3 粉色
        } else if (checkedId == R.id.tv_menu_background_3) {
            ReadConfigManager.getInstance().setIsNightTheme(false);
            ReadConfigManager.getInstance().setTextDrawableIndex(ReaderConfig.PageBgColor.BG_COLOR_PINK);
            //4 深蓝色
        } else if (checkedId == R.id.tv_menu_background_4) {
            ReadConfigManager.getInstance().setIsNightTheme(false);
            ReadConfigManager.getInstance().setTextDrawableIndex(ReaderConfig.PageBgColor.BG_COLOR_SBLUE);
            //5 蓝色
        } else if (checkedId == R.id.tv_menu_background_5) {
            ReadConfigManager.getInstance().setIsNightTheme(false);
            ReadConfigManager.getInstance().setTextDrawableIndex(ReaderConfig.PageBgColor.BG_COLOR_BLUE);
            //6 夜间  页面模式是一种皮肤,也需要一种开关
        } else if (checkedId == R.id.tv_menu_background_6) {
            ReadConfigManager.getInstance().setIsNightTheme(true);
            ReadConfigManager.getInstance().setTextDrawableIndex(ReaderConfig.PageBgColor.BG_COLOR_NIGHT);
        }
        rbPageBg.check(checkedId);
    }

    private int getColor(int resId) {
        return getContext().getResources().getColor(resId);
    }

    private void setTextBold() {
        if (ReadConfigManager.getInstance().getTextBold()) {
            tvMenuTextStyle.setSelected(true);
        } else {
            tvMenuTextStyle.setSelected(false);
        }
    }

    /***翻页模式*/
    private void setPageMode(int mode) {
        tvSettingPageModePaper.setSelected(false);
        tvSettingPageModeCover.setSelected(false);
        tvSettingPageModeVertical.setSelected(false);
        tvSettingPageModeSlide.setSelected(false);
        tvSettingPageModeEmpty.setSelected(false);
        switch (mode) {
            case ReaderConfig.PageMode.SIMULATION:
                tvSettingPageModePaper.setSelected(true);
                break;
            case ReaderConfig.PageMode.COVER:
                tvSettingPageModeCover.setSelected(true);
                break;
            case ReaderConfig.PageMode.SLIDE:
                tvSettingPageModeSlide.setSelected(true);
                break;
            default:
                tvSettingPageModeEmpty.setSelected(true);
                break;
        }
        ReadConfigManager.getInstance().setPageMode(mode);
        if (callBack != null) {
            callBack.onPageAnimChanged();
        }
    }

    @Override
    protected void changeTheme() {

    }


    public void setReadSettingCallBack(ReadSettingCallBack callBack) {
        this.callBack = callBack;
    }

    public interface ReadSettingCallBack {
        /***翻页模式 */
        void onPageAnimChanged();

        /*** 字体大小  简体--繁体  行间距*/
        void onTextStyleChanged();

        /***点击字体切换了*/
        void onTypeFaceClicked();

        /***background change*/
        void onBackGroundChanged();
    }
}

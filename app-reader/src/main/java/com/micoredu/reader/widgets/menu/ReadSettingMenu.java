package com.micoredu.reader.widgets.menu;

import static com.micoredu.reader.constant.PageAnim.coverPageAnim;
import static com.micoredu.reader.constant.PageAnim.scrollPageAnim;
import static com.micoredu.reader.constant.PageAnim.simulationPageAnim;
import static com.micoredu.reader.constant.PageAnim.slidePageAnim;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liuzhenli.common.utils.AppConfig;
import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.common.utils.FillContentUtil;
import com.liuzhenli.common.utils.ToastUtil;
import com.micoredu.reader.help.config.ReadBookConfig;
import com.microedu.lib.reader.R;
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

                int textSize = ReadBookConfig.INSTANCE.getTextSize();
                if (textSize > ReaderConfig.TextSize.SIZE_MIN) {
                    textSize -= 2;
                    ReadBookConfig.INSTANCE.setTextSize(textSize);
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
                int textSize = ReadBookConfig.INSTANCE.getTextSize();
                if (textSize < ReaderConfig.TextSize.SIZE_MAX) {
                    textSize += 2;
                    FillContentUtil.setNumberText(tvSettingMenuTextSize, textSize);
                    ReadBookConfig.INSTANCE.setTextSize(textSize);
                    callBack.onTextStyleChanged();
                } else {
                    ToastUtil.showToast("已经是最大字体了~~");
                }
            }
        });

        //字体简体--繁体
        ClickUtils.click(tvMenuTextSimple, o -> {

            if (AppConfig.INSTANCE.getChineseConverterType() != ReaderConfig.CNText.CN_TRADITION) {
                AppConfig.INSTANCE.setChineseConverterType(ReaderConfig.CNText.CN_TRADITION);
            } else {
                AppConfig.INSTANCE.setChineseConverterType(ReaderConfig.CNText.CN_SIMPLE);
            }
            setCnText();
            if (callBack != null) {
                callBack.onTextStyleChanged();
            }
        });
        //字体加粗,常态
        ClickUtils.click(tvMenuTextStyle, o -> {
            if (ReadBookConfig.INSTANCE.getTextBold() == ReaderConfig.TextType.NORMAL) {
                ReadBookConfig.INSTANCE.setTextBold(ReaderConfig.TextType.BOLD);
            } else {
                ReadBookConfig.INSTANCE.setTextBold(ReaderConfig.TextType.NORMAL);
            }
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
            float lineMultiplier = ReadBookConfig.INSTANCE.getLetterSpacing();
            int tem = (int) (100 * lineMultiplier);
            if (tem <= 0) {
                ToastUtil.showToast("不能再小了哦");
                return;
            }
            tem -= 5;
            ReadBookConfig.INSTANCE.setLetterSpacing(tem / 100.f);
            mHSpaceSize.setText(tem / 100.f + "");
            if (callBack != null) {
                callBack.onTextStyleChanged();
            }
        });
        //文字间距
        ClickUtils.click(mHSpaceEnlarge, o -> {
            float lineMultiplier = ReadBookConfig.INSTANCE.getLetterSpacing();
            int tem = (int) (100 * lineMultiplier);
            tem += 5;
            ReadBookConfig.INSTANCE.setLetterSpacing(tem / 100.f);
            mHSpaceSize.setText(tem / 100.f + "");
            if (callBack != null) {
                callBack.onTextStyleChanged();
            }
        });

        //设置行间距
        ClickUtils.click(mVSpaceMin, o -> {
            float lineMultiplier = ReadBookConfig.INSTANCE.getLineSpacingExtra();
            int tem = (int) (lineMultiplier);
            if (tem <= 0) {
                ToastUtil.showToast("不能再小了哦");
                return;
            }
            tem -= 2;
            ReadBookConfig.INSTANCE.setLineSpacingExtra(tem);
            mVSpaceSize.setText(tem + "");
            if (callBack != null) {
                callBack.onTextStyleChanged();
            }
        });
        //设置行间距
        ClickUtils.click(mVSpaceEnlarge, o -> {
            float lineMultiplier = ReadBookConfig.INSTANCE.getLineSpacingExtra();
            int tem = (int) (lineMultiplier);
            tem += 1;
            ReadBookConfig.INSTANCE.setLineSpacingExtra(tem);
            mVSpaceSize.setText(tem + "");
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
        FillContentUtil.setNumberText(tvSettingMenuTextSize, ReadBookConfig.INSTANCE.getTextSize());
        setCnText();
        setTextBold();
        setPageMode(ReadBookConfig.INSTANCE.getPageAnim());
        mHSpaceSize.setText(ReadBookConfig.INSTANCE.getLetterSpacing() + "");
        mVSpaceSize.setText(ReadBookConfig.INSTANCE.getLineSpacingExtra() + "");
        tvSettingReset.setVisibility(GONE);
    }

    /***繁体,简体*/
    private void setCnText() {
        if (AppConfig.INSTANCE.getChineseConverterType() == ReaderConfig.CNText.CN_TRADITION) {
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
            ReadBookConfig.INSTANCE.getDurConfig().setCurBg(0, "#ffffff");
            ReadBookConfig.INSTANCE.getDurConfig().setCurTextColor(Color.parseColor("#4a4a4a"));
            //1 黄色
        } else if (checkedId == R.id.tv_menu_background_1) {
            ReadBookConfig.INSTANCE.getDurConfig().setCurBg(0, "#ffFBE6B5");
            ReadBookConfig.INSTANCE.getDurConfig().setCurTextColor(Color.parseColor("#6A482E"));
            //2 绿色
        } else if (checkedId == R.id.tv_menu_background_2) {
            ReadBookConfig.INSTANCE.getDurConfig().setCurBg(0, "#D5E9D4");
            ReadBookConfig.INSTANCE.getDurConfig().setCurTextColor(Color.parseColor("#425340"));
            //3 粉色
        } else if (checkedId == R.id.tv_menu_background_3) {
            ReadBookConfig.INSTANCE.getDurConfig().setCurBg(0, "#FFE1DC");
            ReadBookConfig.INSTANCE.getDurConfig().setCurTextColor(Color.parseColor("#814156"));
            //4 深蓝色
        } else if (checkedId == R.id.tv_menu_background_4) {
            ReadBookConfig.INSTANCE.getDurConfig().setCurBg(0, "#28334c");
            ReadBookConfig.INSTANCE.getDurConfig().setCurTextColor(Color.parseColor("#607799"));
            //5 蓝色
        } else if (checkedId == R.id.tv_menu_background_5) {
            ReadBookConfig.INSTANCE.getDurConfig().setCurBg(0, "#CEE7FF");
            ReadBookConfig.INSTANCE.getDurConfig().setCurTextColor(Color.parseColor("#52648A"));
            //6 夜间  页面模式是一种皮肤,也需要一种开关
        } else if (checkedId == R.id.tv_menu_background_6) {
            ReadBookConfig.INSTANCE.getDurConfig().setCurBg(2, "#292929");
            ReadBookConfig.INSTANCE.getDurConfig().setCurTextColor(Color.parseColor("#959595"));
        }
        rbPageBg.check(checkedId);
    }

    private int getColor(int resId) {
        return getContext().getResources().getColor(resId);
    }

    private void setTextBold() {
        if (ReadBookConfig.INSTANCE.getTextBold() == ReaderConfig.TextType.BOLD) {
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
                ReadBookConfig.INSTANCE.setPageAnim(simulationPageAnim);
                tvSettingPageModePaper.setSelected(true);
                break;
            case ReaderConfig.PageMode.COVER:
                tvSettingPageModeCover.setSelected(true);
                ReadBookConfig.INSTANCE.setPageAnim(coverPageAnim);
                break;
            case ReaderConfig.PageMode.SLIDE:
                tvSettingPageModeSlide.setSelected(true);
                ReadBookConfig.INSTANCE.setPageAnim(slidePageAnim);
                break;

            case ReaderConfig.PageMode.SCROLL:
                tvSettingPageModeVertical.setSelected(true);
                ReadBookConfig.INSTANCE.setPageAnim(scrollPageAnim);
                break;
            default:
                tvSettingPageModeEmpty.setSelected(true);
                ReadBookConfig.INSTANCE.setPageAnim(coverPageAnim);
                break;
        }
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

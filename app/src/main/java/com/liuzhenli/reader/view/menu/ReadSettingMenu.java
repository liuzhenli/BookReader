package com.liuzhenli.reader.view.menu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.common.utils.FillContentUtil;
import com.liuzhenli.reader.utils.ToastUtil;
import com.micoredu.readerlib.helper.ReadBookControl;
import com.micoredu.readerlib.utils.ReaderConfig;
import com.microedu.reader.R;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import butterknife.BindView;

/**
 * Description:设置菜单
 *
 * @author liuzhenli 2020/8/16
 * Email: 848808263@qq.com
 */
public class ReadSettingMenu extends BaseMenu {
    @BindView(R.id.tv_pre_chapter)
    TextView tvPreChapter;
    /***缩小字体*/
    @BindView(R.id.tv_menu_text_min)
    QMUIRoundButton tvMenuTextMin;
    @BindView(R.id.tv_setting_menu_text_size)
    TextView tvSettingMenuTextSize;
    @BindView(R.id.tv_menu_text_enlarge)
    QMUIRoundButton tvMenuTextEnlarge;
    @BindView(R.id.tv_menu_text_simple)
    QMUIRoundButton tvMenuTextSimple;
    @BindView(R.id.tv_menu_text_style)
    QMUIRoundButton tvMenuTextStyle;
    @BindView(R.id.tv_menu_text_type_face)
    QMUIRoundButton tvMenuTextTypeFace;
    @BindView(R.id.tv_setting_letter_space)
    TextView tvSettingLetterSpace;
    /***行距*/
    @BindView(R.id.rg_menu_text_space)
    RadioGroup rbTextSpace;

    RadioButton tvMenuTextSpaceDefault;
    @BindView(R.id.tv_menu_text_space_more)
    QMUIRoundButton tvMenuTextSpaceMore;
    @BindView(R.id.tv_setting_text_background)
    TextView tvSettingTextBackground;
    @BindView(R.id.tv_menu_background_1)
    QMUIRoundButton tvMenuBackground1;
    @BindView(R.id.tv_menu_background_2)
    QMUIRoundButton tvMenuBackground2;
    @BindView(R.id.tv_menu_background_3)
    QMUIRoundButton tvMenuBackground3;
    @BindView(R.id.tv_menu_background_4)
    QMUIRoundButton tvMenuBackground4;
    @BindView(R.id.tv_menu_background_5)
    QMUIRoundButton tvMenuBackground5;
    @BindView(R.id.tv_menu_background_more)
    QMUIRoundButton tvMenuBackgroundMore;
    @BindView(R.id.tv_setting_page_mode)
    TextView tvSettingPageMode;
    /****仿真*/
    @BindView(R.id.tv_setting_page_mode_paper)
    QMUIRoundButton tvSettingPageModePaper;
    /****覆盖*/
    @BindView(R.id.tv_setting_page_mode_cover)
    QMUIRoundButton tvSettingPageModeCover;
    /***滑动*/
    @BindView(R.id.tv_setting_page_mode_slide)
    QMUIRoundButton tvSettingPageModeSlide;
    /***上下*/
    @BindView(R.id.tv_setting_page_mode_vertical)
    QMUIRoundButton tvSettingPageModeVertical;
    /***无动画*/
    @BindView(R.id.tv_setting_page_mode_empty)
    QMUIRoundButton tvSettingPageModeEmpty;
    @BindView(R.id.tv_setting_reset)
    QMUIRoundButton tvSettingReset;

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
                int textSize = ReadBookControl.getInstance().getTextSize();
                if (textSize > ReaderConfig.TextSize.SIZE_MIN) {
                    textSize -= 2;
                    ReadBookControl.getInstance().setTextSize(textSize);
                    FillContentUtil.setNumberText(tvSettingMenuTextSize, textSize);
                    callBack.onTextStyleChanged();
                } else {
                    ToastUtil.showCenter("已经是最小字体了~~");
                }
            }
        });

        //增大字号
        ClickUtils.click(tvMenuTextEnlarge, o -> {
            if (callBack != null) {
                int textSize = ReadBookControl.getInstance().getTextSize();
                if (textSize < ReaderConfig.TextSize.SIZE_MAX) {
                    textSize += 2;
                    FillContentUtil.setNumberText(tvSettingMenuTextSize, textSize);
                    ReadBookControl.getInstance().setTextSize(textSize);
                    callBack.onTextStyleChanged();
                } else {
                    ToastUtil.showCenter("已经是最大字体了~~");
                }
            }
        });

        //字体简体--繁体
        ClickUtils.click(tvMenuTextSimple, o -> {
            if (ReadBookControl.getInstance().getTextConvert() != ReaderConfig.CNText.CN_TRADITION) {
                ReadBookControl.getInstance().setTextConvert(ReaderConfig.CNText.CN_TRADITION);
            } else {
                ReadBookControl.getInstance().setTextConvert(ReaderConfig.CNText.CN_SIMPLE);
            }
            setCnText();
            if (callBack != null) {
                callBack.onTextStyleChanged();
            }
        });
        //字体加粗,常态
        ClickUtils.click(tvMenuTextStyle, o -> {
            ReadBookControl.getInstance().setTextBold(!ReadBookControl.getInstance().getTextBold());
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
        rbTextSpace.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setLineSpace(checkedId);
                if (callBack != null) {
                    callBack.onTextStyleChanged();
                }
            }
        });
    }

    /**
     * 默认菜单选项设置
     */
    private void fillDefaultContent() {
        FillContentUtil.setNumberText(tvSettingMenuTextSize, ReadBookControl.getInstance().getTextSize());
        setCnText();
        setTextBold();
        setPageMode(ReadBookControl.getInstance().getPageMode());
        setLineSpace(ReadBookControl.getInstance().getLineMultiplier());
    }

    /***繁体,简体*/
    private void setCnText() {
        if (ReadBookControl.getInstance().getTextConvert() == ReaderConfig.CNText.CN_TRADITION) {
            tvMenuTextSimple.setText("简体");
        } else {
            tvMenuTextSimple.setText("繁体");
        }
    }

    private void setLineSpace(float lineMultiplier) {
        if (lineMultiplier == 1.0) {
            setLineSpace(R.id.tv_menu_text_space_4);
        } else if (lineMultiplier == 2.0) {
            setLineSpace(R.id.tv_menu_text_space_3);
        } else if (lineMultiplier == 3.0) {
            setLineSpace(R.id.tv_menu_text_space_2);
        } else if (lineMultiplier == 0.5f) {
            setLineSpace(R.id.tv_menu_text_space_5);
        }

    }

    private void setLineSpace(int checkedId) {
        switch (checkedId) {
            case R.id.tv_menu_text_space_4:
                ReadBookControl.getInstance().setLineMultiplier(1.0f);
                break;
            case R.id.tv_menu_text_space_3:
                ReadBookControl.getInstance().setLineMultiplier(2);
                break;
            case R.id.tv_menu_text_space_2:
                ReadBookControl.getInstance().setLineMultiplier(3);
                break;
            case R.id.tv_menu_text_space_5:
                ReadBookControl.getInstance().setLineMultiplier(0.5f);
                break;
            default:
                break;
        }
        rbTextSpace.check(checkedId);
    }

    private void setTextBold() {
        if (ReadBookControl.getInstance().getTextBold()) {
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
        ReadBookControl.getInstance().setPageMode(mode);
        if (callBack != null) {
            callBack.onPageAnimChanged();
        }
    }

    @Override
    protected void changeTheme() {

    }

    private ReadSettingCallBack callBack;

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

        /***背景*/
        void onBackGroundClicked();
    }
}

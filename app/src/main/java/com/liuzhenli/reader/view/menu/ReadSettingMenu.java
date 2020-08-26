package com.liuzhenli.reader.view.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liuzhenli.common.utils.ClickUtils;
import com.liuzhenli.reader.ui.contract.ReadContract;
import com.micoredu.readerlib.animation.PageAnimation;
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
    @BindView(R.id.tv_menu_text_space_4)
    QMUIRoundButton tvMenuTextSpace4;
    @BindView(R.id.tv_menu_text_space_3)
    QMUIRoundButton tvMenuTextSpace3;
    @BindView(R.id.tv_menu_text_space_2)
    QMUIRoundButton tvMenuTextSpace2;
    @BindView(R.id.tv_menu_text_space_default)
    QMUIRoundButton tvMenuTextSpaceDefault;
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

    @Override
    protected void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        //仿真模式
        ClickUtils.click(tvSettingPageModePaper, o -> setPageMode(ReaderConfig.PageMode.SIMULATION));
        ClickUtils.click(tvSettingPageModeCover, o -> setPageMode(ReaderConfig.PageMode.COVER));
        ClickUtils.click(tvSettingPageModeVertical, o -> setPageMode(ReaderConfig.PageMode.SCROLL));
        ClickUtils.click(tvSettingPageModeSlide, o -> setPageMode(ReaderConfig.PageMode.SLIDE));
        ClickUtils.click(tvSettingPageModeEmpty, o -> setPageMode(ReaderConfig.PageMode.NONE));

    }

    private void setPageMode(int mode) {
        ReadBookControl.getInstance().setPageMode(mode);
        if (callBack != null) {
            callBack.onPageModeChange();
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
        void onPageModeChange();
    }
}

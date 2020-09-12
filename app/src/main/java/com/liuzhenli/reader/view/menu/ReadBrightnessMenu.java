package com.liuzhenli.reader.view.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liuzhenli.common.utils.ClickUtils;
import com.microedu.reader.R;
import com.qmuiteam.qmui.widget.QMUISlider;

import butterknife.BindView;

/**
 * Description:亮度菜单
 *
 * @author liuzhenli 2020/8/16
 * Email: 848808263@qq.com
 */
public class ReadBrightnessMenu extends BaseMenu {
    @BindView(R.id.view_brightness_mines)
    ImageView viewBrightnessMines;
    @BindView(R.id.sb_menu_setting_brightness)
    QMUISlider sbMenuSettingBrightness;
    @BindView(R.id.view_brightness_add)
    ImageView viewBrightnessAdd;
    @BindView(R.id.view_book_menu_brightness_divider_1)
    View viewBookMenuBrightnessDivider1;
    @BindView(R.id.tv_bright_follow_sys)
    TextView tvBrightFollowSys;
    @BindView(R.id.view_book_menu_brightness_divider_2)
    View viewBookMenuBrightnessDivider2;
    @BindView(R.id.tv_bright_protect_eye)
    TextView tvBrightProtectEye;

    public ReadBrightnessMenu(@NonNull Context context) {
        this(context, null);
    }

    public ReadBrightnessMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReadBrightnessMenu(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_read_menu_brightness_setting;
    }

    @Override
    protected void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        //减小亮度
        ClickUtils.click(viewBrightnessMines, o -> {

        });

        sbMenuSettingBrightness.setCallback(new QMUISlider.DefaultCallback() {
            @Override
            public void onProgressChange(QMUISlider slider, int progress, int tickCount, boolean fromUser) {
                super.onProgressChange(slider, progress, tickCount, fromUser);

            }
        });
    }

    private void setBrightness(int value){
    }

    @Override
    protected void changeTheme() {

    }
}

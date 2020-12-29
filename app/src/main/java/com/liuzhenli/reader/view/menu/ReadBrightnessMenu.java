package com.liuzhenli.reader.view.menu;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liuzhenli.common.utils.ScreenUtils;
import com.micoredu.readerlib.helper.ReadConfigManager;
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
    @BindView(R.id.sb_menu_setting_brightness)
    QMUISlider sbMenuSettingBrightness;
    @BindView(R.id.tv_bright_follow_sys)
    TextView tvBrightFollowSys;
    @BindView(R.id.tv_bright_protect_eye)
    TextView tvBrightProtectEye;
    @BindView(R.id.sw_brightness_follow_system)
    Switch swFollowSystem;
    @BindView(R.id.sw_brightness_protect_eye)
    Switch swProtectEye;


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
        //跟随系统 即自动调节
        swFollowSystem.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setBrightness(-1);
            } else {
                int light = ReadConfigManager.getInstance().getLight();
                setBrightness(light);
            }
            ReadConfigManager.getInstance().setLightFollowSys(isChecked);
        });
        //护眼模式
        swProtectEye.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mCallback.onProtectEyeClick(isChecked);
        });

        //亮度调节
        sbMenuSettingBrightness.setCallback(new QMUISlider.DefaultCallback() {
            @Override
            public void onProgressChange(QMUISlider slider, int progress, int tickCount, boolean fromUser) {
                super.onProgressChange(slider, progress, tickCount, fromUser);
                setBrightness(progress);
                ReadConfigManager.getInstance().setLight(progress);
            }
        });
        sbMenuSettingBrightness.setCurrentProgress(ReadConfigManager.getInstance().getLight());
        sbMenuSettingBrightness.setTickCount(255);
    }

    private void setBrightness(int value) {
        if (mActivity != null) {
            ScreenUtils.setScreenBrightness(mActivity, value);
        }
    }

    public void setProtectedEyeMode(boolean on) {
        swProtectEye.setChecked(on);
    }

    public void setBrightnessFollowSystem(boolean follow) {
        swFollowSystem.setChecked(follow);
    }

    @Override
    protected void changeTheme() {

    }

    public void setCallback(Activity activity, CallBack callBack) {
        this.mCallback = callBack;
        this.mActivity = activity;
    }

    private CallBack mCallback;
    private Activity mActivity;

    public interface CallBack {
        void onProtectEyeClick(boolean on);
    }
}

package com.micoredu.readerlib.helper;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.util.DisplayMetrics;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.liuzhenli.common.BaseApplication;
import com.liuzhenli.common.utils.BitmapUtil;
import com.liuzhenli.common.utils.MeUtils;
import com.liuzhenli.common.SharedPreferencesUtil;
import com.micoredu.readerlib.R;
import com.micoredu.readerlib.ReaderLibManager;
import com.micoredu.readerlib.utils.ReaderConfig;

/****阅读参数设置*/
public class ReadConfigManager {
    private static final int DEFAULT_BG = 1;
    /**
     * 阅读器主题背景对应的文字
     */
    private int mTextDrawableIndex = DEFAULT_BG;
    /***
     * 阅读器页面文字,背景选项配置信息  文字颜色,背景  这里约定,最后一个是夜间模式
     */
    private List<Map<String, Integer>> mTextDrawable;
    private Bitmap bgBitmap;
    /***屏幕方向*/
    private int screenDirection;
    private int speechRate;
    private boolean speechRateFollowSys;
    private int textSize;
    /***字体颜色**/
    private int mTextColor;
    private boolean bgIsColor;
    private int mBackgroundColor;
    private float lineMultiplier;
    private float paragraphSize;
    private int pageMode;
    /***隐藏状态栏*/
    private Boolean hideStatusBar;
    private Boolean hideNavigationBar;
    private String fontPath;
    private int textConvert;
    private int navBarColor;
    private Boolean textBold;
    private Boolean canClickTurn;
    private Boolean canKeyTurn;
    private Boolean readAloudCanKeyTurn;
    private int clickSensitivity;
    private Boolean clickAllNext;
    private Boolean showTitle;
    private Boolean showTimeBattery;
    private Boolean showLine;
    /**
     * 根据阅读页的主题,配置不同的图标
     */
    private Boolean darkStatusIcon;
    private int indent;
    private int screenTimeOut;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private int tipPaddingLeft;
    private int tipPaddingTop;
    private int tipPaddingRight;
    private int tipPaddingBottom;
    private float textLetterSpacing;
    private boolean canSelectText;

    private SharedPreferencesUtil preferences;

    private static ReadConfigManager readBookControl;

    public static ReadConfigManager getInstance() {
        if (readBookControl == null) {
            synchronized (ReadConfigManager.class) {
                if (readBookControl == null) {
                    readBookControl = new ReadConfigManager();
                }
            }
        }
        return readBookControl;
    }

    private ReadConfigManager() {
        preferences = SharedPreferencesUtil.getInstance();
        initTextDrawable();
        updateReaderSettings();
    }

    void updateReaderSettings() {
        this.hideStatusBar = preferences.getBoolean("hide_status_bar", false);
        this.hideNavigationBar = preferences.getBoolean("hide_navigation_bar", false);
        this.indent = preferences.getInt("indent", 2);
        this.textSize = preferences.getInt("textSize", 20);
        this.canClickTurn = preferences.getBoolean("canClickTurn", true);
        this.canKeyTurn = preferences.getBoolean("canKeyTurn", true);
        this.readAloudCanKeyTurn = preferences.getBoolean("readAloudCanKeyTurn", false);
        this.lineMultiplier = preferences.getFloat("lineMultiplier", 1);
        this.paragraphSize = preferences.getFloat("paragraphSize", 1);
        this.clickSensitivity = preferences.getInt("clickSensitivity", 50) > 100
                ? 50 : preferences.getInt("clickSensitivity", 50);
        this.clickAllNext = preferences.getBoolean("clickAllNext", false);
        this.fontPath = preferences.getString("fontPath", null);
        this.textConvert = preferences.getInt("textConvertInt", 0);
        this.textBold = preferences.getBoolean("textBold", false);
        this.speechRate = preferences.getInt("speechRate", 10);
        this.speechRateFollowSys = preferences.getBoolean("speechRateFollowSys", true);
        this.showTitle = preferences.getBoolean("showTitle", true);
        this.showTimeBattery = preferences.getBoolean("showTimeBattery", true);
        this.showLine = preferences.getBoolean("showLine", true);
        this.screenTimeOut = preferences.getInt("screenTimeOut", 0);
        this.paddingLeft = preferences.getInt("paddingLeft", ReaderConfig.DEFAULT_MARGIN_WIDTH);
        this.paddingTop = preferences.getInt("paddingTop", 0);
        this.paddingRight = preferences.getInt("paddingRight", ReaderConfig.DEFAULT_MARGIN_WIDTH);
        this.paddingBottom = preferences.getInt("paddingBottom", 0);
        this.tipPaddingLeft = preferences.getInt("tipPaddingLeft", ReaderConfig.DEFAULT_MARGIN_WIDTH);
        this.tipPaddingTop = preferences.getInt("tipPaddingTop", 0);
        this.tipPaddingRight = preferences.getInt("tipPaddingRight", ReaderConfig.DEFAULT_MARGIN_WIDTH);
        this.tipPaddingBottom = preferences.getInt("tipPaddingBottom", 0);
        this.pageMode = preferences.getInt("pageMode", 0);
        this.screenDirection = preferences.getInt("screenDirection", 0);
        this.navBarColor = preferences.getInt("navBarColorInt", 0);
        this.textLetterSpacing = preferences.getFloat("textLetterSpacing", 0);
        this.canSelectText = preferences.getBoolean("canSelectText", false);
        initTextDrawableIndex();
    }

    /***阅读背景*/
    private void initTextDrawable() {
        if (null == mTextDrawable) {
            //0 白天
            mTextDrawable = new ArrayList<>();
            Map<String, Integer> temp1 = new HashMap<>();
            temp1.put("textColor", BaseApplication.getInstance().getResources().getColor(R.color.skin_day_reader_scene_text_color));
            temp1.put("bgIsColor", 1);
            temp1.put("textBackground", BaseApplication.getInstance().getResources().getColor(R.color.skin_day_reader_scene_bg_color));
            temp1.put("darkStatusIcon", 1);
            mTextDrawable.add(temp1);

            //1 黄
            Map<String, Integer> temp2 = new HashMap<>();
            temp2.put("textColor", BaseApplication.getInstance().getResources().getColor(R.color.skin_yellow_reader_scene_text_color));
            temp2.put("bgIsColor", 1);
            temp2.put("textBackground", BaseApplication.getInstance().getResources().getColor(R.color.skin_yellow_reader_scene_bg_color));
            temp2.put("darkStatusIcon", 1);
            mTextDrawable.add(temp2);

            //2 绿
            Map<String, Integer> temp3 = new HashMap<>();
            temp3.put("textColor", BaseApplication.getInstance().getResources().getColor(R.color.skin_green_reader_scene_text_color));
            temp3.put("bgIsColor", 1);
            temp3.put("textBackground", BaseApplication.getInstance().getResources().getColor(R.color.skin_green_reader_scene_bg_color));
            temp3.put("darkStatusIcon", 1);
            mTextDrawable.add(temp3);

            //3 粉
            Map<String, Integer> temp4 = new HashMap<>();
            temp4.put("textColor", BaseApplication.getInstance().getResources().getColor(R.color.skin_pink_reader_scene_text_color));
            temp4.put("bgIsColor", 1);
            temp4.put("textBackground", BaseApplication.getInstance().getResources().getColor(R.color.skin_pink_reader_scene_bg_color));
            temp4.put("darkStatusIcon", 0);
            mTextDrawable.add(temp4);

            //4 深蓝
            Map<String, Integer> temp5 = new HashMap<>();
            temp5.put("textColor", BaseApplication.getInstance().getResources().getColor(R.color.skin_sblue_reader_scene_text_color));
            temp5.put("bgIsColor", 1);
            temp5.put("textBackground", BaseApplication.getInstance().getResources().getColor(R.color.skin_sblue_reader_scene_bg_color));
            temp5.put("darkStatusIcon", 0);
            mTextDrawable.add(temp5);

            //5 蓝
            Map<String, Integer> temp6 = new HashMap<>();
            temp6.put("textColor", BaseApplication.getInstance().getResources().getColor(R.color.skin_blue_reader_scene_text_color));
            temp6.put("bgIsColor", 1);
            temp6.put("textBackground", BaseApplication.getInstance().getResources().getColor(R.color.skin_blue_reader_scene_bg_color));
            temp6.put("darkStatusIcon", 0);
            mTextDrawable.add(temp6);

            //6 夜间
            Map<String, Integer> temp7 = new HashMap<>();
            temp7.put("textColor", BaseApplication.getInstance().getResources().getColor(R.color.skin_night_reader_scene_text_color));
            temp7.put("bgIsColor", 1);
            temp7.put("textBackground", BaseApplication.getInstance().getResources().getColor(R.color.skin_night_reader_scene_bg_color));
            temp7.put("darkStatusIcon", 0);
            mTextDrawable.add(temp7);
        }
    }

    public void initTextDrawableIndex() {
        if (getIsNightTheme()) {
            //直接取值夜间模式字体颜色
            mTextDrawableIndex = preferences.getInt("textDrawableIndexNight", 6);
        } else {
            mTextDrawableIndex = preferences.getInt("textDrawableIndex", DEFAULT_BG);
        }
        if (mTextDrawableIndex == -1) {
            mTextDrawableIndex = DEFAULT_BG;
        }
        initPageStyle();
        setTextDrawable();
    }

    @SuppressWarnings("ConstantConditions")
    private void initPageStyle() {
        int bgCustom = getBgCustom(mTextDrawableIndex);
        //图片背景
        if ((bgCustom == 2 || bgCustom == 3) && getBgPath(mTextDrawableIndex) != null) {
            bgIsColor = false;
            String bgPath = getBgPath(mTextDrawableIndex);
            Resources resources = ReaderLibManager.getAppResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            if (bgCustom == 2) {
                bgBitmap = BitmapUtil.getFitSampleBitmap(bgPath, width, height);
            } else {
                bgBitmap = MeUtils.getFitAssetsSampleBitmap(ReaderLibManager.getApplication().getAssets(), bgPath, width, height);
            }
            if (bgBitmap != null) {
                return;
            }
        } else if (getBgCustom(mTextDrawableIndex) == 1) {
            bgIsColor = true;
            mBackgroundColor = getBgColor(mTextDrawableIndex);
            return;
        }
        //the background is color
        bgIsColor = true;
        if (mTextDrawable.size() > mTextDrawableIndex && mTextDrawable.get(mTextDrawableIndex) != null) {
            mBackgroundColor = mTextDrawable.get(mTextDrawableIndex).get("textBackground");
        }
    }

    private void setTextDrawable() {
        darkStatusIcon = getDarkStatusIcon(mTextDrawableIndex);
        mTextColor = getTextColor(mTextDrawableIndex);
    }

    public int getTextColor(int textDrawableIndex) {
        if (preferences.getInt("textColor" + textDrawableIndex, 0) != 0) {
            return preferences.getInt("textColor" + textDrawableIndex, 0);
        } else {
            return getDefaultTextColor(textDrawableIndex);
        }
    }

    /**
     * set text color
     *
     * @param textDrawableIndex the position
     * @param textColor         text color
     */
    public void setTextColor(int textDrawableIndex, int textColor) {
        preferences.putInt("textColor" + textDrawableIndex, textColor);
    }

    @SuppressWarnings("ConstantConditions")
    public Drawable getBgDrawable(int textDrawableIndex, Context context, int width, int height) {
        int color;
        try {
            Bitmap bitmap = null;
            switch (getBgCustom(textDrawableIndex)) {
                case 3:
                    bitmap = MeUtils.getFitAssetsSampleBitmap(context.getAssets(), getBgPath(textDrawableIndex), width, height);
                    if (bitmap != null) {
                        return new BitmapDrawable(context.getResources(), bitmap);
                    }
                case 2:
                    bitmap = BitmapUtil.getFitSampleBitmap(getBgPath(textDrawableIndex), width, height);
                    if (bitmap != null) {
                        return new BitmapDrawable(context.getResources(), bitmap);
                    }
                    break;
                case 1:
                    color = getBgColor(textDrawableIndex);
                    return new ColorDrawable(color);
                default:
                    break;
            }
            if (mTextDrawable.get(textDrawableIndex).get("bgIsColor") != 0) {
                color = mTextDrawable.get(textDrawableIndex).get("textBackground");
                return new ColorDrawable(color);
            } else {
                return getDefaultBgDrawable(textDrawableIndex, context);
            }
        } catch (Exception e) {
            if (mTextDrawable.get(textDrawableIndex).get("bgIsColor") != 0) {
                color = mTextDrawable.get(textDrawableIndex).get("textBackground");
                return new ColorDrawable(color);
            } else {
                return getDefaultBgDrawable(textDrawableIndex, context);
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    public Drawable getDefaultBgDrawable(int textDrawableIndex, Context context) {
        if (mTextDrawable.get(textDrawableIndex).get("bgIsColor") != 0) {
            return new ColorDrawable(mTextDrawable.get(textDrawableIndex).get("textBackground"));
        } else {
            return context.getResources().getDrawable(getDefaultBg(textDrawableIndex));
        }
    }

    public int getBgCustom(int textDrawableIndex) {
        return preferences.getInt("bgCustom" + textDrawableIndex, 0);
    }

    public void setBgCustom(int textDrawableIndex, int bgCustom) {
        preferences.putInt("bgCustom" + textDrawableIndex, bgCustom);
    }

    /***与文字相对应的背景路径*/
    public String getBgPath(int textDrawableIndex) {
        return preferences.getString("bgPath" + textDrawableIndex, null);
    }

    public void setBgPath(int textDrawableIndex, String bgUri) {
        preferences.putString("bgPath" + textDrawableIndex, bgUri);
    }

    @SuppressWarnings("ConstantConditions")
    public int getDefaultTextColor(int textDrawableIndex) {
        return mTextDrawable.get(textDrawableIndex).get("textColor");
    }

    @SuppressWarnings("ConstantConditions")
    private int getDefaultBg(int textDrawableIndex) {
        return mTextDrawable.get(textDrawableIndex).get("textBackground");
    }

    public int getBgColor() {
        return mBackgroundColor;
    }

    public int getBgColor(int index) {
        return preferences.getInt("bgColor" + index, Color.parseColor("#1e1e1e"));
    }

    public void setBgColor(int index, int bgColor) {
        preferences.putInt("bgColor" + index, bgColor);
    }

    public boolean getIsNightTheme() {
        return preferences.getBoolean("nightTheme", false);
    }

    /**
     * set night theme
     *
     * @param isNightTheme true if is night theme
     */
    public void setIsNightTheme(boolean isNightTheme) {
        preferences.putBoolean("nightTheme", isNightTheme);
    }

    public boolean getImmersionStatusBar() {
        return preferences.getBoolean("immersionStatusBar", false);
    }

    public void setImmersionStatusBar(boolean immersionStatusBar) {
        preferences.putBoolean("immersionStatusBar", immersionStatusBar);
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        preferences.putInt("textSize", textSize);
    }

    public int getTextColor() {
        return mTextColor;
    }

    public boolean bgIsColor() {
        return bgIsColor;
    }

    public Drawable getTextBackground(Context context) {
        if (bgIsColor) {
            return new ColorDrawable(mBackgroundColor);
        }
        return new BitmapDrawable(context.getResources(), bgBitmap);
    }

    public boolean bgBitmapIsNull() {
        return bgBitmap == null || bgBitmap.isRecycled();
    }

    public Bitmap getBgBitmap() {
        return bgBitmap.copy(Bitmap.Config.ARGB_8888, true);
    }

    public int getTextDrawableIndex() {
        return mTextDrawableIndex;
    }

    public void setTextDrawableIndex(int textDrawableIndex) {
        this.mTextDrawableIndex = textDrawableIndex;
        if (getIsNightTheme()) {
            preferences.putInt("textDrawableIndexNight", textDrawableIndex);
        } else {
            preferences.putInt("textDrawableIndex", textDrawableIndex);
        }
        setTextDrawable();
    }

    public void setTextConvert(int textConvert) {
        this.textConvert = textConvert;
        preferences.putInt("textConvertInt", textConvert);
    }

    /**
     * 字体繁简
     *
     * @return ReaderConfig.CNText.CN_SIMPLE
     */
    public int getTextConvert() {
        return textConvert == -1 ? ReaderConfig.CNText.CN_SIMPLE : textConvert;
    }

    public void setNavBarColor(int navBarColor) {
        this.navBarColor = navBarColor;
        preferences.putInt("navBarColorInt", navBarColor);
    }

    public int getNavBarColor() {
        return navBarColor;
    }


    public void setTextBold(boolean textBold) {
        this.textBold = textBold;
        preferences.putBoolean("textBold", textBold);
    }

    public void setReadBookFont(String fontPath) {
        this.fontPath = fontPath;
        preferences.putString("fontPath", fontPath);
    }

    public String getFontPath() {
        return fontPath;
    }


    public Boolean getTextBold() {
        return textBold;
    }

    public Boolean getCanKeyTurn(Boolean isPlay) {
        if (!canKeyTurn) {
            return false;
        } else if (readAloudCanKeyTurn) {
            return true;
        } else {
            return !isPlay;
        }
    }

    public Boolean getCanKeyTurn() {
        return canKeyTurn;
    }

    public void setCanKeyTurn(Boolean canKeyTurn) {
        this.canKeyTurn = canKeyTurn;
        preferences.putBoolean("canKeyTurn", canKeyTurn);
    }

    public Boolean getAloudCanKeyTurn() {
        return readAloudCanKeyTurn;
    }

    public void setAloudCanKeyTurn(Boolean canAloudKeyTurn) {
        this.readAloudCanKeyTurn = canAloudKeyTurn;
        preferences.putBoolean("readAloudCanKeyTurn", canAloudKeyTurn);
    }

    public Boolean getCanClickTurn() {
        return canClickTurn;
    }

    public void setCanClickTurn(Boolean canClickTurn) {
        this.canClickTurn = canClickTurn;
        preferences.putBoolean("canClickTurn", canClickTurn);
    }

    public float getTextLetterSpacing() {
        return textLetterSpacing;
    }

    public void setTextLetterSpacing(float textLetterSpacing) {
        this.textLetterSpacing = textLetterSpacing;
        preferences.putFloat("textLetterSpacing", textLetterSpacing);
    }

    public float getLineMultiplier() {
        return lineMultiplier;
    }

    public void setLineMultiplier(float lineMultiplier) {
        this.lineMultiplier = lineMultiplier;
        preferences.putFloat("lineMultiplier", lineMultiplier);
    }

    public float getParagraphSize() {
        return paragraphSize;
    }

    public void setParagraphSize(float paragraphSize) {
        this.paragraphSize = paragraphSize;
        preferences.putFloat("paragraphSize", paragraphSize);
    }

    public int getClickSensitivity() {
        return clickSensitivity;
    }

    public void setClickSensitivity(int clickSensitivity) {
        this.clickSensitivity = clickSensitivity;
        preferences.putInt("clickSensitivity", clickSensitivity);
    }

    public Boolean getClickAllNext() {
        return clickAllNext;
    }

    public void setClickAllNext(Boolean clickAllNext) {
        this.clickAllNext = clickAllNext;
        preferences.putBoolean("clickAllNext", clickAllNext);
    }

    public int getSpeechRate() {
        return speechRate;
    }

    public void setSpeechRate(int speechRate) {
        this.speechRate = speechRate;
        preferences.putInt("speechRate", speechRate);
    }

    public boolean isSpeechRateFollowSys() {
        return speechRateFollowSys;
    }

    public void setSpeechRateFollowSys(boolean speechRateFollowSys) {
        this.speechRateFollowSys = speechRateFollowSys;
        preferences.putBoolean("speechRateFollowSys", speechRateFollowSys);
    }

    public Boolean getShowTitle() {
        return showTitle;
    }

    public void setShowTitle(Boolean showTitle) {
        this.showTitle = showTitle;
        preferences.putBoolean("showTitle", showTitle);
    }

    public Boolean getShowTimeBattery() {
        return showTimeBattery;
    }

    public void setShowTimeBattery(Boolean showTimeBattery) {
        this.showTimeBattery = showTimeBattery;
        preferences.putBoolean("showTimeBattery", showTimeBattery);
    }

    public Boolean getHideStatusBar() {
        return hideStatusBar;
    }

    public void setHideStatusBar(Boolean hideStatusBar) {
        this.hideStatusBar = hideStatusBar;
        preferences.putBoolean("hide_status_bar", hideStatusBar);
    }

    public Boolean getToLh() {
        return preferences.getBoolean("toLh", false);
    }

    public void setToLh(Boolean toLh) {
        preferences.putBoolean("toLh", toLh);
    }

    public Boolean getHideNavigationBar() {
        return hideNavigationBar;
    }

    public void setHideNavigationBar(Boolean hideNavigationBar) {
        this.hideNavigationBar = hideNavigationBar;
        preferences.putBoolean("hide_navigation_bar", hideNavigationBar);
    }

    public Boolean getShowLine() {
        return showLine;
    }

    public void setShowLine(Boolean showLine) {
        this.showLine = showLine;
        preferences.putBoolean("showLine", showLine);
    }

    public boolean getDarkStatusIcon() {
        return darkStatusIcon;
    }

    @SuppressWarnings("ConstantConditions")
    public boolean getDarkStatusIcon(int textDrawableIndex) {
        return preferences.getBoolean("darkStatusIcon" + textDrawableIndex, mTextDrawable.get(textDrawableIndex).get("darkStatusIcon") != 0);
    }

    public void setDarkStatusIcon(int textDrawableIndex, Boolean darkStatusIcon) {
        preferences.putBoolean("darkStatusIcon" + textDrawableIndex, darkStatusIcon);
    }

    public int getScreenTimeOut() {
        return screenTimeOut;
    }

    public void setScreenTimeOut(int screenTimeOut) {
        this.screenTimeOut = screenTimeOut;
        preferences.putInt("screenTimeOut", screenTimeOut);
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
        preferences.putInt("paddingLeft", paddingLeft);
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
        preferences.putInt("paddingTop", paddingTop);
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
        preferences.putInt("paddingRight", paddingRight);
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
        preferences.putInt("paddingBottom", paddingBottom);
    }

    public int getTipPaddingLeft() {
        return tipPaddingLeft;
    }

    public void setTipPaddingLeft(int tipPaddingLeft) {
        this.tipPaddingLeft = tipPaddingLeft;
        preferences.putInt("tipPaddingLeft", tipPaddingLeft);
    }

    public boolean isCanSelectText() {
        return canSelectText;
    }

    public void setCanSelectText(boolean canSelectText) {
        this.canSelectText = canSelectText;
        preferences.putBoolean("canSelectText", canSelectText);
    }

    public int getTipPaddingTop() {
        return tipPaddingTop;
    }

    public void setTipPaddingTop(int tipPaddingTop) {
        this.tipPaddingTop = tipPaddingTop;
        preferences.putInt("tipPaddingTop", tipPaddingTop);
    }

    public int getTipPaddingRight() {
        return tipPaddingRight;
    }

    public void setTipPaddingRight(int tipPaddingRight) {
        this.tipPaddingRight = tipPaddingRight;
        preferences.putInt("tipPaddingRight", tipPaddingRight);
    }

    public int getTipPaddingBottom() {
        return tipPaddingBottom;
    }

    public void setTipPaddingBottom(int tipPaddingBottom) {
        this.tipPaddingBottom = tipPaddingBottom;
        preferences.putInt("tipPaddingBottom", tipPaddingBottom);
    }

    public int getPageMode() {
        return pageMode;
    }

    public void setPageMode(int pageMode) {
        this.pageMode = pageMode;
        preferences.putInt("pageMode", pageMode);
    }

    public int getScreenDirection() {
        return screenDirection;
    }

    public void setScreenDirection(int screenDirection) {
        this.screenDirection = screenDirection;
        preferences.putInt("screenDirection", screenDirection);
    }

    public void setIndent(int indent) {
        this.indent = indent;
        preferences.putInt("indent", indent);
    }

    public int getIndent() {
        return indent;
    }

    public int getLight() {
        return preferences.getInt("light", getScreenBrightness());
    }

    public void setLight(int light) {
        preferences.putInt("light", light);
    }

    public Boolean getLightFollowSys() {
        return preferences.getBoolean("lightFollowSys", true);
    }

    public void setLightFollowSys(boolean isFollowSys) {
        preferences.putBoolean("lightFollowSys", isFollowSys);
    }

    private int getScreenBrightness() {
        int value = 0;
        ContentResolver cr = BaseApplication.getInstance().getContentResolver();
        try {
            value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException ignored) {
        }
        return value;
    }

    public boolean disableScrollClickTurn() {
        return preferences.getBoolean("disableScrollClickTurn", false);
    }
}

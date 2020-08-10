package com.liuzhenli.reader.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class DensityUtil {
    /** 
     * 根据手机的分辨率�?dp 的单�?转成�?px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率�?px(像素) 的单�?转成�?dp 
     */  
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }
    
    
    /*
	 * 适配屏幕文字大小
	 */
	public static int getTextSize(Context cxt) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity) cxt).getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		int densityDpi = displaymetrics.densityDpi;
		int size = 0;
		switch (densityDpi) {
		case 120:
			size = 12;
			break;
		case 160:
			size = 18;
			break;
		case 240:
			size = 24;
			break;
		case 480:// 适配小米3手机
			size = 48;
			break;
		default:
			size = 31;
			break;
		}

		// size = sp.getInt("textSize", size);
		return size;
	}

	/**
	 * 取得屏幕密度
	 * 
	 * @param cxt
	 * @return
	 */
	public static int getdensityDpi(Context cxt) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity) cxt).getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		int densityDpi = displaymetrics.densityDpi;
		// size = sp.getInt("textSize", size);
		return densityDpi;
	}
}

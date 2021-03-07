package com.liuzhenli.common.widget.face;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liwei on 2017/6/6.
 */
public class StringDateToLongUtils {

    // 将字符串转为时间戳
    public static long getTime(String user_time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time = 0;
        try {
            Date d = sdf.parse(user_time);
            time = d.getTime();
        }catch (Exception e) {
        }
        return time;
    }

    // 将时间戳转为字符串
    public static String getStrTime(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        // 例如：
//        cc_time=1291778220 ;
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }
}

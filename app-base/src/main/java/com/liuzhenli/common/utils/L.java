package com.liuzhenli.common.utils;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import com.liuzhenli.common.BaseApplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import timber.log.Timber;

public class L {
    /***日志文件总开关*/
    private static final Boolean LOG_SWITCH = BaseApplication.isDebug;
    /***日志写入文件开关*/
    private static final Boolean LOG_TO_FILE = false;
    /***默认的tag**/
    private static final String LOG_TAG = "zlog--- ";
    /***输入日志类型，v代表输出所有信息,w则只输出警告...*/
    private static final char LOG_TYPE = 'v';
    /***sd卡中日志文件的最多保存天数*/
    private static final int LOG_SAVE_DAYS = 7;
    /*** 日志的输出格式*/
    @SuppressLint("SimpleDateFormat")
    private final static SimpleDateFormat LOG_FORMAT
            = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /***日志文件格式*/
    @SuppressLint("SimpleDateFormat")
    private final static SimpleDateFormat FILE_SUFFIX = new SimpleDateFormat("yyyy-MM-dd");
    /***日志文件保存路径*/
    private static String LOG_FILE_PATH;
    /***日志文件保存名称*/
    private static String LOG_FILE_NAME;

    private static int LOG_MAX_LENGTH = 2000;

    /***在Application中初始化*/
    public static void init() {
        LOG_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator
                + BaseApplication.getInstance().getPackageName();
        LOG_FILE_NAME = "Log";
    }

    /***************************** Warn*********************************/
    public static void w(Object msg) {
        w(LOG_TAG, msg);
    }

    public static void w(String tag, Object msg) {
        w(tag, msg, null);
    }

    public static void w(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'w');
    }

    /**************************** Error ********************************/
    public static void e(Object msg) {
        e(LOG_TAG, msg);
    }

    public static void e(String tag, Object msg) {
        e(tag, msg, null);
    }

    public static void e(String tag, Object msg, Throwable tr) {
        if (msg == null) {
            log(tag, "null", tr, 'e');
        } else {
            log(tag, msg.toString(), tr, 'e');
        }
    }

    /**************************** Debug ********************************/
    public static void d(Object msg) {
        d(LOG_TAG, msg);
    }

    public static void d(String tag, Object msg) {// 调试信息
        d(tag, msg, null);
    }

    public static void d(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'd');
    }

    /***************************** Info  *********************************/
    public static void i(Object msg) {
        i(LOG_TAG, msg);
    }

    public static void i(String tag, Object msg) {
        i(tag, msg, null);
    }

    public static void i(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'i');
    }

    /*************************** Verbose ********************************/
    public static void v(Object msg) {
        v(LOG_TAG, msg);
    }

    public static void v(String tag, Object msg) {
        v(tag, msg, null);
    }

    public static void v(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'v');
    }

    /**
     * 根据tag, msg和等级，输出日志
     *
     * @param tag   log tag
     * @param msg   log content
     * @param level log level
     */
    private static void log(String tag, String msg, Throwable tr, char level) {
        if (LOG_SWITCH) {
            if ('e' == level && ('e' == LOG_TYPE || 'v' == LOG_TYPE)) { // 输出错误信息
                Log.e(tag, createMessage(msg), tr);
            } else if ('w' == level && ('w' == LOG_TYPE || 'v' == LOG_TYPE)) {
                Log.w(tag, createMessage(msg), tr);
            } else if ('d' == level && ('d' == LOG_TYPE || 'v' == LOG_TYPE)) {
                Log.d(tag, createMessage(msg), tr);
            } else if ('i' == level && ('d' == LOG_TYPE || 'v' == LOG_TYPE)) {
//                i(tag,msg);
                Log.i(tag, createMessage(msg), tr);
            } else {
                Log.v(tag, createMessage(msg), tr);
            }
            if (LOG_TO_FILE) {
                log2File(String.valueOf(level), tag, msg + tr == null ? "" : "\n" + Log.getStackTraceString(tr));
            }
        }
    }

    public static void i(String TAG, String msg) {
        int strLength = msg.length();
        int start = 0;
        int end = LOG_MAX_LENGTH;
        for (int i = 0; i < 100; i++) {
            //剩下的文本还是大于规定长度则继续重复截取并输出
            if (strLength > end) {
                Timber.i(msg.substring(start, end));
                start = end;
                end = end + LOG_MAX_LENGTH;
            } else {
                Timber.i(msg.substring(start, strLength));
                break;
            }
        }
    }

    private static String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return null;
        }
        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if (st.getFileName().equals("LogUtils.java")) {
                continue;
            }
            return "[" + Thread.currentThread().getName() + "("
                    + Thread.currentThread().getId() + "): " + st.getFileName()
                    + ":" + st.getLineNumber() + "]";
        }
        return null;
    }

    private static String createMessage(String msg) {
        String functionName = getFunctionName();
        return (functionName == null ? msg
                : (functionName + " - " + msg));
    }

    /**
     * 打开日志文件并写入日志
     **/
    private synchronized static void log2File(String mylogtype, String tag, String text) {
        Date nowtime = new Date();
        String date = FILE_SUFFIX.format(nowtime);
        String dateLogContent = LOG_FORMAT.format(nowtime) + ":" + mylogtype + ":" + tag + ":" + text; // 日志输出格式
        File destDir = new File(LOG_FILE_PATH);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File file = new File(LOG_FILE_PATH, LOG_FILE_NAME + date);
        try {
            FileWriter filerWriter = new FileWriter(file, true);
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(dateLogContent);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定的日志文件
     */
    public static boolean delFile() {
        String needDelFile = FILE_SUFFIX.format(getDateBefore());
        File file = new File(LOG_FILE_PATH, needDelFile + LOG_FILE_NAME);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    /*** 得到LOG_SAVE_DAYS天前的日期 */
    private static Date getDateBefore() {
        Date nowTime = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(nowTime);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - LOG_SAVE_DAYS);
        return now.getTime();
    }


    public static String getErrorString(Throwable e) {
        String result;
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        Throwable cause = e.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        result = writer.toString();
        return result;
    }
}

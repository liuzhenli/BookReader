package com.liuzhenli.reader.exception;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/** 
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告. 
 *  
 * @author user 
 *  
 */  
public class CrashHandler implements UncaughtExceptionHandler {
      
    public final String TAG = "CrashHandler";
    public final String requestURL = "http://192.168.1.110:8080/UploadError/UploadServlet";
    //系统默认的UncaughtException处理类   
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例  
    private static CrashHandler INSTANCE = new CrashHandler();  
    //程序的Context对象  
    private Context mContext;
    //用来存储设备信息和异常信息  
    private Map<String, String> infos = new HashMap<String, String>();
  
    //用于格式化日期,作为日志文件名的一部分  
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	private String mailto ;
  
    /** 保证只有一个CrashHandler实例 */  
    private CrashHandler() {  
    }  
  
    /** 获取CrashHandler实例 ,单例模式 */  
    public static CrashHandler getInstance() {  
        return INSTANCE;  
    }  
  
    /** 
     * 初始化 
     *  
     * @param context 
     */  
    public void init(Context context) {
        mContext = context;  
        //获取系统默认的UncaughtException处理器  
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器  
        Thread.setDefaultUncaughtExceptionHandler(this);
    }  
  
    /** 
     * 当UncaughtException发生时会转入该函数来处理 
     */  
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {  
            //如果用户没有处理则让系统默认的异常处理器来处理  
            mDefaultHandler.uncaughtException(thread, ex);  
        } else {  
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                Log.e(TAG, "error : ", e);
//            }
            //退出程序

//           ReaderApplication application = (ReaderApplication) mContext;
//            Intent intent = new Intent(application.getApplicationContext(), MainActivity.class);
//            PendingIntent restartIntent = PendingIntent.getActivity(
//                    application.getApplicationContext(), 0, intent,
//                    Intent.FLAG_ACTIVITY_NEW_TASK);
            //退出程序
//            AlarmManager mgr = (AlarmManager)application.getSystemService(Context.ALARM_SERVICE);
//            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500,
//                    restartIntent); // 1秒钟后重启应用
//            application.finishActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);

        }  
    }  
  
    /** 
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 
     *  
     * @param ex 
     * @return true:如果处理了该异常信息;否则返回false. 
     */  
    private boolean handleException(Throwable ex) {
        if (ex == null) {  
            return false;  
        }  
        //使用Toast来显示异常信息  
//        new Thread() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
//                Looper.loop();
//            }
//        }.start();
        
        //收集设备参数信息

        Logcat.collectDeviceInfo(mContext,infos);
        Log.i(TAG, "收集设备信息完成"+infos.size());
        //保存日志文件   
        ex.printStackTrace();
        Log.i(TAG, "打印日志");
        //2013-06-27
        String saveCrashInfo2File = Logcat.saveCrashInfo2File(mContext,ex,infos);
        Logger.i(TAG, "日志保存完成");
        
//        Logcat.sendErrorLogToServer(new File(saveCrashInfo2File), requestURL);
        return true;  
    }  
    
      
    
    /** 
     * 网络是否可用 
     *  
     * @param context 
     * @return 
     */  
    public  boolean isNetworkAvailable(Context context) {
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info != null) {  
            for (int i = 0; i < info.length; i++) {  
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;  
                }  
            }  
        }  
        
        return false;  
    } 
}  
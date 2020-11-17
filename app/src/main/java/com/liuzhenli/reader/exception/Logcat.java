package com.liuzhenli.reader.exception;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.liuzhenli.common.utils.Constant;
import com.liuzhenli.common.utils.FileUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

public class Logcat {
	private static final String TAG = "Logcat";
	private static boolean isShow = false;
	// 用于格式化日期,作为日志文件名的一部分
	private static DateFormat formatter = new SimpleDateFormat(
			"[yyyy-MM-dd HH:mm:ss");
	private static long reference = 60 * 60 * 1000 * 48L;
	private static int fileLengthReference = 1024 * 20;

	/**
	 * 发错误日志到服务器
	 *
	 * @param
	 * @param serverURL
	 */
	public static void sendErrorLogToServer(File logFile, String serverURL) {
		SimpleDateFormat logFt = new SimpleDateFormat("yyyyMMdd");
		List<File> files = new ArrayList<File>();
		FileUtils.list(logFile.getParentFile(), "crash", ".txt", "3", files);
		String time = logFt.format(new Date());
		try {
			if (files.isEmpty())
				return;
			// UIHelper.Log("i", "", "发错误日志到服务器----start");
			File zipFile = new File(logFile.getParent() + "/crash_" + time
					+ ".zip");
//			ZipUtils.zipFiles(files, zipFile);
//			UploadUtil uploadUtil = new UploadUtil();
//			uploadUtil.uploadFile(zipFile, serverURL);
			// UIHelper.Log("i", "", "发错误日志到服务器----end");
		} catch (Exception e) {
			return;
		}
	}

	/**
	 * 支付结果
	 */
	public static void sendResultForPay(final Activity context, final SharedPreferences sp, final String result) {
//		GetRequestParam requestParam = new GetRequestParam();
//		final String concatGetRequestParam = concatGetRequestParam(
//				requestParam, context);
//		final RequestVo updateVo = new RequestVo();
//		updateVo.requestUrl = R.string.pay_result;// 获取资源的url
//		updateVo.context = context;
//
//		ThreadPoolManager.getInstance().addTask(new Runnable() {
//
//			public void run() {
//				NetUtil.get(updateVo, concatGetRequestParam+"&&price="+ Constant.Price+"&&result="+result);
//				sendWapPushRequest(context, sp);
//			}
//		});
	}

	public static String getIMSI(Context context) {
//		TelephonyManager tm = (TelephonyManager) context
//				.getSystemService(Context.TELEPHONY_SERVICE);
//		return tm.getSubscriberId();
		return "";
	}

	public static String getPackageName(Context context) {
		return context.getPackageName();
	}

	/**
	 * 收集设备参数信息 for action
	 *
	 * @param ctx
	 */
	public static void collectDeviceInfoForAction(Context ctx,
                                                  Map<String, String> infos) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			TelephonyManager tm = (TelephonyManager) ctx
					.getSystemService(Context.TELEPHONY_SERVICE);
			if (tm != null) {

				if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
					// TODO: Consider calling
					//    ActivityCompat#requestPermissions
					// here to request the missing permissions, and then overriding
					//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
					//                                          int[] grantResults)
					// to handle the case where the user grants the permission. See the documentation
					// for ActivityCompat#requestPermissions for more details.
					return;
				}
				String deviceId = tm.getDeviceId();

				if (deviceId == null) {
					deviceId = tm.getSubscriberId();

				}
				if (deviceId == null) {
					deviceId = Settings.System.getString(
							ctx.getContentResolver(),
							Settings.System.ANDROID_ID);

				}
				if (deviceId == null || "9774d56d682e549c".equals(deviceId)) {// 在主流厂商生产的设备上，有一个很经常的bug，就是每个设备都会产生相同的ANDROID_ID：9774d56d682e549c
					deviceId = getMac();

				}
				if (deviceId == null) {
					deviceId = UUID.randomUUID().toString();

				}
				infos.put("uuid", deviceId);
			}
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				infos.put("appver", versionName);
			}
			infos.put("sysname", "Android");
			infos.put("sysver", android.os.Build.VERSION.RELEASE);
			String packageName = ctx.getPackageName();
			infos.put("appname",
					packageName.substring(packageName.lastIndexOf(".") + 1));

		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}
		// infos.put("FINGERPRINT", android.os.Build.FINGERPRINT);
		// Field[] fields = Build.class.getDeclaredFields();
		// for (Field field : fields) {
		// try {
		// field.setAccessible(true);
		// infos.put(field.getName(), field.get(null).toString());
		// Log.d(TAG, field.getName() + " : " + field.get(null));
		// } catch (Exception e) {
		// Log.e(TAG, "an error occured when collect crash info", e);
		// }
		// }
	}

	/**
	 * 收集设备参数信息 for boot
	 *
	 * @param ctx
	 */
	public static void collectDeviceInfo(Context ctx, Map<String, String> infos) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			TelephonyManager tm = (TelephonyManager) ctx
					.getSystemService(Context.TELEPHONY_SERVICE);
			if (tm != null) {
				if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
					// TODO: Consider calling
					String imei = tm.getDeviceId();
					infos.put("IMEI", imei);
				}

			}
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
//			String channel = Logcat.getMetaDataValue(ctx, "CHANNEL");
//			infos.put("channel", channel);// 2013-07-10 增加渠道号，以便判断来源于哪个包
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}
		// infos.put("FINGERPRINT", android.os.Build.FINGERPRINT);
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}

	/**
	 * 获取日志文件数据 2013-06-27修改 增加对未插入SD卡的设备进行支持
	 * 
	 * @param flag
	 * @return
	 */
	public static Map<String, String> getLogFileContent(Context context,
                                                        String flag) {
		Map<String, String> maps = new HashMap<String, String>();
		String dirPath = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			dirPath = "/sdcard/.kanshu/log/";

		} else {
			dirPath = context.getFilesDir().getAbsolutePath() + "/log/";
		}
		File dir = new File(dirPath);
		if (!dir.exists()) {
			return null;
		}
		File[] listFiles = dir.listFiles();
		File logfile = null;
		if (listFiles != null)
			for (File file : listFiles) {
				String name = file.getName();
				if (name.contains(flag)) {
					String[] split = name.split("-");
					String substring = split[1].substring(0,
							split[1].lastIndexOf("."));
					long currentTime = System.currentTimeMillis();
					long timeInterval = currentTime - Long.parseLong(substring);// 间隔
					long length = file.length();
					Logcat.i(TAG, flag + ":" + timeInterval / 60 / 1000 + "分:"
							+ length / 1024 + "K");
					// 日志文件>20k或时间大于两天则上传
					if (timeInterval >= reference
							|| length > fileLengthReference) {
						logfile = file;
						maps.put("filepath", logfile.getAbsolutePath());
					} else {
						return null;
					}
					break;
				}

			}
		if (logfile == null)
			return null;
		StringBuilder sb = new StringBuilder();

		BufferedReader br = null;
		String readLine = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					logfile), Constant.CHARSET_NAME));
			while ((readLine = br.readLine()) != null) {
				sb.append(readLine + ",");
			}
			sb.deleteCharAt(sb.length() - 1);
			br.close();
			maps.put("data", sb.toString());
			return maps;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String StringEcode(String s) {
		String encode = null;

		try {
			encode = URLEncoder.encode(s, Constant.CHARSET_NAME);
		} catch (NullPointerException e1) {
			Logcat.i(TAG, "NullPointerException");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Logcat.i(TAG, "UnsupportedEncodingException");
		}

		return encode;
	}



	/**
	 * 2013-07-11 获取硬件mac地址
	 * 
	 * @return
	 */
	public static String getMac() {
		String macSerial = null;
		String str = "";

		macSerial = runtime(macSerial, str, "cat /sys/class/net/wlan0/address");
		if (macSerial == null)
			macSerial = runtime(macSerial, str,
					"cat /sys/devices/virtual/net/tiwlan0/address");// G3
		if (macSerial == null)
			macSerial = runtime(macSerial, str,
					"cat /sys/class/net/eth0/address");// 模拟器
		return macSerial;
	}

	private static String runtime(String macSerial, String str, String cmd) {
		// TODO Auto-generated method stub
		LineNumberReader input = null;
		try {
			Process pp = Runtime.getRuntime().exec(cmd);

			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			 input = new LineNumberReader(ir);

			for (; null != str;) {
				str = input.readLine();
				if (str != null) {
					macSerial = str.trim();// 去空格
					return macSerial;
				}
			}
		} catch (IOException ex) {
			// 赋予默认值
			ex.printStackTrace();
		}finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return macSerial;
	}



	/**
	 * 保存错误信息到文件中 2013-06-27 增加对没有插SD卡设备的支持
	 * 
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	public static String saveCrashInfo2File(Context context, Throwable ex,
                                            Map<String, String> infos) {
		StringBuffer sb = new StringBuffer(); // 线程安全
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if ("TIME".equals(key)) {
				sb.append("TIME=" + formatter.format(new Date()).substring(1));
				continue;
			} else {
				sb.append(key + "=" + value + " ");
			}
		}
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try {
//			long timestamp = System.currentTimeMillis();
//
//			String fileName = "crash-" + timestamp + ".log";
//			String filePath = null;
//			String dirPath = null;
//			if (Environment.getExternalStorageState().equals(
//					Environment.MEDIA_MOUNTED)) {
//				dirPath = "/sdcard/.liu/log/";
//
//			} else {
//				dirPath = context.getFilesDir().getAbsolutePath() + "/log/";
//
//			}
//			File dir = new File(dirPath);
//			if (!dir.exists()) {
//				dir.mkdirs();
//			}
//			filePath = dirPath + fileName;
//			File[] listFiles = dir.listFiles();
//			File logfile = new File(filePath);
//			if (listFiles != null)
//				for (File file : listFiles) {
//					if (file.getName().contains("crash")) {
//						logfile = file;
//						break;
//					}
//
//				}
			File errorLogFile = FileUtils.getErrorLogFile();
			FileOutputStream fos = new FileOutputStream(errorLogFile, true);
			fos.write(sb.toString().getBytes());
			fos.write("\n".getBytes());
			fos.close();
			return errorLogFile.getAbsolutePath();
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
		}
		return null;
	}

	/**
	 * 保存错误信息到文件中 2013-06-27修改 增加支持无SD卡的设备
	 * 
	 * @param
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	public static String saveInfo2File(Context context,
                                       Map<String, String> infos, String flag) {

		try {
			TimeZone timeZone = TimeZone.getDefault();// 时区
			int offset = timeZone.getOffset(System.currentTimeMillis());
			int timeZoneOffset = offset / 1000 / 60 / 60;
			String time = formatter.format(new Date());
			long timestamp = System.currentTimeMillis();
			String fileName = flag + "-" + timestamp + ".log";
			if (timeZoneOffset > 0)
				time = time + " +0" + timeZoneOffset + "00]";
			else
				time = time + " -0" + Math.abs(timeZoneOffset) + "00]";
			StringBuffer sb = new StringBuffer(); // 线程安全
			sb.append(time + " ");
			for (Map.Entry<String, String> entry : infos.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				sb.append(key + ":" + value + " ");
			}
			String filePath = null;
			String dirPath = null;
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				dirPath = "/sdcard/.kanshu/log/";

			} else {
				dirPath = context.getFilesDir().getAbsolutePath() + "/log/";
			}
			File dir = new File(dirPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			filePath = dirPath + fileName;
			File[] listFiles = dir.listFiles();
			File logfile = new File(filePath);
			if (listFiles != null)
				for (File file : listFiles) {
					if (file.getName().contains(flag)) {
						logfile = file;
						break;
					}

				}

			BufferedWriter bw = null;

			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(logfile, true), Constant.CHARSET_NAME));

			bw.write(sb.toString());
			bw.newLine();
			bw.close();
			return filePath;
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
		}
		return null;
	}

	public static void i(String tag, String msg) {
		if (isShow) {
			Log.i(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (isShow) {
			Log.d(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (isShow) {
			Log.e(tag, msg);
		}
	}

	public static void e(String tag, String msg, Exception e) {
		if (isShow) {
			Log.e(tag, msg, e);
		}
	}

	public static String getMetaDataValue(Context context, String name) {

		Object value = null;

		PackageManager packageManager = context.getPackageManager();

		ApplicationInfo applicationInfo;

		try {

			applicationInfo = packageManager.getApplicationInfo(context

			.getPackageName(), PackageManager.GET_META_DATA);

			if (applicationInfo != null && applicationInfo.metaData != null) {

				value = applicationInfo.metaData.get(name);

			}

		} catch (NameNotFoundException e) {

			throw new RuntimeException(

			"Could not read the name in the manifest file.", e);

		}

		if (value == null) {

			throw new RuntimeException("The name '" + name

			+ "' is not defined in the manifest file's meta data.");

		}

		return value.toString();

	}

	public static void w(String tag, String msg) {
		if (isShow) {
			Log.w(tag, msg);
		}
	}

}

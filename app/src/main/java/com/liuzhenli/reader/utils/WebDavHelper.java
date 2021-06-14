package com.liuzhenli.reader.utils;

import com.liuzhenli.common.BaseApplication;
import com.liuzhenli.common.utils.L;
import com.liuzhenli.common.utils.ZipUtils;
import com.thegrizzlylabs.sardineandroid.Sardine;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Description:
 *
 * @author liuzhenli 2020/12/14
 * Email: 848808263@qq.com
 */
public class WebDavHelper {


    public static final String TAG = "WebDavHelper";
    public static final String DIALOG_BETA_BACKUP_DIR = "cloud_backup_dir/";
    public static final String DEF_SERVER = "https://dav.jianguoyun.com/dav/";

    /**
     * 备份到坚果云webDev
     */

    public static boolean backupToWebDav(String server, String userName, String password) {
        //init webDev
        Sardine sardine = new OkHttpSardine();
        sardine.setCredentials(userName, password);
        //zip data
        File outFile = new File(BaseApplication.getInstance().getCacheDir(), "backup.zip");
        if (outFile.exists()) {
            outFile.delete();
        }
        File[] inFiles = new File(BaseApplication.getInstance().getCacheDir().getParentFile(), "shared_prefs").listFiles();
        if (inFiles == null || inFiles.length <= 0) {
            return false;
        }
        boolean result = false;
        try {
            result = ZipUtils.zipFiles(Arrays.asList(inFiles), outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!result) {
            return false;
        }
        if (!outFile.exists() || !outFile.canRead() || outFile.length() <= 0) {
            return false;
        }
        //upload data
        try {
            if (!sardine.exists(server + DIALOG_BETA_BACKUP_DIR)) {
                sardine.createDirectory(server + DIALOG_BETA_BACKUP_DIR);
            }
            sardine.put(server + DIALOG_BETA_BACKUP_DIR + "backup.zip", outFile, "*/*");
            outFile.delete();
            L.e(TAG, "backup success");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            L.e(TAG, "backup failed");
        }
        return false;
    }

    /**
     * 从坚果云恢复备份
     */
    public static boolean restoreFromWebDav(String server, String userName, String password) {
        //init webDev
        Sardine sardine = new OkHttpSardine();
        sardine.setCredentials(userName, password);
        //download backup data
        File outFile = new File(BaseApplication.getInstance().getCacheDir(), "backup.zip");
        if (outFile.exists()) {
            outFile.delete();
        }
        try {
            outFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (!sardine.exists(server + DIALOG_BETA_BACKUP_DIR)) {
                return false;
            }
            InputStream inputStream = sardine.get(server + DIALOG_BETA_BACKUP_DIR + "backup.zip");
            OutputStream outputStream = new FileOutputStream(outFile);
            byte[] buf = new byte[1024];
            int n = 0;
            while ((n = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, n);
                outputStream.flush();
            }
            outputStream.close();
            inputStream.close();
            File in = outFile;
            File outDir = new File(BaseApplication.getInstance().getCacheDir().getParentFile(), "shared_prefs");
            ZipUtils.unzipFile(in, new File(outDir.getPath()));
            L.e(TAG, "restore success");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            L.e(TAG, "restore failed");
            return false;
        }
    }
}




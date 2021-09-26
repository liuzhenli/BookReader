package com.liuzhenli.common.utils.filepicker.entity;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.Date;

/**
 * Description:
 *
 * @author liuzhenli 2020/8/6
 * Email: 848808263@qq.com
 */
public class FileItem2 {
    /**
     * {@link  com.liuzhenli.common.utils.Constant.FileAttr}
     */
    public String fileType;
    public String fileName;
    public long size;
    public Date time;
    public File file;
    public String filePath;
    public boolean isSelected;
    /***后缀**/
    public String FileSuffix;
    /***文件夹子文件的个数*/
    public String fileCount;
    public Uri uri;

    @NonNull
    @Override
    public String toString() {
        return "LocalFileBean{" +'\n'+
                "fileType='" + fileType + '\'' +'\n'+
                ", fileName='" + fileName + '\'' +'\n'+
                ", size=" + size +'\n'+
                ", time=" + time +'\n'+
                ", file=" + file +'\n'+
                ", filePath='" + filePath + '\'' +'\n'+
                ", isSelected=" + isSelected +'\n'+
                ", FileSuffix='" + FileSuffix + '\'' +'\n'+
                ", fileCount='" + fileCount + '\'' +'\n'+
                ", uri=" + uri +'\n'+
                '}';
    }
}

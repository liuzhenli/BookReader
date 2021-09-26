package com.liuzhenli.common.utils.filepicker.entity;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.io.File;
import java.util.Date;

/**
 * 文件项信息
 */
public class FileItem extends JavaBean {
    private Drawable icon;
    public String path = "/";
    public long size = 0;
    private boolean isDirectory = false;

    public String fileType;
    public String name;
    public Date time;
    public File file;
    public boolean isSelected;
    /***后缀**/
    public String FileSuffix;
    /***文件夹子文件的个数*/
    public String fileCount;
    public Uri uri;


    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean isDirectory) {
        this.isDirectory = isDirectory;
    }

}

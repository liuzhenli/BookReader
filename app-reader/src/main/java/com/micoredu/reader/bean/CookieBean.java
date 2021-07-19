package com.micoredu.reader.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "cookies", primaryKeys = "url")
public class CookieBean implements Parcelable {

    @NonNull
    private String url = "";
    private String cookie;

    @Ignore
    private CookieBean(Parcel in) {
        url = in.readString();
        cookie = in.readString();
    }

    @Ignore
    public CookieBean(String url, String cookie) {
        this.url = url;
        this.cookie = cookie;
    }

    public CookieBean() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(cookie);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCookie() {
        return cookie == null ? "" : cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public static final Creator<CookieBean> CREATOR = new Creator<CookieBean>() {
        @Override
        public CookieBean createFromParcel(Parcel in) {
            return new CookieBean(in);
        }

        @Override
        public CookieBean[] newArray(int size) {
            return new CookieBean[size];
        }
    };
}

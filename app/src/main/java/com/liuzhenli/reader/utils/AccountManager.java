package com.liuzhenli.reader.utils;

/**
 * @author Liuzhenli
 * @since 2019-07-06 20:39
 */
public class AccountManager {
    public AccountManager() {
    }

    public static AccountManager getInstance() {
        return new AccountManager();
    }


    public boolean isLogin() {
        return false;
    }

    public String getToken(){
        return "";
    }
}

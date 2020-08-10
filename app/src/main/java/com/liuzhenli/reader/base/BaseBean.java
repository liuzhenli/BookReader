package com.liuzhenli.reader.base;

import java.io.Serializable;

/**
 * @author Liuzhenli
 * @since 2019-07-06
 */
public class BaseBean implements Serializable {
    public ResultState status;

    public class ResultState implements Serializable {
        public int code;
        public String msg;
    }

    /**
     * 服务器时间
     */
    public long time;


    public int num;
    public int curPage;
    public int totalNum;
    public int totalPage;
    public String accessToken;
    public String test_at;

    public boolean isCodeInvalid() {
        return status != null && status.code != 0;
    }

}

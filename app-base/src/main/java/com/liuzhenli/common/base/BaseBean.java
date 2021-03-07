package com.liuzhenli.common.base;

import java.io.Serializable;

/**
 * @author Liuzhenli
 * @since 2019-07-06
 */
public class BaseBean implements Serializable {
    /**
     * code : 0
     * msg :
     * data : {"sourceUrl":"","update":{"code":1,"url":"","intro":"有新版本"}}
     */

    public int code;
    public String msg;
    /**
     * sourceUrl :
     * update : {"code":1,"url":"","intro":"有新版本"}
     */

    public boolean isCodeInvalid() {
        return code != 0;
    }

}

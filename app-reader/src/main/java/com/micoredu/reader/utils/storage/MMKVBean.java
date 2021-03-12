package com.micoredu.reader.utils.storage;

import java.io.Serializable;

/**
 * Description:
 *
 * @author liuzhenli 3/12/21
 * Email: 848808263@qq.com
 */
public class MMKVBean implements Serializable {
    public @interface Type {

        String INT = "int";
        String LONG = "long";
        String FLOAT = "float";
        String DOUBLE = "double";

        String BOOL = "boolean";
        String STRING = "string";

        String SET = "set";
    }

    public MMKVBean(String type, String key, String value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public String type;
    public String key;
    public String value;
}

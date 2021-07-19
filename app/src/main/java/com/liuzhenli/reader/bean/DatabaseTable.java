package com.liuzhenli.reader.bean;

/**
 * Description:
 *
 * @author liuzhenli 2021/7/19
 * Email: 848808263@qq.com
 */
public class DatabaseTable {
    public String dbName;
    public String tableName;

    public DatabaseTable(String dbName, String tableName) {
        this.dbName = dbName;
        this.tableName = tableName;
    }
}

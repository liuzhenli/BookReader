package com.micoredu.reader.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "readRecord", primaryKeys = ["deviceId", "bookName"])
data class ReadRecord(
    var id: Long? = null,
    var deviceId: String = "",
    var bookName: String = "",
    @ColumnInfo(defaultValue = "0")
    var readTime: Long = 0L,
    @ColumnInfo(defaultValue = "0")
    var lastRead: Long = System.currentTimeMillis(),

    var bookCover: String? = null,
    var type: String? = null,
    var authorName: String? = null,
    var noteUrl: String? = null,

    /**
     * 时间戳,每天的唯一标识
     */
    var dayMillis: Long = 0,

    /**
     * 当天阅读总的时长
     */
    var sumTime: Long = 0,
)
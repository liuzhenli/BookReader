package com.micoredu.reader.dao

import androidx.room.*
import com.micoredu.reader.bean.ReadRecord
import com.micoredu.reader.bean.ReadRecordShow

@Dao
interface ReadRecordDao {

    @get:Query("select * from readRecord")
    val all: List<ReadRecord>

    @get:Query(
        """
        select bookName, sum(readTime) as readTime, max(lastRead) as lastRead 
        from readRecord 
        group by bookName 
        order by bookName collate localized"""
    )
    val allShow: List<ReadRecordShow>

    @get:Query("select sum(readTime) from readRecord")
    val allTime: Long

    @Query(
        """
        select bookName, sum(readTime) as readTime, max(lastRead) as lastRead 
        from readRecord 
        where bookName like '%' || :searchKey || '%'
        group by bookName 
        order by bookName collate localized"""
    )
    fun search(searchKey: String): List<ReadRecordShow>

    @Query("select sum(readTime) from readRecord where bookName = :bookName")
    fun getReadTime(bookName: String): Long?

    @Query("select readTime from readRecord where deviceId = :androidId and bookName = :bookName")
    fun getReadTime(androidId: String, bookName: String): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg readRecord: ReadRecord)

    @Update
    fun update(vararg record: ReadRecord)

    @Delete
    fun delete(vararg record: ReadRecord)


    @Query("delete from readRecord where bookName = :bookName")
    fun deleteByName(bookName: String)


//    @Query("select id,bookCover,type,bookName,authorName,noteUrl,dayMillis, sum(sumTime) as sumTime from readRecord group by bookName order by dayMillis")
//    fun getAllGroupByBookName(): List<ReadRecord?>?

    @Query("select * from readRecord where bookName=:bookName and dayMillis=:dayMillis")
    fun getByBookName(bookName: String?, dayMillis: Long): ReadRecord?


    @Query("select sum(sumTime) from readRecord where dayMillis=:dayMillis")
    fun getTodayAllTime(dayMillis: Long): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(readRecord: ReadRecord?)

    @Update
    fun update(readRecord: ReadRecord?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(readRecord: List<ReadRecord?>?)

    @Query("delete from readRecord where bookName = :bookName")
    fun deleteByBookName(bookName: String?)

    @Query("delete from readRecord")
    fun clear()
}
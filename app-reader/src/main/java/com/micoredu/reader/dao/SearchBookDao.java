package com.micoredu.reader.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.jayway.jsonpath.Criteria;
import com.micoredu.reader.bean.SearchBookBean;

import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2021/6/28
 * Email: 848808263@qq.com
 */
@Dao
public interface SearchBookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplaceInTx(List<SearchBookBean> searchBookBeans);

    @Delete
    void delete(SearchBookBean searchBookBean);

    @Query("SELECT * FROM searchBooks WHERE name is :bookName and  author is :bookAuthor ")
    List<SearchBookBean> getBookList(String bookName, String bookAuthor);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplace(SearchBookBean searchBookBean);

    @Query("SELECT * FROM searchBooks WHERE name is :bookName and  author is :bookAuthor and origin like '%'||:origin||'%'")
    List<SearchBookBean> getBookList(String bookName, String bookAuthor, String origin);

    @Query("SELECT * FROM searchBooks WHERE name is :name  ")
    List<SearchBookBean> getBookListByBookName(String name);

    @Query("SELECT * FROM searchBooks WHERE trim(noteUrl) is :noteUrl")
    SearchBookBean getBookByNoteUrl(String noteUrl);
}

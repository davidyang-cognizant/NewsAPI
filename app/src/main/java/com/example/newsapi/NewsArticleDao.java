package com.example.newsapi;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.newsapi.model.NewsArticle;

import java.util.List;

/**
 * This Dao class is an ORM to help SQLite queries
 */
@Dao
public interface NewsArticleDao {
    @Query("Select * from articles")
    List<NewsArticle> getArticles();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertArticle(NewsArticle person);

    @Update
    void updateArticle(NewsArticle person);

    @Delete
    void deleteArticle(NewsArticle person);

    @Query("DELETE FROM articles")
    void nuke();

    @Query("DELETE FROM articles WHERE id =:id")
    void deleteArticleById(int id);

    /**
     * This is a super neat way of finding a title that *contains* a certain string
     * the LIKE key word will find a match and the '%' means contains rather than match
     * by a specific string.
     * @param title
     * @return
     */
    @Query("SELECT * FROM articles WHERE title LIKE :title || '%'")
    List<NewsArticle> loadArticleByTitle(String title);

}

package com.example.newsapi;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.newsapi.model.NewsArticle;

/**
 * Abstract database class that returns a singleton of the database
 */
@Database(entities = NewsArticle.class, exportSchema = false, version = 2)
public abstract class NewsArticleDatabase extends RoomDatabase {
    private static final String DB_NAME = "article_db";
    private static final Object LOCK = new Object();

    private static NewsArticleDatabase instance;

    public static synchronized NewsArticleDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                /* SEE here that I added allowMainThreadQueries, please do note that this is not
                   best practices but I understand the risks that can come with main thread queries.
                   Since databases carry a lot of data this can block your UI for quite some time. */
                instance = Room.databaseBuilder(context.getApplicationContext(), NewsArticleDatabase.class, DB_NAME)
                        .enableMultiInstanceInvalidation()
                        .fallbackToDestructiveMigration().
                                allowMainThreadQueries().build();
            }
        }
        return instance;
    }

    public abstract NewsArticleDao articleDao();

}

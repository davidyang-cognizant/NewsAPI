package com.example.newsapi.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "articles", indices = @Index(value = {"title"}, unique = true))
public class NewsArticle {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String author;
    private String title;
    private String description;
    private String urlToImage;
    private String publishedAt;


    @Ignore
    public NewsArticle() {

    }

    /**
     * News Article is a model containing the object partitions.
     *
     * @param author      - name of author
     * @param title       - title of article
     * @param description - description of article
     * @param urlToImage  - imageURL
     * @param publishedAt - time of publishment
     */
    @Ignore
    public NewsArticle(String author, String title, String description, String urlToImage, String publishedAt) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
    }


    public NewsArticle(int id, String author, String title, String description, String urlToImage, String publishedAt) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.description = description;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToTimage) {
        this.urlToImage = urlToTimage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
}

package com.example.newsapi.model;

public class NewsArticle {
    private String author;
    private String title;
    private String description;
    private String urlToTimage;
    private String publishedAt;

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrlToTimage() {
        return urlToTimage;
    }

    /**
     * News Article is a model containing the object partitions.
     * @param author - name of author
     * @param title - title of article
     * @param description - description of article
     * @param urlToImage - imageURL
     * @param publishedAt - time of publishment
     */
    public NewsArticle(String author, String title, String description, String urlToImage, String publishedAt) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.urlToTimage = urlToImage;
        this.publishedAt = publishedAt;
    }
}

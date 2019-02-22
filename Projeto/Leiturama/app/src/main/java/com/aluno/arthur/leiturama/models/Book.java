package com.aluno.arthur.leiturama.models;

import java.io.Serializable;

public class Book implements Serializable {

    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String publishedDate;
    private String description;
    private String categories;
    private int pageCount;
    private String imagePath;

    public Book() {}

    public Book(String isbn) {
        this.isbn = isbn;
    }

    public Book(String isbn, String title, String author, String publisher, String publishedDate,
                String description, String categories, int pageCount, String imagePath) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.description = description;
        this.categories = categories;
        this.pageCount = pageCount;
        this.imagePath = imagePath;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}

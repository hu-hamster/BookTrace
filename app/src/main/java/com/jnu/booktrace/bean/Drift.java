package com.jnu.booktrace.bean;

public class Drift {
    private int id;
    private String author_name;
    private String time;
    private String book_image;
    private String book_author;
    private String title;
    private String recommend;

    public Drift() {
    }

    public Drift(int id, String author_name, String time, String title, String book_author, String recommend){
        this.id = id;
        this.author_name = author_name;
        this.time = time;
        this.title = title;
        this.book_author = book_author;
        this.recommend = recommend;
    }

    public Drift(int id, String author_name, String time, String image, String title, String book_author, String recommend) {
        this.id = id;
        this.author_name = author_name;
        this.time = time;
        this.book_image = image;
        this.title = title;
        this.book_author = book_author;
        this.recommend = recommend;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getBook_Author() {
        return book_author;
    }

    public void setBook_Author(String book_author) {
        this.book_author = book_author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBook_image() {
        return book_image;
    }

    public void setBook_image(String book_image) {
        this.book_image = book_image;
    }
}

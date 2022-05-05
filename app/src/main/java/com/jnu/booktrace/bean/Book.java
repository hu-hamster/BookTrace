package com.jnu.booktrace.bean;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.Field;

public class Book implements Parcelable {
    private String id;
    private String isbn10;
    private String title;
    private String image;
    private String author;
    private String translator;
    private String publisher;
    private String pubdate;
    private String price;
    private String pages;
    private String author_intro;
    private String summary;
    private String binding;
    private String tags;

    public Book(){

    }
    protected Book(Parcel in) {
        id = in.readString();
        isbn10 = in.readString();
        title = in.readString();
        image = in.readString();
        author = in.readString();
        translator = in.readString();
        publisher = in.readString();
        pubdate = in.readString();
        price = in.readString();
        pages = in.readString();
        author_intro = in.readString();
        summary = in.readString();
        binding = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getBinding() {
        return binding;
    }

    /**
     *
     * @param binding 规格，精装/平装
     */
    public void setBinding(String binding) {
        this.binding = binding;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPages() {
        return pages;
    }

    /**
     *
     * @param pages 页数
     */
    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getAuthor_intro() {
        return author_intro;
    }

    /**
     *
     * @param author_intro 作者简介
     */
    public void setAuthor_intro(String author_intro) {
        this.author_intro = author_intro;
    }

    public String getSummary() {
        return summary;
    }

    /**
     * @param summary 书籍简介
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getId());
        dest.writeString(getIsbn10());
        dest.writeString(getTitle());
        dest.writeString(getImage());
        dest.writeString(getAuthor());
        dest.writeString(getTranslator());
        dest.writeString(getPublisher());
        dest.writeString(getPubdate());
        dest.writeString(getPrice());
        dest.writeString(getPages());
        dest.writeString(getAuthor_intro());
        dest.writeString(getSummary());
        dest.writeString(getBinding());
    }
}

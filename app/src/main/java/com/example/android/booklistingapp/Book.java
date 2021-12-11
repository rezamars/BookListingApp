package com.example.android.booklistingapp;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class Book {

    private String bookID;
    private String bookTitle;
    private ArrayList<String> bookAuthorsList = new ArrayList<String>();
    private String bookPublishDate;
    private String bookLanguage;
    private String imageLinkSmallThumbnail;
    private Bitmap bitmap;


    public Book(){

    }

    public Book(String id, String title, ArrayList authorsList, String pubDate,
                String language, String imageLink,
                Bitmap bitmap1){
        bookID = id;
        bookTitle = title;
        bookAuthorsList = authorsList;
        bookPublishDate = pubDate;
        bookLanguage = language;
        imageLinkSmallThumbnail = imageLink;
        bitmap = bitmap1;

    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public ArrayList<String> getBookAuthorsList() {
        return bookAuthorsList;
    }

    public void setBookAuthorsList(ArrayList<String> bookAuthorsList) {
        this.bookAuthorsList = bookAuthorsList;
    }

    public String getBookPublishDate() {
        return bookPublishDate;
    }

    public void setBookPublishDate(String bookPublishDate) {
        this.bookPublishDate = bookPublishDate;
    }

    public String getBookLanguage() {
        return bookLanguage;
    }

    public void setBookLanguage(String bookLanguage) {
        this.bookLanguage = bookLanguage;
    }

    public String getImageLinkSmallThumbnail() {
        return imageLinkSmallThumbnail;
    }

    public void setImageLinkSmallThumbnail(String imageLinkSmallThumbnail) {
        this.imageLinkSmallThumbnail = imageLinkSmallThumbnail;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

}

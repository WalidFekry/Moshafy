package com.appwalied.quran.qoutes;

public class new_item_post {

    int id;
    String title;
    String createdAt;
    String imageurl;


    public new_item_post() {
    }

    public new_item_post(String title, String imageurl, int id) {
        this.title = title;
        this.imageurl = imageurl;
        this.id = id;
    }

    public new_item_post(String title, String imageurl, int id, String createdAt) {
        this.title = title;
        this.imageurl = imageurl;
        this.id = id;
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public new_item_post(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

package com.appwalied.quran.notifications_messages.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationResponse {
    @SerializedName("post")
    private List<NotificationModel> posts;

    public List<NotificationModel> getPosts() {
        return posts;
    }

    public void setPosts(List<NotificationModel> posts) {
        this.posts = posts;
    }
}

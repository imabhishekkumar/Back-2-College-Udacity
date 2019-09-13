package com.android.imabhishekkumar.back2college.model;

import java.io.Serializable;

public class ModelPost implements Serializable {
    private String userName;
    private String post;
    private String webLink;
    private String avatarURL;
    private Long timestamp;

    public ModelPost() {
    }

    public ModelPost(String userName, String post, String webLink, String avatarURL, Long timestamp) {
        this.userName = userName;
        this.post = post;
        this.webLink = webLink;
        this.avatarURL = avatarURL;
        this.timestamp = timestamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}

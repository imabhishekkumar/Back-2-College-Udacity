package com.android.imabhishekkumar.back2college.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ModelPost  {
    private String name;
    private String details;
    private String multimediaURL;
    private String avatar;
    private ArrayList<String> postTo;
    private String uid;
    private String time;
    private Long timestamp;

    public ModelPost() {
    }

    public ModelPost(String name, String details, String multimediaURL, String avatar, ArrayList<String> postTo, String uid, String time, Long timestamp) {
        this.name = name;
        this.details = details;
        this.multimediaURL = multimediaURL;
        this.avatar = avatar;
        this.postTo = postTo;
        this.uid = uid;
        this.time = time;
        this.timestamp = timestamp;
    }

    public ArrayList<String> getPostTo() {
        return postTo;
    }

    public void setPostTo(ArrayList<String> postTo) {
        this.postTo = postTo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getMultimediaURL() {
        return multimediaURL;
    }

    public void setMultimediaURL(String multimediaURL) {
        this.multimediaURL = multimediaURL;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}

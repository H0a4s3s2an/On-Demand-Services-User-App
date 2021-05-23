package com.example.sample.usertribe.Model;

/**
 * Created by Hassan Javaid on 9/5/2018.
 */

public class Review {
    public String comment;
    public String uid;
    public String rating;
    public String username;

    public Review() {
    }

    public Review(String comment, String uid, String rating, String username) {
        this.comment = comment;
        this.uid = uid;
        this.rating = rating;
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

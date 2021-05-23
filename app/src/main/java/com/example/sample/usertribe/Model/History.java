package com.example.sample.usertribe.Model;

/**
 * Created by Hassan Javaid on 9/4/2018.
 */

public class History {
    public double price;
    public String status, time,category,image,uid;


    public History() {
    }

    public History(String uid, double price, String status, String time, String category, String image) {
        this.uid=uid;
        this.price = price;
        this.status = status;
        this.time = time;
        this.category = category;
        this.image=image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

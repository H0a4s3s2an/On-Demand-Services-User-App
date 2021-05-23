package com.example.sample.usertribe.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hassan Javaid on 7/5/2018.
 */

public class Provider  {
    private String uid;
    private String firstname;
    private String lastname;
    private String email;
    private String img;
    private String rating;
    private String number;
    private String serviceId;
    public Provider()
    {

    }

    public Provider(String uid, String firstname, String lastname, String email, String img, String rating, String number, String serviceId) {
        this.uid=uid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.img = img;
        this.rating = rating;
        this.number = number;
        this.serviceId = serviceId;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}

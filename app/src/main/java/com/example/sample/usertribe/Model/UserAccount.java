package com.example.sample.usertribe.Model;

/**
 * Created by Hassan Javaid on 4/17/2018.
 */

public class UserAccount {
public String uid;
public String fname;
public String lname;
public String email;
public String password;
public String mobile;
public String picture;
public double latitude;
public double longitude;

public UserAccount()
{

}


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public UserAccount(String uid, String fname, String lname, String email, String password, String picture,double latitude,double longitude) {
        this.uid = uid;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
        this.picture = picture;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}

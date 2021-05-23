package com.example.sample.usertribe.Model;

/**
 * Created by Hassan Javaid on 6/12/2018.
 */

public class Category {
    String title, image, description;

    //constructor
    public Category(){}

    public Category(String title, String image, String description) {
        this.title = title;
        this.image = image;
        this.description = description;
    }
    //getter and setters press Alt+Insert

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

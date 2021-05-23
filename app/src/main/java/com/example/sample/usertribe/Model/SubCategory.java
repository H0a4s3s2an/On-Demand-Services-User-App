package com.example.sample.usertribe.Model;

/**
 * Created by Hassan Javaid on 6/17/2018.
 */

public class SubCategory {
    String icon,name,categoryId;
    public SubCategory(){}
    public SubCategory(String icon, String name,String categoryId) {
        this.icon = icon;
        this.name = name;
        this.categoryId=categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

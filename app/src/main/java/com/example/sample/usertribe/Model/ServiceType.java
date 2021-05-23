package com.example.sample.usertribe.Model;

/**
 * Created by Hassan Javaid on 7/1/2018.
 */

public class ServiceType {
    String name,price,subCategoryId;
    public ServiceType(){}

    public ServiceType(String name, String price, String subCategoryId) {
        this.name = name;
        this.price = price;
        this.subCategoryId = subCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }
}

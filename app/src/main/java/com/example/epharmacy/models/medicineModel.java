package com.example.epharmacy.models;

public class medicineModel {

    private String name, price;

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

    public medicineModel(String name, String price) {
        this.name = name;
        this.price = price;
    }
}

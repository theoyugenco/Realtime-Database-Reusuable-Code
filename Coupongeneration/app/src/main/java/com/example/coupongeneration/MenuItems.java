package com.example.coupongeneration;

public class MenuItems {
    String name;
    Double price;

    public MenuItems(){
        this.name = "nothing";
        this.price = 0.0;
    }

    public MenuItems(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }
}

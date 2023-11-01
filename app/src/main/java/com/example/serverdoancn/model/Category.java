package com.example.serverdoancn.model;

public class Category {
    private String Name;
    private String Price;
    private String Image;
    private String discount;
    private String Description;

    public Category() {
    }

    public Category(String name, String price, String image, String discount, String description) {
        this.Name = name;
        this.Price = price;
        this.Image = image;
        this.discount = discount;
        this.Description = description;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}

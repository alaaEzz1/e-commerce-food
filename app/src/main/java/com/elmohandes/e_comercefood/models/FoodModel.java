package com.elmohandes.e_comercefood.models;

import java.io.Serializable;

public class FoodModel implements Serializable {

    private String id="";
    private String title;
    private String pic ="";
    private String description;
    private String categoryName;
    private double fee;
    private int numberInCart;
    private boolean bestSeller = false;

    public FoodModel() {
    }

    public FoodModel(String title, String pic, String description, double fee) {
        this.title = title;
        this.pic = pic;
        this.description = description;
        this.fee = fee;
    }

    public FoodModel(String id, String title, String pic, String description, String categoryName,
                     double fee, int numberInCart, boolean bestSeller) {
        this.id = id;
        this.title = title;
        this.pic = pic;
        this.description = description;
        this.categoryName = categoryName;
        this.fee = fee;
        this.numberInCart = numberInCart;
        this.bestSeller = bestSeller;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isBestSeller() {
        return bestSeller;
    }

    public void setBestSeller(boolean bestSeller) {
        this.bestSeller = bestSeller;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }
}

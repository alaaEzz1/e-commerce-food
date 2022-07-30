package com.elmohandes.e_comercefood.models;

public class CartModel {
    private String cartId,uid;
    private FoodModel foodModel;

    public CartModel() {
    }

    public CartModel(String cartId, String uid, FoodModel foodModel) {
        this.cartId = cartId;
        this.uid = uid;
        this.foodModel = foodModel;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public FoodModel getFoodModel() {
        return foodModel;
    }

    public void setFoodModel(FoodModel foodModel) {
        this.foodModel = foodModel;
    }
}

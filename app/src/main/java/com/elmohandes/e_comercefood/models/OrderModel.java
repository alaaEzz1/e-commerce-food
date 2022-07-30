package com.elmohandes.e_comercefood.models;

public class OrderModel {

    private String orderId;
    private Users users;
    private FoodModel foodModel;
    private String orderTime;

    public OrderModel() {
    }

    public OrderModel(String orderId, Users users, FoodModel foodModel, String orderTime) {
        this.orderId = orderId;
        this.users = users;
        this.foodModel = foodModel;
        this.orderTime = orderTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public FoodModel getFoodModel() {
        return foodModel;
    }

    public void setFoodModel(FoodModel foodModel) {
        this.foodModel = foodModel;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
}

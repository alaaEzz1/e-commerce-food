package com.elmohandes.e_comercefood.models;

public class Users {

    String userId,fullName,email,phone,password,imgUrl = "";
    boolean isPhoneEnabled = false;

    public Users() {
    }

    public Users(String userId, String fullName, String email, String phone, String password) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public Users(String userId, String fullName, String email,
                 String phone, String password, String imgUrl, boolean isPhoneEnabled) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.imgUrl = imgUrl;
        this.isPhoneEnabled = isPhoneEnabled;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isPhoneEnabled() {
        return isPhoneEnabled;
    }

    public void setPhoneEnabled(boolean phoneEnabled) {
        isPhoneEnabled = phoneEnabled;
    }
}

package com.elmohandes.e_comercefood.models;

public class CategoryModel {

    private String id, title ;
    private String pic;

    public CategoryModel() {
    }

    public CategoryModel(String id, String title, String pic) {
        this.id = id;
        this.title = title;
        this.pic = pic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}

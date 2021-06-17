package com.lulixe.pulari.model;

public class Category {
    private int id,marketID,active;
    private String image,name;

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getMarketID() {
        return marketID;
    }

    public void setMarketID(int marketID) {
        this.marketID = marketID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package com.lulixe.pulari.model;

public class Offers {
    private int id,headID,addedBY,active;
    private String description,image,dateTime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHeadID() {
        return headID;
    }

    public void setHeadID(int headID) {
        this.headID = headID;
    }

    public int getAddedBY() {
        return addedBY;
    }

    public void setAddedBY(int addedBY) {
        this.addedBY = addedBY;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}

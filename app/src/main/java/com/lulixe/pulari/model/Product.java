package com.lulixe.pulari.model;

import java.util.List;

public class Product {
    private int id,headID,type, varID,active,category,stock,featuered,dbID;
    private String name,image,dateTime,unit,categoryName;
    private double price;
    private ProductVarient productVarient;
    private List<ProductVarient> proVarList;
    private int customerQty;

    public int getDbID() {
        return dbID;
    }

    public void setDbID(int dbID) {
        this.dbID = dbID;
    }

    public int getCustomerQty() {
        return customerQty;
    }

    public void setCustomerQty(int customerQty) {
        this.customerQty = customerQty;
    }

    public List<ProductVarient> getProVarList() {
        return proVarList;
    }

    public void setProVarList(List<ProductVarient> proVarList) {
        this.proVarList = proVarList;
    }

    public ProductVarient getProductVarient() {
        return productVarient;
    }

    public void setProductVarient(ProductVarient productVarient) {
        this.productVarient = productVarient;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getFeatuered() {
        return featuered;
    }

    public void setFeatuered(int featuered) {
        this.featuered = featuered;
    }

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getVarID() {
        return varID;
    }

    public void setVarID(int varID) {
        this.varID = varID;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

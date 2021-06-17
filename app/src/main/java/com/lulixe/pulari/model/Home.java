package com.lulixe.pulari.model;

import java.util.List;

public class Home {
    private String date;
    private List<Offers>offers;
    private List<Category>categories;
    private List<Product>products;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Offers> getOffers() {
        return offers;
    }

    public void setOffers(List<Offers> offers) {
        this.offers = offers;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}

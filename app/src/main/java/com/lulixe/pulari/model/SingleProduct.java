package com.lulixe.pulari.model;

import java.util.List;

public class SingleProduct {
    private Product product;
    private List<Product> similarProduct;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Product> getSimilarProduct() {
        return similarProduct;
    }

    public void setSimilarProduct(List<Product> similarProduct) {
        this.similarProduct = similarProduct;
    }
}

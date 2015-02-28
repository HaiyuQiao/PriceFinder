package com.projects.pricefinder.entities;

import java.util.List;
public class Pagemap {
    CSE_thumbnail cse_thumbnail;
    List<Metatag> metatags;
    Product product;
    Offer offer;

    public Pagemap(){    }

    public List<Metatag> getMetatags() {
        return this.metatags;
    }
    public void setMetatags(List<Metatag> metatags) {
        this.metatags = metatags;
    }

    public CSE_thumbnail getCSE_thumbnail() {
        return this.cse_thumbnail;
    }
    public void setCSE_thumbnail(CSE_thumbnail cse_thumbnail) {
        this.cse_thumbnail = cse_thumbnail;
    }

    public Product getProduct() {
        return this.product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }

    public Offer getOffer() {
        return this.offer;
    }
    public void setOffer(Offer offer) {
        this.offer = offer;
    }
}

package com.projects.pricefinder.entities;

/**
 * Created by Qiao on 02/25/2015.
 * "product": [ {
 * "name": "Toshiba Satellite C50D-B 15.6\" Laptop - Black (AMD E1-2100 / 500GB HDD / 4GB RAM / Windows 8.1)",
 * "image": "http://www.bestbuy.ca/multimedia/Products/500x500/102/10299/10299018.jpg"
 *  } ]
 */
public class Product {
    private String name;
    String model;
    String image;

    public Product() {
        super();
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return this.model;
    }
    public void setModel(String model) {
        this.model = model;
    }

    public String getImage() {
        return this.image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}

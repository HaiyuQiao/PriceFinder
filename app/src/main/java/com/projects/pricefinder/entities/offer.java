package com.projects.pricefinder.entities;

/**
 * Created by Qiao on 02/25/2015.
 *"Offer": [ {
 * "pricecurrency": "CAD",
 *  "price": "$329.95",
 *  "availability": "Sold Out"
 *  } ]
 */
public class Offer {
    String price;
    String pricecurrency;
    String availability;

    public String getPrice() {
        return this.price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public String getPricecurrency() {
        return this.pricecurrency;
    }
    public void setPricecurrency(String pricecurrency) {
        this.pricecurrency = pricecurrency;
    }
    public String getAvailability() {
        return this.availability;
    }
    public void setAvailability(String availability) {
        this.availability = availability;
    }
}

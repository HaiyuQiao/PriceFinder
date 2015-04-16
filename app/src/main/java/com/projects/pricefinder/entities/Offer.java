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
    private String price;
    private String pricecurrency;
    private String availability;
    private String country;


    public Offer() {
        super();
    }

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

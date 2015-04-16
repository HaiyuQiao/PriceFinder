package com.projects.pricefinder.models;

/**
 * Created by Administrator on 04/16/2015.
 */
public class UPC {
    private String upc;
    private String itemname;
    private String alias;
    private String description;
    private String avg_price;
    private String rate_up;
    private String rate_down;
    private String valid;
    private String error;
    private String reason;


    public UPC() {
        super();
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvg_price() {
        return avg_price;
    }

    public void setAvg_price(String avg_price) {
        this.avg_price = avg_price;
    }

    public String getRate_up() {
        return rate_up;
    }

    public void setRate_up(String rate_up) {
        this.rate_up = rate_up;
    }

    public String getRate_down() {
        return rate_down;
    }

    public void setRate_down(String rate_down) {
        this.rate_down = rate_down;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

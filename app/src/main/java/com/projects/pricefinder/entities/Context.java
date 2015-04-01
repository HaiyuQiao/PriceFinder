package com.projects.pricefinder.entities;

import java.util.List;

/**
 * https://developers.google.com/resources/api-libraries/documentation/customsearch/v1/java/latest/
 */
public class Context {
    String title;
    List<Facet> facets;

    public Context(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Facet> getFacets() {
        return facets;
    }

    public void setFacets(List<Facet> facets) {
        this.facets = facets;
    }


}
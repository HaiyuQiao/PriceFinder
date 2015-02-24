package com.projects.pricefinder.entities;

public class Query {
    NextPage nextPage;
    Request request;

    public Query(){

    }

    public NextPage getNextPage() {
        return nextPage;
    }

    public void setNextPage(NextPage nextPage) {
        this.nextPage = nextPage;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

}

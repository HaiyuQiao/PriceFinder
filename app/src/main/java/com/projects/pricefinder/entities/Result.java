package com.projects.pricefinder.entities;

import java.util.ArrayList;
import java.util.List;

public class Result {
    String kind;
    Url url;
    String type;
    String template;
    Query Query;
    Context context;
    List<Item> items = new ArrayList<Item>();

    public Result(){

    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Url getUrl() {
        return url;
    }

    public void setUrl(Url url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Query getQuery() {
        return Query;
    }

    public void setQuery(Query Query) {
        this.Query = Query;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}

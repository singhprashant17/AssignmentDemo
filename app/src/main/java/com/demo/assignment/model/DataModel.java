package com.demo.assignment.model;

public class DataModel {
    private String id;
    private String uri;
    private String title;

    public DataModel(String id, String uri, String title) {
        this.id = id;
        this.uri = uri;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean equals(DataModel o) {
        return id.equalsIgnoreCase(o.getId());
    }
}

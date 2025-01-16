package com.subway2feira.API;

public enum ContentType {
    JSON ("application/json"),
    XML ("application/xml");

    private String contentType;
 
    ContentType(String contentType) {
        this.contentType = contentType;
    }
 
    public String getType() {
        return contentType;
    }
}

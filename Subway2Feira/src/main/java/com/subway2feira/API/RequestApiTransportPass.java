package com.subway2feira.API;

import java.sql.Time;

import com.google.gson.Gson;
import com.subway2feira.API.models.ClassConverter;


public class RequestApiTransportPass extends ClassConverter {
    
    private String clientID;
    private String passTypeID;
    private String expirationDate;
    private Boolean active;
    
    public RequestApiTransportPass() {
    }

    public RequestApiTransportPass(String clientID, String passTypeID, String expirationDate, Boolean active) {
        this.clientID = clientID;
        this.passTypeID = passTypeID;
        this.expirationDate = expirationDate;
        this.active = active;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getPassTypeID() {
        return passTypeID;
    }

    public void setPassTypeID(String passTypeID) {
        this.passTypeID = passTypeID;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

@Override
    public String toJsonString() {
        return new Gson().toJson(this);
    }
@Override
    public String toXmlString() {
        return String.format(
                "<TransportPass>" +
                        "<ClientId>%s</ClientId>" +
                        "<PassTypeId>%s</PassTypeId>" +
                        "<ExpirationDate>%s</ExpirationDate>" +
                        "<Active>%s</Active>" +
                        "</TransportPass>",
                this.clientID,
                this.passTypeID, this.expirationDate, this.active);

    }

}

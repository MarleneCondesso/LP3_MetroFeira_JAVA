package com.subway2feira.API;

import java.sql.Time;

import com.google.gson.Gson;
import com.subway2feira.API.models.ClassConverter;

public class RequestApi extends ClassConverter {
    
    private String customer;
    private String begin;
    private String end;
    private String price;
    private String duration;
    
    public RequestApi() {
    }

    public RequestApi(String customer, String begin, String end, String price, String duration) {
        this.customer = customer;
        this.begin = begin;
        this.end = end;
        this.price = price.toString();
        this.duration = duration.toString();
    }

    public String getCustomer() {
        return this.customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getBegin() {
        return this.begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return this.end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getPrice() {
        return this.price;

    }

    public void setPrice(String price) {
        this.price = price.toString();
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String duration) {
        this.duration = duration.toString();
    }

    public String toJsonString() {
        return new Gson().toJson(this);
    }

    public String toXmlString() {
        return String.format(
                "<TripTicket>" +
                        "<Customer>%s</Customer>" +
                        "<Begin>%s</Begin>" +
                        "<End>%s</End>" +
                        "<Price>%s</Price>" +
                        "<Duration>%s</Duration>" +
                        "</TripTicket>",
                this.customer,
                this.begin, this.end, this.price, this.duration);

    }

}

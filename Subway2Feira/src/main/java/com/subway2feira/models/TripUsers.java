package com.subway2feira.models;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;


public class TripUsers {
    private Integer id;
    private Integer userId;
    private Integer departureId;
    private Integer arriveId;
    private String duration;
    private String price;
    private LocalDate date;

    public TripUsers(Integer id, Integer userId, Integer departureId, Integer arriveId, String duration, String price, LocalDate date) {
        this.id = id;
        this.userId = userId;
        this.departureId = departureId;
        this.arriveId = arriveId;
        this.date=date;
        this.duration=duration;
        this.price=price;
        this.date=date;
    }

    public TripUsers() {
    }

    
    
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDepartureId() {
        return this.departureId;
    }

    public void setDepartureId(Integer departureId) {
        this.departureId = departureId;
    }

    public Integer getArriveId() {
        return this.arriveId;
    }

    public void setArriveId(Integer arriveId) {
        this.arriveId = arriveId;
    }
    public LocalDate getDate(){
        return this.date;
    }
    public void setDate(LocalDate date){
        this.date=date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "rteste";
    }

}

package com.subway2feira.models;

import java.time.LocalDate;
import javafx.scene.image.ImageView;

public class UserHistory {
    private Integer id;
    private Integer userId;
    private String departure;
    private String arrive;
    private String duration;
    private String price;
    private LocalDate date;  
    private ImageView qrCode;  

    public UserHistory(Integer id, Integer userId, String departure, String arrive, String duration, String price, LocalDate date, ImageView qrCode) {
        this.id = id;
        this.userId = userId;
        this.departure = departure;
        this.arrive = arrive;
        this.duration = duration;
        this.price = price;
        this.date = date;
        this.qrCode = qrCode;
    }

    public UserHistory() {
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

    public String getDeparture() {
        return this.departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrive() {
        return this.arrive;
    }

    public void setArrive(String arrive) {
        this.arrive = arrive;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ImageView getQrCode() {
        return qrCode;
    }

    public void setQrCode(ImageView qrCode) {
        this.qrCode = qrCode;
    }
    

}

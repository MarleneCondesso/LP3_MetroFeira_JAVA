package com.subway2feira.models;

import com.google.gson.annotations.SerializedName;

public class Ticket {
    
    @SerializedName("Status")
    private String status;
    @SerializedName("UserQR")
    private String userQR;

    public Ticket() {
    }

    public Ticket(String Status, String userQR) {
        this.status = Status;
        this.userQR = userQR;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String Status) {
        this.status = Status;
    }

    public String getUserQR() {
        return this.userQR;
    }

    public void setUserQR(String userQR) {
        this.userQR = userQR;
    }

}

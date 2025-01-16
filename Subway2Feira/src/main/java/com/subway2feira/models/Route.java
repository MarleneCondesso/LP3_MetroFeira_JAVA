package com.subway2feira.models;

public class Route {
    
    private Integer id;
    private Integer tripUserId;
    private Integer tripsId;

    public Route() {
    }

    public Route(Integer id, Integer tripUserId, Integer tripsId) {
        this.id = id;
        this.tripUserId = tripUserId;
        this.tripsId = tripsId;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTripUserId() {
        return this.tripUserId;
    }

    public void setTripUserId(Integer tripUserId) {
        this.tripUserId = tripUserId;
    }

    public Integer getTripsId() {
        return this.tripsId;
    }

    public void setTripsId(Integer tripsId) {
        this.tripsId = tripsId;
    }

}

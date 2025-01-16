package com.subway2feira.models;

import java.sql.Time;

public class Trips {

    private Line line;
    private Integer id;
    private StationLine arrival;
    private StationLine departure;
    private Time duration;

    public Trips() {
    }

    public Trips(StationLine arrival, StationLine departure, Line line, Time duration) {
        this.line = line;
        this.arrival = arrival;
        this.departure = departure;
        this.duration = duration;

    }

    public Trips(Integer id, Line line, StationLine arrival, StationLine departure, Time duration) {
        this.line = line;
        this.id = id;
        this.arrival = arrival;
        this.departure = departure;
        this.duration = duration;

    }

    public Line getLine() {
        return this.line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StationLine getArrival() {
        return this.arrival;
    }

    public void setArrival(StationLine arrival) {
        this.arrival = arrival;
    }

    public StationLine getDeparture() {
        return this.departure;
    }

    public void setDeparture(StationLine departure) {
        this.departure = departure;
    }

    public String ToString() {
        return String.format("%s -> %s", this.departure.getStation().getName(), this.arrival.getStation().getName());
    }

    /**
     * @return the duration
     */
    public Time getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(Time duration) {
        this.duration = duration;
    }

}

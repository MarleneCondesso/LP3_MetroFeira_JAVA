package com.subway2feira.models;

public class StationLine {

    private Integer id;
    private Line line;
    private Station station;

    public StationLine() {
    }

    public StationLine(Integer id, Line line, Station station) {
        this.id = id;
        this.line = line;
        this.station = station;
    }

    public StationLine(Station station) {
        this.station = station;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Line getLine() {
        return this.line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Station getStation() {
        return this.station;
    }

    public void setStation(Station station) {
        this.station = station;
    }
}

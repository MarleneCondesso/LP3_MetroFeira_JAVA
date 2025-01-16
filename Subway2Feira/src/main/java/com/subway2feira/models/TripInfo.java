package com.subway2feira.models;

import java.util.List;

public class TripInfo {

    private String nStations;
    private String nLines;
    private String nTime;
    private String nPrice;

    private String nDep;
    private String nArr;

    private List<Trips> tripList;

    public TripInfo(String nStations, String nLines, String nTime, String nPrice, String nDep, String nArr,
            List<Trips> tripList) {
        this.nStations = nStations;
        this.nLines = nLines;
        this.nTime = nTime;
        this.nPrice = nPrice;
        this.nDep = nDep;
        this.nArr = nArr;
        this.tripList = tripList;

    }

    public TripInfo(String nStations, String nLines, String nTime, String nPrice, String nDep, String nArr) {
        this.nStations = nStations;
        this.nLines = nLines;
        this.nTime = nTime;
        this.nPrice = nPrice;
        this.nDep = nDep;
        this.nArr = nArr;

    }

    public String getNStations() {
        return this.nStations;
    }

    public void setNStations(String nStations) {
        this.nStations = nStations;
    }

    public String getNLines() {
        return this.nLines;
    }

    public void setNLines(String nLines) {
        this.nLines = nLines;
    }

    public String getNTime() {
        return this.nTime;
    }

    public void setNTime(String nTime) {
        this.nTime = nTime;
    }

    public String getNPrice() {
        return this.nPrice;
    }

    public void setNPrice(String nPrice) {
        this.nPrice = nPrice;
    }

    public String getNDep() {
        return this.nDep;
    }

    public void setNDep(String nDep) {
        this.nDep = nDep;
    }

    public String getNArr() {
        return this.nArr;
    }

    public void setNArr(String nArr) {
        this.nArr = nArr;
    }

    @Override
    public String toString() {
        return "{" +
                " nStations='" + getNStations() + "'" +
                ", nLines='" + getNLines() + "'" +
                ", nTime='" + getNTime() + "'" +
                ", nPrice='" + getNPrice() + "'" +
                ", nDep='" + getNDep() + "'" +
                ", nArr='" + getNArr() + "'" +
                "}";
    }

    public List<Trips> getTripList() {
        return this.tripList;
    }

}

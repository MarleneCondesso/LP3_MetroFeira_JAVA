package com.subway2feira.models;

public class Line {

    private String letter;
    private String color;

    public Line() {
    }

    public Line(String letter) {
        this.letter = letter;
    }

    public Line(String letter, String color) {
        this.letter = letter;
        this.color = color;
    }

    public String getLetter() {
        return this.letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

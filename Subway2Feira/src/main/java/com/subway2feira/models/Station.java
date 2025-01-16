package com.subway2feira.models;


public class Station {

    private String name;
    private Integer id;
    private Double price;
            

    public Station() {
    }
    
    public Station(Integer id, String name, Double price) {
        this.name = name;
        this.id = id;
        this.price = price;
    }
    
    public Station( String name) {
        this.name = name;
    }
    public Station( String name, Double price) {
        this.name = name;
        this.price = price;
    }
    
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

  /**
   * @return the price
   */
  public Double getPrice() {
    return price;
  }

  /**
   * @param price the price to set
   */
  public void setPrice(Double price) {
    this.price = price;
  }
}

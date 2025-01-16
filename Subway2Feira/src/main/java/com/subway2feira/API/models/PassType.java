/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.subway2feira.API.models;

/**
 *
 * @author lucpe
 */
public class PassType {
    private String PassTypeId;
    private double Discount;
    private String Description;
    private int MinAge;
    private int MaxAge;

  /**
   * @return the PassTypeId
   */
  public String getPassTypeId() {
    return PassTypeId;
  }

  /**
   * @param PassTypeId the PassTypeId to set
   */
  public void setPassTypeId(String PassTypeId) {
    this.PassTypeId = PassTypeId;
  }

  /**
   * @return the Discount
   */
  public double getDiscount() {
    return Discount;
  }

  /**
   * @param Discount the Discount to set
   */
  public void setDiscount(double Discount) {
    this.Discount = Discount;
  }

  /**
   * @return the Description
   */
  public String getDescription() {
    return Description;
  }

  /**
   * @param Description the Description to set
   */
  public void setDescription(String Description) {
    this.Description = Description;
  }

  /**
   * @return the minAge
   */
  public int getMinAge() {
    return MinAge;
  }

  /**
   * @param minAge the minAge to set
   */
  public void setMinAge(int minAge) {
    this.MinAge = MinAge;
  }

  /**
   * @return the maxAge
   */
  public int getMaxAge() {
    return MaxAge;
  }

  /**
   * @param maxAge the maxAge to set
   */
  public void setMaxAge(int maxAge) {
    this.MaxAge = MaxAge;
  }
  
}

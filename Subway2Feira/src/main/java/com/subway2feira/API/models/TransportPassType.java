/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.subway2feira.API.models;

import java.util.ArrayList;

/**
 *
 * @author lucpe
 */
public class TransportPassType {
    private String Status;
    private ArrayList<PassType> TransportPassTypes;

  /**
   * @return the Status
   */
  public String getStatus() {
    return Status;
  }

  /**
   * @param Status the Status to set
   */
  public void setStatus(String Status) {
    this.Status = Status;
  }

  /**
   * @return the TransportPassTypes
   */
  public ArrayList<PassType> getTransportPassTypes() {
    return TransportPassTypes;
  }

  /**
   * @param TransportPassTypes the TransportPassTypes to set
   */
  public void setTransportPassTypes(ArrayList<PassType> TransportPassTypes) {
    this.TransportPassTypes = TransportPassTypes;
  }
}

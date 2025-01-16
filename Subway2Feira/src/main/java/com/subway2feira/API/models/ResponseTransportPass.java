/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.subway2feira.API.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

/**
 *
 * @author lucpe
 */
public class ResponseTransportPass {
  
  private String Status;
@SerializedName(value="TransportPass", alternate="TransportPasses")
  private ArrayList<TransportPass> TransportPass;

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
   * @return the TransportPass
   */
  public ArrayList<TransportPass> getTransportPasses() {
    return TransportPass;
  }

  /**
   * @param TransportPass the TransportPass to set
   */
  public void setTransportPass(ArrayList<TransportPass> TransportPasses) {
    this.TransportPass = TransportPasses;
  }
}

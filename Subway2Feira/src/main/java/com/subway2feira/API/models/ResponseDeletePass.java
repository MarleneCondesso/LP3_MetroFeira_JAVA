/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.subway2feira.API.models;

/**
 *
 * @author lucpe
 */
public class ResponseDeletePass {
  private String Status;
  private String Message;

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
   * @return the Message
   */
  public String getMessage() {
    return Message;
  }

  /**
   * @param Message the Message to set
   */
  public void setMessage(String Message) {
    this.Message = Message;
  }
}

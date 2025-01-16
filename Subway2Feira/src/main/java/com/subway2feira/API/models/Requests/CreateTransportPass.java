/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.subway2feira.API.models.Requests;

import java.sql.Date;

/**
 *
 * @author lucpe
 */
public class CreateTransportPass {
  private String ClientId;
  private String PassTypeId;
  private Date ExpirationDate;
  private Boolean Active;

  /**
   * @return the ClientId
   */
  public String getClientId() {
    return ClientId;
  }

  /**
   * @param ClientId the ClientId to set
   */
  public void setClientId(String ClientId) {
    this.ClientId = ClientId;
  }

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
   * @return the ExpirationDate
   */
  public Date getExpirationDate() {
    return ExpirationDate;
  }

  /**
   * @param ExpirationDate the ExpirationDate to set
   */
  public void setExpirationDate(Date ExpirationDate) {
    this.ExpirationDate = ExpirationDate;
  }

  /**
   * @return the Active
   */
  public Boolean getActive() {
    return Active;
  }

  /**
   * @param Active the Active to set
   */
  public void setActive(Boolean Active) {
    this.Active = Active;
  }
}

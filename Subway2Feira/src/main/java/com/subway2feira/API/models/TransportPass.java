/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.subway2feira.API.models;

import java.sql.Date;
import java.time.LocalDateTime;

/**
 *
 * @author lucpe
 */
public class TransportPass {
  private String Id;
  private String GroupId;
  private String ClientId;
  private String PassTypeId;
  private String ExpirationDate;
  private boolean Active;
  private String InsertDate;


  /**
   * @return the Id
   */
  public String getId() {
    return Id;
  }

  /**
   * @param Id the Id to set
   */
  public void setId(String Id) {
    this.Id = Id;
  }

  /**
   * @return the GroupId
   */
  public String getGroupId() {
    return GroupId;
  }

  /**
   * @param GroupId the GroupId to set
   */
  public void setGroupId(String GroupId) {
    this.GroupId = GroupId;
  }

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
  public String getExpirationDate() {
    return ExpirationDate;
  }

  /**
   * @param ExpirationDate the ExpirationDate to set
   */
  public void setExpirationDate(String ExpirationDate) {
    this.ExpirationDate = ExpirationDate;
  }

  /**
   * @return the Active
   */
  public boolean isActive() {
    return Active;
  }

  /**
   * @param Active the Active to set
   */
  public void setActive(boolean Active) {
    this.Active = Active;
  }

  /**
   * @return the InsertDate
   */
  public String getInsertDate() {
    return InsertDate;
  }

  /**
   * @param InsertDate the InsertDate to set
   */
  public void setInsertDate(String InsertDate) {
    this.InsertDate = InsertDate;
  }

  public String getValidation() {
    LocalDateTime localDateTime = LocalDateTime.parse(this.getExpirationDate());

    if (localDateTime.isAfter(LocalDateTime.now()))
      return "Valido";
    else
      return "Invalido";
  }

}

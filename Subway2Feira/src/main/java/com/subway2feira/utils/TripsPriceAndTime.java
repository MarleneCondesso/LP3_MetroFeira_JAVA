/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.subway2feira.utils;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import com.subway2feira.models.Trips;

/**
 *
 * @author lucpe
 */
public class TripsPriceAndTime {

  public static Time getTripTime(List<Trips> trips) {
    StringBuilder station = new StringBuilder();
    Long totalTripTime = 0L;

    for (Trips stations : trips) {

      totalTripTime += stations.getDuration().toLocalTime().toEpochSecond(LocalDate.now(), ZoneOffset.UTC);
      station.append(" -> " + stations.getArrival().getStation().getName());
    }

    return new java.sql.Time(totalTripTime * 1000L);

  }

  public static Double getTripPrice(List<Trips> trips) {
    Double preco = 0.0;

    for (Trips stations : trips) {
      preco += stations.getArrival().getStation().getPrice();
    }

    return preco;
  }

  public static String stationToString(List<Trips> trips) {
    StringBuilder station = new StringBuilder();
    for (Trips stations : trips) {
      station.append(" -> " + stations.getArrival().getStation().getName());
    }
    DateFormat formatter = new SimpleDateFormat("mm:ss");
    String date = formatter.format(TripsPriceAndTime.getTripTime(trips));

    station.append(String.format(" | Duração Total da Viagem : %s min.", date));
    station.append(String.format(" | Custo total da Viagem : %.2f €", TripsPriceAndTime.getTripPrice(trips)));
    return station.toString();
  }

}

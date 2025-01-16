/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.subway2feira.services;

import com.subway2feira.models.Line;
import com.subway2feira.models.Station;
import com.subway2feira.models.StationLine;
import com.subway2feira.models.Trips;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Asus
 */
public class TripService extends Service {

  /**
   * Método para converter os objetos passados como paramêtros para o Modelo
   * Trips
   *
   * @param letter letra
   * @param departureID id de partida
   * @param arrivalID id de chegada
   * @return devolve
   */
  private Trips hashMapToTrips(Object letter, Object departureID, Object arrivalID) {

    Trips t = new Trips();
    t.setLine((Line) letter);
    t.setDeparture((StationLine) departureID);
    t.setArrival((StationLine) arrivalID);

    return t;
  }

  /**
   *
   * Método para converter os objetos passados como paramêtros para o Modelo
   * Station
   *
   * @param id id
   * @param name nome
   * @param price preço
   * @return devolve
   */
  private Station hashMapToStations(Object id, Object name, Object price) {

    Station station = new Station();

    station.setId((Integer) id);
    station.setName((String) name);
    station.setPrice((Double) Double.parseDouble(price.toString()));

    return station;
  }

  /**
   *
   * Método para converter os objetos passados como paramêtros para o Modelo
   * Line
   *
   * @param letter letra
   * @param color cor
   * @return devolver
   */
  private Line hashMapToLines(Object letter, Object color) {

    Line line = new Line();
    line.setLetter((String) letter);
    line.setColor((String) color);

    return line;
  }

  /**
   * Método para retornar uma lista de todas as Viagens Ativas
   *
   * @return todas as viagens existentes na base de dados
   */
  public ArrayList<Trips> getAll() {

    ArrayList<Trips> trips = new ArrayList<>();

    ArrayList<String> values = new ArrayList();

    values.add("letter");
    values.add("departureID");
    values.add("arrivalID");

    ArrayList<HashMap<String, Object>> data = super.get("SELECT * FROM Trip", null, values);

    for (HashMap<String, Object> trip : data) {

      // Associa as chaves da Base de Dados ao valor Objeto
      // trips.add(hashMapToTrips("letter"), trip.get("departureID"),
      // trip.get("arrivalID"));
    }
    return trips;
  }

  /**
   * Método para retornar uma lista de todas estações
   *
   * @return todas as estações existentes na base de dados
   */
  public ArrayList<Station> getAllStation() {

    ArrayList<Station> stations = new ArrayList<>();

    ArrayList<String> values = new ArrayList();

    values.add("id");
    values.add("name");
    values.add("price");

    ArrayList<HashMap<String, Object>> data = super.get("SELECT * FROM Station", null, values);

    for (HashMap<String, Object> station : data) {

      // Associa as chaves da Base de Dados ao valor Objeto
      stations.add(hashMapToStations(station.get("id"), station.get("name"), station.get("price")));

    }
    return stations;
  }

  /**
   * Método para retornar uma lista de todas Linhas
   *
   * @return todas as linhas existentes na base de dados
   */
  public ArrayList<Line> getAllLine() {

    ArrayList<Line> lines = new ArrayList<>();

    ArrayList<String> values = new ArrayList();

    values.add("letter");
    values.add("color");

    ArrayList<HashMap<String, Object>> data = super.get("SELECT * FROM Line", null, values);

    for (HashMap<String, Object> line : data) {

      // Associa as chaves da Base de Dados ao valor Objeto
      lines.add(hashMapToLines(line.get("letter"), line.get("color")));

    }
    return lines;
  }

  /**
   * Método para retornar a estação .
   *
   * @param id id
   * @return a estação.
   */
  public Station getStationById(Integer id) {

    Station stations = null;

    String query = "SELECT * FROM Station WHERE id =?";

    ArrayList<Object> params = new ArrayList();

    params.add(id);

    ArrayList<String> values = new ArrayList();

    values.add("id");
    values.add("name");
    values.add("price");

    ArrayList<HashMap<String, Object>> data = super.get(query, params, values);

    if (data.size() == 1) {

      HashMap<String, Object> value = data.get(0);

      stations = hashMapToStations(value.get("id"), value.get("name"), value.get("price"));

    }
    System.out.println(stations);
    return stations;
  }

  /**
   *
   * adiciona um novo trip user uma viagem de utilizador
   *
   * @param userId id do utilizador
   * @param departure partida
   * @param arrival chegada
   * @param date data
   * @param price preço
   * @param tripHour hora da viagem
   * @return devolve viagem do user.
   */
  public boolean addTripUser(Integer userId, int departure, int arrival, LocalDate date, Time tripHour, Double price, String QrCode) {

    Date tripDate = Date.from(date.atStartOfDay().toInstant(ZoneOffset.UTC));
    String query = "[TripUsers] (userID, departureID,arriveID, tripDate, tripHour, price, QrCode) VALUES (?,?,?,?,?,?,?)";

    ArrayList<Object> params = new ArrayList();

    params.add(userId);
    params.add(departure);
    params.add(arrival);
    params.add(tripDate);
    params.add(tripHour);
    params.add(price);
    params.add(QrCode);

    return super.insert(query, params);

  }

  /**
   * Adiciona um percurso na base de dados
   *
   * @param tripUserId id do utilizador
   * @param tripId id da viagem
   * @return devolve o percurso
   */
  public boolean addPath(Integer tripUserId, Integer tripId) {
    String query = "[Path] (tripUserID, tripsID) VALUES (?,?)";

    ArrayList<Object> params = new ArrayList();

    params.add(tripUserId);
    params.add(tripId);

    return super.insert(query, params);

  }

  /**
   * Vai buscar a última viagens do user
   *
   * @return devolve as últimas viagens do user
   */
  public int getLastTripUserID() {
    String query = "SELECT IDENT_CURRENT('TripUsers') as id";

    ArrayList<String> values = new ArrayList();

    values.add("id");

    ArrayList<HashMap<String, Object>> data = super.get(query, null, values);

    Integer i = 0;

    for (HashMap<String, Object> hashMap : data) {
      BigDecimal k = new BigDecimal(String.valueOf(hashMap.get("id")));
      i = k.intValue();
    }

    return i;

  }

  /**
   * Método para retornar uma Viagem como parâmetros departureID e arrivalID.
   *
   * @param departureID id de partida
   * @param arrivalID id de chegada
   * @return devolve uma viagem respeitando os dois ids indicados.
   */
  public Trips getTripsByDepartureIDAndArrivalID(StationLine departureID, StationLine arrivalID) {

    Trips trips = null;

    String query = "SELECT * FROM [Trips] WHERE departureID = ? AND arrivalID = ?";

    ArrayList<Object> params = new ArrayList();

    params.add(arrivalID);
    params.add(departureID);

    ArrayList<String> values = new ArrayList();

    values.add("letter");
    values.add("departureID");
    values.add("arrivalID");

    ArrayList<HashMap<String, Object>> data = super.get(query, params, values);

    if (data.size() == 1) {

      HashMap<String, Object> value = data.get(0);

      trips = hashMapToTrips(value.get("letter"), value.get("departureID"), value.get("arrivalID"));

    }

    return trips;
  }

  /**
   * Método para criar registo de Viagens na Base de Dados
   *
   * @param letter letra
   * @param departureID id de partida
   * @param arrivalID id de chegada
   * @return devolve o registo de viagens na BD.
   */
  public boolean create(Line letter, StationLine departureID, StationLine arrivalID) {

    String query = "[Trips] (letter, departureID, arrivalID) VALUES (?,?,?)";

    ArrayList<Object> params = new ArrayList();
    params.add(letter);
    params.add(departureID);
    params.add(arrivalID);

    return super.insert(query, params);

  }

  public boolean addTripUserQR(Integer id, String QRCode) {

    String query = "[TripUsers] SET QRCode = ? WHERE id = ?";

    ArrayList<Object> params = new ArrayList();
    params.add(QRCode);
    params.add(id);

    return super.update(query, params);

  }

}

package com.subway2feira.API;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.subway2feira.API.Exeception.SubwayException;
import com.subway2feira.API.models.ResponseDeletePass;
import com.subway2feira.API.models.ResponseTransportPass;
import com.subway2feira.API.models.TransportPassType;
import com.subway2feira.models.*;
import com.subway2feira.utils.XmlFile;
import com.subway2feira.utils.XmlToTicket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class API extends Connect {
  // #region Contrutores

  public API() {

  }

  public API(ContentType contentType) {
    super(contentType);
  }

  public API(String user, String pass) {
    super(user, pass);
  }

  public API(String user, String pass, ContentType contentType) {
    super(user, pass, contentType);
  }

  // #endregion
  // #region Ticket
  /**
   *
   * Ticket QRcode via Rest APi
   *
   * @param rApi Dados viagem
   * @return QrCode
   * @throws IOException
   * @throws Exception
   */
  public Ticket getTicket(RequestApi rApi) throws SubwayException {

    String response = errorReturn(pConnection(rApi, this.trip));

    switch (contentType) {
      case JSON:
        return new Gson().fromJson(response, Ticket.class); // new Gson().fromJson(response,
      // ArrayList<Trips.class>());
      default:
        return new XmlToTicket(response).get();
    }
  }

  // #endregion
  // region Map
  /**
   *
   * Lista de viagens atualizada via Rest API
   *
   * @return Lista de viagens
   * @throws IOException
   * @throws Exception
   */
  public List<Trips> getTrips() throws SubwayException {

    String response = errorReturn(gConnection(this.map));

    switch (contentType) {
      case JSON:
        return convert(response); // new Gson().fromJson(response,
      // ArrayList<Trips.class>());
      default:
                try {
        return new XmlFile(response, false).getAllTrips();
      } catch (Exception e) {
        throw new SubwayException("Erro convert xml em object List<Trips>: " + e.getMessage());
      }

    }

  }

  // #endregion
  // #region Transport Pass
  /**
   *
   * Transport Pass via Rest APi
   *
   * @param rApi Dados viagem
   * @return QrCode
   * @throws IOException
   * @throws Exception
   */
  public TransportPassType getTransportPass() throws SubwayException {

    String response = errorReturn(gConnection(this.transportPassType));

    return new Gson().fromJson(response, TransportPassType.class); // new Gson().fromJson(response,
    // ArrayList<TransportPass.class>());
  }

  public ResponseTransportPass createTransportPass(RequestApiTransportPass pass) throws SubwayException {
    String response = errorReturn(pConnection(pass, this.createPass));

    return new Gson().fromJson(response, ResponseTransportPass.class);
  }

  public ResponseTransportPass getTransportPasses() throws SubwayException {
    String response = errorReturn(gConnection(this.createPass));

    return new Gson().fromJson(response, ResponseTransportPass.class);
  }

  public ResponseTransportPass getTransportPassesbyID(String ID) throws SubwayException {
    String response = errorReturn(gConnection(this.createPass + ID));

    return new Gson().fromJson(response, ResponseTransportPass.class);
  }

  public ResponseDeletePass deleteTransportPass(String ID) throws SubwayException {
    String response = errorReturn(dConnection(this.createPass + ID));

    return new Gson().fromJson(response, ResponseDeletePass.class);
  }
  public ResponseTransportPass updateTransportPass(RequestApiTransportPass pass,String ID) throws SubwayException{
    String response = errorReturn(putConnection(pass,this.createPass+ ID));

    return new Gson().fromJson(response, ResponseTransportPass.class);
  }
  // #endregion
  public List<Trips> convert(String json) throws SubwayException {

    try {
      JSONObject jsonObject = new JSONObject(json);

      JSONArray lines = (JSONArray) jsonObject.get("Lines");

      List<Line> lList = getLines(lines);

      JSONArray stations = (JSONArray) jsonObject.get("Stations");

      List<Station> sList = getStations(stations);

      JSONArray trips = (JSONArray) jsonObject.get("Trips");

      List<Trips> tList = getTrips(lList, sList, trips);

      tList.addAll(getInvertStation(tList));

      return tList;
    } catch (Exception e) {
      throw new SubwayException("Error convert request em objecto: " + e.getMessage());
    }

  }

  private List<Trips> getTrips(List<Line> lList, List<Station> sList, JSONArray trips) throws ParseException {
    DateFormat formatter = new SimpleDateFormat("mm:ss");
    List<Trips> tList = new ArrayList<>();
    for (Object iterable_element : trips) {
      JSONObject obj = (JSONObject) iterable_element;

      tList.add(new Trips(
              new StationLine(sList.stream().filter(
                      action -> action.getName().equals(obj.getString("Arrival")))
                      .findFirst().get()),
              new StationLine(sList.stream().filter(
                      action -> action.getName().equals(obj.getString("Departure")))
                      .findFirst().get()),
              lList.stream().filter(
                      action -> action.getLetter()
                              .equals(obj.getString("Line")))
                      .findFirst().get(),
              new java.sql.Time(
                      formatter.parse(obj.getString("Duration")).getTime())
      ));
    }
    return tList;
  }

  private List<Station> getStations(JSONArray stations) {
    List<Station> sList = new ArrayList<>();
    for (Object iterable_element : stations) {
      JSONObject obj = (JSONObject) iterable_element;
      sList.add(new Station(obj.getString("Name"), Double.parseDouble(obj.getString("Price"))));
    }
    return sList;
  }

  private List<Line> getLines(JSONArray lines) {
    List<Line> lList = new ArrayList<>();
    for (Object iterable_element : lines) {
      JSONObject obj = (JSONObject) iterable_element;
      lList.add(new Line(obj.getString("Line"), obj.getString("Name")));
    }
    return lList;
  }

  private List<Trips> getInvertStation(List<Trips> station) {

    List<Trips> tp = new ArrayList<>();

    station.forEach(action -> tp.add(makeTrips(action)));

    return tp;

  }

  private Trips makeTrips(Trips Trips) {
    return new Trips(Trips.getDeparture(), Trips.getArrival(), Trips.getLine(), Trips.getDuration());
  }
  // #endregion

}

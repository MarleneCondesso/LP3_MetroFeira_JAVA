package com.subway2feira.services;

import java.util.ArrayList;
import java.util.HashMap;

import com.subway2feira.models.Line;
import com.subway2feira.models.Station;
import com.subway2feira.models.StationLine;
import com.subway2feira.models.Trips;
import java.sql.Time;

public class AdminService extends Service {

    /**
     * 
     * Converte Objectos em um Objecto do tipo Trips
     * 
     * @param idTrip idTrip
     * @param idArrival idArrival
     * @param idDeparture idDeparture
     * @param stationArrId stationArrId
     * @param stationArrName stationArrName
     * @param stationArrTime stationArrTime
     * @param stationArrPrice stationArrPrice
     * @param stationDepId stationDepId
     * @param stationDepName stationDepName
     * @param stationDepTime stationDepTime
     * @param stationDepPrice stationDepPrice
     * @param letter letter
     * @param color color
     * @return devolver
     */
    private Trips hashMapToUser(Object idTrip, Object idArrival, Object idDeparture, Object stationArrId,
            Object stationArrName, Object stationDepId, Object stationDepName, Object letter, Object color, Object duration,Object arrprice, Object depprice) {

        Line line = new Line((String) letter, (String) color);

        Station stationArr = new Station((Integer) stationArrId, (String) stationArrName, (Double) Double.parseDouble(arrprice.toString()));
        Station stationDep = new Station((Integer) stationDepId, (String) stationDepName, (Double) Double.parseDouble(depprice.toString()));
        
        StationLine stationLineArr = new StationLine((Integer) idArrival, line, stationArr);
        StationLine stationLineDep = new StationLine((Integer) idDeparture, line, stationDep);

        Trips trips = new Trips((Integer) idTrip, line, stationLineArr, stationLineDep, (Time) duration);

        return trips;
    }

    /**
     * 
     * Método para retornar uma lista de todas Trips
     * 
     * @return devolver
     */
    public ArrayList<Trips> getAllTrip() {

        ArrayList<Trips> trips = new ArrayList<>();

        ArrayList<String> values = new ArrayList<>();

        values.add("idTrip");
        values.add("Duration");
        values.add("idArrival");
        values.add("idDeparture");
        values.add("stationArrId");
        values.add("stationArrName");
        values.add("stationArrPrice");
        values.add("stationDepId");
        values.add("stationDepName");
        values.add("stationDepPrice");
        values.add("letter");
        values.add("color");
        

        ArrayList<HashMap<String, Object>> data = super.get("SELECT * FROM view_Trips", null, values);

        for (HashMap<String, Object> trip : data) {

            // Associa as chaves da Base de Dados ao valor Objeto
            trips.add(hashMapToUser(trip.get("idTrip"), trip.get("idArrival"), trip.get("idDeparture"),
                    trip.get("stationArrId"), trip.get("stationArrName"), trip.get("stationDepId"),
                    trip.get("stationDepName"), trip.get("letter"), trip.get("color"), trip.get("Duration"), trip.get("stationArrPrice"),trip.get("stationDepPrice")));
        }
        return trips;

    }

    /**
     * 
     * Atualiza tabela Trip na base de dados Tabela que contem as ligações entre
     * estações
     * 
     * @param trip trip
     * @return retorna True caso do update ser sucesso ou False caso não
     */
    public boolean addOrUpdateTrip(Trips trip) {

        String query = "pd_AddOrUpdateTrip @letter = ? , @color = ?, @arrival = ?, @departure = ? , @duration = ? , @pricearrival = ?, @pricedeparture = ?";

        ArrayList<Object> params = new ArrayList<>();

        params.add(trip.getLine().getLetter());
        params.add(trip.getLine().getColor());
        params.add(trip.getArrival().getStation().getName());
        params.add(trip.getDeparture().getStation().getName());
        params.add(trip.getDuration());
        params.add(trip.getArrival().getStation().getPrice());
        params.add(trip.getDeparture().getStation().getPrice());

        return super.procedure(query, params);
    }

}

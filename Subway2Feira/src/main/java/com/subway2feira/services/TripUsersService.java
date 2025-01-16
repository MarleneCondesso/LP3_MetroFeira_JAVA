/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.subway2feira.services;

import com.subway2feira.controllers.ViewTicketController;
import com.subway2feira.models.TripUsers;
import com.subway2feira.models.UserHistory;
import java.io.ByteArrayInputStream;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author 35191
 */
public class TripUsersService extends Service {

    /**
     * Método para converter os objetos passados como paramêtros para o Modelo
     * TripUsers
     *
     * Usado para listar as viagens do Utilizador
     *
     * @param id
     * @param userId
     * @param departureId
     * @param arriveId
     * @param date
     * @param price 
     * @param tripHour 
     * 
     * @return Utilizador com os dados
     */
    private TripUsers hashMapToTripUsers(Object id, Object userId, Object departureId, Object arriveId, Object date, Object tripHour, Object price) {

        TripUsers tripUser = new TripUsers();
        //Define o tipo de data que entra para melhor conversão para localdate
        Date tripDate;
        tripDate = (Date) date;
        //Preço (money) -> bigdecimal
        BigDecimal p = (BigDecimal) price;

        //formatação da hora
        SimpleDateFormat formatter = new SimpleDateFormat("mm.ss");

        tripUser.setId((Integer) id);
        tripUser.setUserId((Integer) userId);
        tripUser.setDepartureId((Integer) departureId);
        tripUser.setArriveId((Integer) arriveId);
        tripUser.setDate((LocalDate) tripDate.toLocalDate());
        tripUser.setDuration((String) formatter.format(tripHour).concat(" min."));
        tripUser.setPrice((String) String.format("%.2f", p).concat(" €"));

        return tripUser;
    }

      /**
     * Método para converter os objetos passados como paramêtros para o Modelo
     * TripUsers
     *
     * Usado para listar as viagens do Utilizador
     *
     * @param id
     * @param userId
     * @param departureId
     * @param arriveId
     * @param date
     * @param price 
     * @param tripHour 
     * 
     * @return Utilizador com os dados
     */
    private UserHistory hashMapToUserHistory(Object id, Object userId, Object departureId, Object arriveId, Object date, Object tripHour, Object price, Object qrCode) {
        UserHistory tripUser = new UserHistory();
        //Define o tipo de data que entra para melhor conversão para localdate
        Date tripDate;
        tripDate = (Date) date;
        //Preço (money) -> bigdecimal
        BigDecimal p = (BigDecimal) price;

        //formatação da hora
        SimpleDateFormat formatter = new SimpleDateFormat("mm.ss");
        
        String base64Image = qrCode.toString().split(",")[1];
        byte[] decodeImg = Base64.getDecoder().decode(base64Image);
        
        ImageView qrImage = new ImageView();
        
        qrImage.setImage(new Image(new ByteArrayInputStream(decodeImg)));
        qrImage.setFitHeight(150);
        qrImage.setFitWidth(150);
        tripUser.setId((Integer) id);
        tripUser.setUserId((Integer) userId);
        tripUser.setDeparture((String) departureId);
        tripUser.setArrive((String) arriveId);
        tripUser.setDate((LocalDate) tripDate.toLocalDate());
        tripUser.setDuration((String) formatter.format(tripHour).concat(" min."));
        tripUser.setPrice((String) String.format("%.2f", p).concat(" €"));
        tripUser.setQrCode((ImageView)qrImage);

        return tripUser;
    }

 

    /**
     * Método de inserir na tabela TripUser 
     * @param userId id de utilizador
     * @param departureId id de partida
     * @param arriveId id de chegada
     * @return  devolve viagem do utilizador.
     */
    public boolean createTripUser(Integer userId, Integer departureId, Integer arriveId) {

        String query = "TripUsers (userID, departureID, arriveID) VALUES(?,?,?,?,?,?)";

        ArrayList<Object> params = new ArrayList<>();

        params.add(userId);
        params.add(departureId);
        params.add(arriveId);

        return super.insert(query, params);
    }

    /**
     * Método para ir buscar todas as viagens
     * @return Lista de viagens
     */
    public ArrayList<TripUsers> getAllTrip() {

        ArrayList<TripUsers> trips = new ArrayList<>();
        ArrayList<String> values = new ArrayList();

        values.add("id");
        values.add("userID");
        values.add("departureID");
        values.add("arriveID");
        values.add("tripDate");
        values.add("tripHour");
        values.add("price");

        ArrayList<HashMap<String, Object>> data = super.get("SELECT * FROM TripUsers", null, values);

        for (HashMap<String, Object> trip : data) {

            // Associa as chaves da Base de Dados ao valor Objeto
            trips.add(hashMapToTripUsers(trip.get("id"), trip.get("userID"),
                    trip.get("departureID"), trip.get("arriveID"), trip.get("tripDate"), trip.get("tripHour"), trip.get("price")));

        }
        return trips;
    }

/**
     * Método para retornar as Viagens que o Utilizador realizou passando como
     * paramêntro o id.
     *
     * @param id id
     * @return o as viagens do utilizador.
     */
    public List<UserHistory> getTripUsersByUser(Integer id) {

        List<UserHistory> listTripsUser = new ArrayList<>();

        String query = "SELECT * FROM view_TripHistory WHERE userID =?";
        
        ArrayList<Object> params = new ArrayList();

        params.add(id);

        ArrayList<String> values = new ArrayList();


        values.add("userID");
        values.add("TripUsersID");
        values.add("TripARR");
        values.add("TripDEP");
        values.add("TripDate");
        values.add("Time");
        values.add("Price");
        values.add("QRCode");

        ArrayList<HashMap<String, Object>> data = super.get(query, params, values);

        for (HashMap<String, Object> tripUser : data) {

            listTripsUser.add(hashMapToUserHistory(tripUser.get("TripUsersID"), tripUser.get("userID"),
               tripUser.get("TripDEP"), tripUser.get("TripARR"), tripUser.get("TripDate"), tripUser.get("Time"), tripUser.get("Price"), tripUser.get("QRCode")));
        }

        return listTripsUser;
    }
}

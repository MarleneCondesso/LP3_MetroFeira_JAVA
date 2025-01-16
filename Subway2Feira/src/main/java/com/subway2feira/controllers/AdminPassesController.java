/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.subway2feira.controllers;

import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.subway2feira.API.API;
import com.subway2feira.API.RequestApiTransportPass;
import com.subway2feira.API.Exeception.SubwayException;
import com.subway2feira.API.models.ResponseTransportPass;
import com.subway2feira.API.models.TransportPass;
import com.subway2feira.utils.AlertBoxController;
import static java.util.stream.Collectors.toList;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import net.bytebuddy.asm.Advice.Return;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class AdminPassesController implements Initializable {

    @FXML
    private HBox principalPane;

    @FXML
    private TableView<TransportPass> tableAtive;

    @FXML
    private TableView<TransportPass> tableRequest;

    @FXML
    private TableColumn<TransportPass, String> passTypeId;

    @FXML
    private TableColumn<TransportPass, String> clientId;

    @FXML
    private TableColumn<TransportPass, String> expirationDate;

    @FXML
    private TableColumn<TransportPass, String> clientId1;

    @FXML
    private TableColumn<TransportPass, String> expirationDate1;

    @FXML
    private TableColumn<TransportPass, String> passTypeId1;

    @FXML
    private TableColumn<TransportPass, String> info;

    API api;
    SimpleDateFormat formatter;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        this.api = new API();

        passTypeId.setCellValueFactory(new PropertyValueFactory<>("PassTypeId"));
        clientId.setCellValueFactory(new PropertyValueFactory<>("ClientId"));
        expirationDate.setCellValueFactory(new PropertyValueFactory<>("ExpirationDate"));

        passTypeId1.setCellValueFactory(new PropertyValueFactory<>("PassTypeId"));
        clientId1.setCellValueFactory(new PropertyValueFactory<>("ClientId"));
        expirationDate1.setCellValueFactory(new PropertyValueFactory<>("ExpirationDate"));

        info.setCellValueFactory(info -> new SimpleStringProperty(info.getValue().getValidation()));

        info.setCellFactory(updateInfo());

        this.populationTables();

    }

    private Callback<TableColumn<TransportPass, String>, TableCell<TransportPass, String>> updateInfo() {

        return param -> new TableCell<TransportPass, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                if (item != "Valido") {
                    this.setText(item);
                    this.setStyle("-fx-background-color:red;-fx-alignment: center ;");
                } else {
                    this.setText(item);
                    this.setStyle("-fx-background-color:green;-fx-alignment: center ;");
                }
            }
        };
    }

    private void populationTables() {

        try {

            ResponseTransportPass transportPass = this.api.getTransportPasses();

            if (!transportPass.getStatus().equals("OK")) {

                new AlertBoxController(Alert.AlertType.ERROR, "FAIL",
                        String.format("Erro na atualização passe %s", transportPass.getStatus()));

                return;
            }

            updateTableRequest(transportPass.getTransportPasses());

            updateTableActive(transportPass.getTransportPasses());

        } catch (SubwayException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @FXML
    void Aprove(ActionEvent event) {

        List<TransportPass> TransportPass = tableRequest.getItems();

        try {

            updatePassToActive(TransportPass);

            ResponseTransportPass transportPass = this.api.getTransportPasses();

            updateTableRequest(transportPass.getTransportPasses());

        } catch (Exception e) {

        }

    }

    @FXML
    void updatePass(ActionEvent event) {

        List<TransportPass> t = tableAtive.getItems()
                .filtered(predicate -> predicate.getValidation().equals("Invalido"));
    }

    private void updatePassToActive(List<TransportPass> tPasses) {

        try {

            for (TransportPass transportPass : tPasses) {

                ResponseTransportPass responseTransportPass = this.api.updateTransportPass(
                        makeRequestTransportPass(transportPass),
                        transportPass.getId());

                if (!responseTransportPass.getStatus().equals("OK"))
                    new AlertBoxController(Alert.AlertType.ERROR, "FAIL",
                            String.format("Erro na atualização passe %s", transportPass.getClientId()));
            }

        } catch (SubwayException e) {
            System.out.println("error: " + e.getMessage());
        }

    }

    private RequestApiTransportPass makeRequestTransportPass(TransportPass transportPass) {

        return new RequestApiTransportPass(
                transportPass.getClientId(),
                transportPass.getPassTypeId(),
                transportPass.getExpirationDate(),
                true);

    }

    private void updateTableRequest(ArrayList<TransportPass> transportPass) {

        List<TransportPass> transportPasses = transportPass
                .stream()
                .filter(predicate -> filterDate(predicate)).collect(toList());

        tableRequest.getItems().clear();
        tableRequest.getItems().addAll(transportPasses);

    }

    private boolean filterDate(TransportPass transportPass) {

        return !transportPass.isActive() && LocalDateTime
                .parse(transportPass.getExpirationDate())
                .isAfter(LocalDateTime.now());

    }

    private void updateTableActive(ArrayList<TransportPass> transportPass) {

        List<TransportPass> transportPasses = transportPass
                .stream()
                .filter(predicate -> predicate.isActive())
                .collect(toList());

        tableAtive.getItems().clear();
        tableAtive.getItems().addAll(transportPasses);

    }
}

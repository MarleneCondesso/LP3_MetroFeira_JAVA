package com.subway2feira.controllers;

import com.subway2feira.models.Line;
import com.subway2feira.models.Station;
import com.subway2feira.models.Trips;
import com.subway2feira.services.AdminService;
import com.subway2feira.services.TripService;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author 35191
 */
public class ListXMLController implements Initializable {

    @FXML
    private Pane pane;
    @FXML
    private Tab stations;
    @FXML
    private TableView<Station> tvStations;
    @FXML
    private TableColumn<Station, String> columnName;
    @FXML
    private TableColumn<Station, Double> columnPrice;
    @FXML
    private Tab lines;
    @FXML
    private TableView<Line> tvLines;
    @FXML
    private TableColumn<Line, String> letter;
    @FXML
    private TableColumn<Line, String> color;
    @FXML
    private Tab trip;
    @FXML
    private TableView<Trips> tvTrip;
    @FXML
    private TableColumn<Trips, String> letters;
    @FXML
    private TableColumn<Trips, String> departure;
    @FXML
    private TableColumn<Trips, String> arrive;
    @FXML
    private TableColumn<Trips, String> duration;

    private TripService tripService = new TripService();
    private AdminService adminService = new AdminService();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshTableLines();
        refreshTableStations();
        refreshTableTrip();
    }

    /**
     * Método para atualização dos dados na tabela de Linhas Jogadores por Época
     */
    public void refreshTableLines() {
        ObservableList obsList = FXCollections.observableArrayList(tripService.getAllLine());
        configColumnsLines();
        tvLines.getItems().setAll(obsList);
    }

    /**
     * Método de configuração dos dados na tabela de Linhas Jogadores
     */
    private void configColumnsLines() {
        letter.setCellValueFactory(new PropertyValueFactory<>("letter"));
        color.setCellValueFactory(new PropertyValueFactory<>("color"));
    }

    /**
     * Método para atualização dos dados na tabela de Estações Equipa por
     * Jornada
     */
    public void refreshTableStations() {
        ObservableList obsList = FXCollections.observableArrayList(tripService.getAllStation());
        configColumnsStations();
        tvStations.getItems().setAll(obsList);
    }

    /**
     * Método de configuração dos dados na tabela de Estações Equipa
     */
    private void configColumnsStations() {
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Método para atualização dos dados na tabela de Percurso
     */
    public void refreshTableTrip() {
        ObservableList obsList = FXCollections.observableArrayList(adminService.getAllTrip());
        configColumnsTrips();
        tvTrip.getItems().setAll(obsList);
    }

    /**
     * Método de configuração dos dados na tabela de Percurso
     */
    private void configColumnsTrips() {
        letter.setCellValueFactory(letter -> new SimpleObjectProperty(letter.getValue().getLetter()));
        arrive.setCellValueFactory(arrive -> new SimpleObjectProperty(arrive.getValue().getArrival().getStation().getName()));
        departure.setCellValueFactory(departure -> new SimpleObjectProperty(departure.getValue().getDeparture().getStation().getName()));
        duration.setCellValueFactory(new PropertyValueFactory<>("duration"));

    }

    /**
     * Método para fechar a janela
     *
     * @param btn botão
     * @throws IOException excepção
     */
    public void closeModalView(Button btn) throws IOException {
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.close();
    }
}

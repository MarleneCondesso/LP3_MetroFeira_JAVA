package com.subway2feira.controllers;

import com.subway2feira.main.App;
import com.subway2feira.models.Station;
import java.net.URL;
import java.util.ResourceBundle;

import com.subway2feira.models.TripUsers;
import com.subway2feira.models.Trips;
import com.subway2feira.models.User;
import com.subway2feira.services.AdminService;
import com.subway2feira.services.TripService;
import com.subway2feira.services.TripUsersService;
import com.subway2feira.services.UserService;
import com.subway2feira.utils.Session;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import static java.util.Objects.isNull;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class HistoryTravelController implements Initializable {

    @FXML
    private TableView<TripUsers> table;
    @FXML
    private TableColumn<TripUsers, String> arrive;

    @FXML
    private TableColumn<TripUsers, String> departure;

    @FXML
    private TableColumn<TripUsers, String> duration;
    @FXML
    private TableColumn<TripUsers, String> price;
    @FXML
    private TableColumn<TripUsers, LocalDate> date;
    @FXML
    private TableColumn<TripUsers, Image> qrCode;

    private User user = null;

    private TripUsers tripUsers;
    private Station departur;
    private Station arrival;
    private List<Trips> trips;

    private ObservableList obsList;

    private UserService userService = new UserService();

    private TripService tripService = new TripService();

    private TripUsersService service = new TripUsersService();

    private AdminService adminService;

    private Stage stage;

    private Scene scene;

    private ListClientController listClientController;

    private Integer position;

    /**
     * Método para passar valores para historyController
     *
     * @param user User
     * @param pos Posição
     * @param listClientController Controlador
     */
    public void setView(User user, Integer pos, ListClientController listClientController) {
        this.listClientController = listClientController;

        if (!isNull(user)) {
            this.user = user;
            this.position = pos;
        } else {
            this.user = null;
            this.position = null;
        }
        refreshTableClient(user);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        String admin = "Administrador";
        String type = Session.getType();
        
        if (!type.equals(admin)) {
            refreshTable();
        }

    }

    /**
     * Método para atualização dos dados na tabela
     */
    public void refreshTable() {
        user = userService.getUserByEmail(Session.getEmail());
        obsList = FXCollections.observableArrayList(service.getTripUsersByUser(user.getId()));
        configColumns();
        table.getItems().addAll(obsList);
    }

    /**
     * Método para atualização dos dados na tabela por cliente
     * @param user utilizador
     */
    public void refreshTableClient(User user) {
        if (isNull(user)) {
            user = userService.getUserByEmail(Session.getEmail());
        }
        obsList = FXCollections.observableArrayList(service.getTripUsersByUser(user.getId()));
        configColumns();
        table.getItems().addAll(obsList);
    }

    /**
     * Método de configuração dos dados na tabela
     */
    private void configColumns() {
        departure.setStyle("-fx-alignment: CENTER; -fx-font-size:20px;");
        departure.setCellValueFactory(new PropertyValueFactory<>("departure"));
        arrive.setStyle("-fx-alignment: CENTER; -fx-font-size:20px;");
        arrive.setCellValueFactory(new PropertyValueFactory<>("arrive"));
        duration.setStyle("-fx-alignment: CENTER; -fx-font-size:20px;");
        duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        price.setStyle("-fx-alignment: CENTER; -fx-font-size:20px;");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        date.setStyle("-fx-alignment: CENTER; -fx-font-size:20px;");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        qrCode.setStyle("-fx-alignment: CENTER;");
        qrCode.setCellValueFactory(new PropertyValueFactory<>("qrCode"));
    }

    /**
     * Método para abrir uma nova janela
     *
     * @param fxml Fxml
     * @throws IOException Excepção
     */
    private void showModalView(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(App.viewsPath + fxml + ".fxml"));
        fxmlLoader.load();
        scene = new Scene(fxmlLoader.getRoot());
        stage = new Stage();
        stage.getIcons().add(new Image("/com/subway2feira/img/subwayIcon.png"));
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Método para fechar a janela
     *
     * @param btn Botão
     * @throws IOException Excepção
     */
    public void closeModalView(Button btn) throws IOException {
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.close();
    }
}

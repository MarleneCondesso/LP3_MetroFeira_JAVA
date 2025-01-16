/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.subway2feira.controllers;

import com.subway2feira.controllers.HistoryTravelController;
import com.subway2feira.main.App;
import com.subway2feira.models.User;
import com.subway2feira.services.TripService;
import com.subway2feira.services.TripUsersService;
import com.subway2feira.services.UserService;
import com.subway2feira.utils.Session;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
 * @author 35191
 */
public class ListClientController implements Initializable {

    @FXML
    private TableView<User> table;
    @FXML
    private TableColumn<User, String> name;
    @FXML
    private TableColumn<User, String> email;
    private User user = new User();

    private ObservableList obsList;

    private UserService userService = new UserService();

    private Stage stage;

    private Scene scene;

    private HistoryTravelController historyController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        table.setOnMouseClicked(event -> {
            user = table.getSelectionModel().getSelectedItem();
            Integer position = table.getSelectionModel().getSelectedIndex();
            if (event.getClickCount() == 2) {
                try {
                    showModalViewClient("historyTravel", user, position);
                } catch (IOException ex) {
                    System.out.println("Erro: Ao abrir o histórico detalhado de viagens.");
                }
            }
        });

        refreshTable();
    }

    /**
     * Método para atualização dos dados na tabela
     */
    public void refreshTable() {
        obsList = FXCollections.observableArrayList(userService.getAllClient());
        configColumns();
        table.getItems().addAll(obsList);
    }

    /**
     * Método de configuração dos dados na tabela
     */
    private void configColumns() {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    /**
     * Método para abrir uma nova janela
     *
     * @param fxml fxml
     * @param pos posição
     * @param user  utilizador
     * 
     * @throws IOException excepção
     */
    private void showModalViewClient(String fxml, User user, Integer pos) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(App.viewsPath + fxml + ".fxml"));
        fxmlLoader.load();

        historyController = fxmlLoader.getController();
        historyController.setView(user, pos, this);

        scene = new Scene(fxmlLoader.getRoot());
        stage = new Stage();
        stage.getIcons().add(new Image("/com/subway2feira/img/subwayIcon.png"));
        stage.setScene(scene);
        stage.show();
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

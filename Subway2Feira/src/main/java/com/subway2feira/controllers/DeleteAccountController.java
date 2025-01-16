/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.subway2feira.controllers;

import com.subway2feira.main.App;
import com.subway2feira.models.User;
import com.subway2feira.services.TripUsersService;
import com.subway2feira.services.UserService;
import com.subway2feira.utils.AlertBoxController;
import com.subway2feira.utils.Session;
import java.io.IOException;
import java.net.URL;
import static java.util.Objects.isNull;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author 35191
 */
public class DeleteAccountController implements Initializable {

    @FXML
    private Button btnDelete;

    @FXML
    private Label txtEmail;

    @FXML
    private Label txtName;

    @FXML
    private Label txtNif;

    @FXML
    private TextField txtNrTravel;

    private Stage stage;

    private Scene scene;

    private User user;

    private ObservableList obsList;

    private UserService service = new UserService();
    private TripUsersService serviceTripUsers = new TripUsersService();

    /**
     * Ação do botão elimar conta de utlizador e returnar ao Login
     *
     * @param event Evento
     */
    @FXML
    void deleteAccount(ActionEvent event) {
        String email = user.getEmail();
        String state = "0";

        AlertBoxController alertaConf = new AlertBoxController(Alert.AlertType.CONFIRMATION, "Atenção ", user.getName() + " " + "tem a certeza que pretende desativar a sua conta?");

        if (alertaConf.getAlert().getResult().getText().equals("OK")) {
            try {
                showModalView("login");
                
                Boolean response = service.disableByEmail(state, email);

                if (response) {
                    
                    AlertBoxController alertaConf1 = new AlertBoxController(Alert.AlertType.CONFIRMATION, "SUCESSO",
                            "A sua conta " + user.getName() + "foi eliminada!");
                    try {
                        showModalView("login");
                    } catch (IOException ex) {
                        System.out.println("ERRO: AO ABRIR LOGIN" + ex);
                    }
                    try {
                        closeModalView(btnDelete);
                    } catch (IOException ex) {
                        System.out.println("ERRO: AO FECHAR A JANELA" + ex);
                    }

                } else {
                    AlertBoxController alertaErr = new AlertBoxController(Alert.AlertType.ERROR, "ERRO",
                            "Não foi possível eliminar o Utilizador.");
                }

            } catch (IOException ex) {
                System.err.println("ERRO");
            }
        } else if (alertaConf.getAlert().getResult().getText().equals("CANCEL")) {
            alertaConf.getAlert().close();
        }

    }

    @FXML
    void onMouseOut(MouseEvent event) {
        btnDelete.setStyle("-fx-background-color: #46a049;-fx-text-fill: white;-fx-border-width: 5; -fx-background-radius: 80;");

    }

    @FXML
    void onMouseOver(MouseEvent event) {
        btnDelete.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-border-radius: 80; -fx-border-width: 5; -fx-background-radius: 80; -fx-border-color: #46a049;");

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        String nif = "---------";
        user = service.getUserByEmail(Session.getEmail());
        txtName.setText(String.format("Nome: %s ", user.getName()));
        txtEmail.setText(String.format("E-mail: %s ", user.getEmail()));
        if (isNull(user.getNif())) {
            txtNif.setText(String.format("Nif: %s", nif));
        } else {
            txtNif.setText(String.format("Nif: %s ", user.getNif()));
        }
        obsList = FXCollections.observableArrayList(serviceTripUsers.getTripUsersByUser(user.getId()));
        int numeroViagens = obsList.size();

        txtNrTravel.setText(String.valueOf(numeroViagens));
    }

    /**
     * Método para abrir uma nova janela
     *
     * @param fxml fxml
     * @throws IOException Erro
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
     * @param btn botão
     * @throws IOException Erro
     */
    public void closeModalView(Button btn) throws IOException {
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.close();
    }

}

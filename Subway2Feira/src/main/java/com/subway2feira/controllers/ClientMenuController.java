/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.subway2feira.controllers;

import com.subway2feira.main.App;
import com.subway2feira.utils.AlertBoxController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author lucpe
 */
public class ClientMenuController implements Initializable {
    
    @FXML
    private Button btnPlanTrip;
    @FXML
    private Button btnHistoryTravel;
    @FXML
    private Button btnPerfile;
    @FXML
    private Button btnClose;
    @FXML
    private HBox principalPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private ImageView img;
    @FXML
    private Button btnLoadFile;
    @FXML
    private Button btnRegister;
    @FXML
    private Label labelTitle;
    
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    
    @FXML
    void clientProfile(ActionEvent event) {
        try {
            stackPane.getChildren().setAll(App.loadFXML("clientProfile"));
        } catch (IOException ex) {
            System.err.println("ERRO: AO ABRIR O PERFIL DO CLIENTE");
        }
    }
    
    @FXML
    void historyTravel(ActionEvent event) {
        try {
            stackPane.getChildren().setAll(App.loadFXML("historyTravel"));
        } catch (IOException ex) {
            System.err.println("ERRO: AO ABRIR O HISTÓRICO DE VIAGENS");
        }
    }
    
    @FXML
    void planTrip(ActionEvent event) {
        try {
            stackPane.getChildren().setAll(App.loadFXML("PlanTrip"));
        } catch (IOException ex) {
            System.err.println("ERRO: AO ABRIR O AGENDAMENTO DE VIAGEM");
        }
    }
    
    @FXML
    void close(ActionEvent event) {
        AlertBoxController alertaConf = new AlertBoxController(Alert.AlertType.CONFIRMATION, "Atenção", "Tem a certeza que pretende voltar ao Login?");
        
        if (alertaConf.getAlert().getResult().getText().equals("OK")) {
            try {
                showModalView("login");
                closeModalView(btnClose);
            } catch (IOException ex) {
                System.err.println("ERRO");
            }
        } else if (alertaConf.getAlert().getResult().getText().equals("CANCEL")) {
            alertaConf.getAlert().close();
        }
        
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    /**
     * Método para abrir a view forgetPassword
     *
     * @param fxml
     * @throws IOException Erros
     */
    private void showModalView(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(App.viewsPath + fxml + ".fxml"));
        fxmlLoader.load();
        
        scene = new Scene(fxmlLoader.getRoot());
        stage = new Stage();
        stage.setTitle("Login - Subway2Feira");
        stage.getIcons().add(new Image("/com/subway2feira/img/subwayIcon.png"));
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Método para fechar a janela
     *
     * @param btn botão
     * @throws IOException Erros
     */
    public void closeModalView(Button btn) throws IOException {
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.close();
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.subway2feira.controllers;

import com.subway2feira.main.App;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author lucpe
 */
public class ClientProfileController implements Initializable {

    @FXML
    private Button btnDeleteAccount;

    @FXML
    private Button btnInformation;

    @FXML
    private Button btnUpdatePass;

    @FXML
    private StackPane stackPane;
    @FXML
    private ImageView img;
    
    private Stage stage;
    private Scene scene;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    @FXML
    void checkInformation(ActionEvent event) throws IOException {
        stackPane.getChildren().setAll(App.loadFXML("checkInformation"));

    }
    
    @FXML
    void deleteAccountUser(ActionEvent event) throws IOException {
        stackPane.getChildren().setAll(App.loadFXML("deleteAccount"));
    }
    
    @FXML
    void updatePassword(ActionEvent event) throws IOException {
        stackPane.getChildren().setAll(App.loadFXML("updatePassword"));
    }
    
    /**
     * Ação do rato no botão de Informação
     * 
     * @param event Evento
     */
    @FXML
    void onMouseOverInformation(MouseEvent event) {
        btnInformation.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-border-radius: 80; -fx-border-width: 5; -fx-background-radius: 80; -fx-border-color: #46a049;");
    }

    /**
     * Ação do rato fora do botão de Informação
     *  
     * @param event Evento
     */
    @FXML
    void onMouseOutInformation(MouseEvent event) {
        btnInformation.setStyle("-fx-background-color: #46a049;-fx-text-fill: white;-fx-border-width: 5; -fx-background-radius: 80;");
    }
    
    /**
     * Ação do rato no botão Atualziar Palavra-passe
     * 
     * @param event Evento
     */
    @FXML
    void onMouseOverUpdatePass(MouseEvent event) {
        btnUpdatePass.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: black; -fx-border-radius: 80; -fx-border-width: 5; -fx-background-radius: 80;-fx-border-color: #46a049;");
    }

    /**
     * Ação do rato fora do botão Atualizar Palavra-passe
     * 
     * @param event Evento
     */
    @FXML
    void onMouseOutUpdatePass(MouseEvent event) {
        btnUpdatePass.setStyle("-fx-background-color: #46a049;-fx-text-fill: white;-fx-border-width: 5; -fx-background-radius: 80; -fx-border-radius: 80;");
    }
    
    /**
     * Ação do rato no botão Eliminar conta
     * 
     * @param event Evento
     */
    @FXML
    void onMouseOverDeleteAccount(MouseEvent event) {
        btnDeleteAccount.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-border-radius: 80; -fx-border-width: 5; -fx-background-radius: 80;-fx-border-color: #46a049;");
    }

    /**
     * Ação do rato fora do botão Eliminar conta 
     * 
     * @param event Evento
     */
    @FXML
    void onMouseOutDeleteAccount(MouseEvent event) {
        btnDeleteAccount.setStyle("-fx-background-color: #46a049;-fx-text-fill: white;-fx-border-width: 5; -fx-background-radius: 80; -fx-border-radius: 80;");
    }
}

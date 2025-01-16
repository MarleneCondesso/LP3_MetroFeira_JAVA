/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.subway2feira.utils;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Marlene
 */
public class AlertBoxController {
    
    Alert alert;
    Optional<ButtonType> button;

    public AlertBoxController(Alert.AlertType type, String title, String message) {

        alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        button = alert.showAndWait();
    }

    public Alert getAlert() {
        return alert;
    }

    public Optional<ButtonType> getButton() {
        return button;
    } 
}

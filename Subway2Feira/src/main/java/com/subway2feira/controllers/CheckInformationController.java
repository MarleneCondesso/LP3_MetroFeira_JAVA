/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.subway2feira.controllers;

import com.subway2feira.API.API;
import com.subway2feira.API.Exeception.SubwayException;
import com.subway2feira.API.RequestApiTransportPass;
import com.subway2feira.API.models.PassType;
import com.subway2feira.API.models.ResponseTransportPass;
import com.subway2feira.API.models.TransportPass;
import com.subway2feira.API.models.TransportPassType;
import com.subway2feira.models.MakePass;
import com.subway2feira.models.User;
import com.subway2feira.services.UserService;
import com.subway2feira.utils.AlertBoxController;
import com.subway2feira.utils.Session;
import java.net.URL;
import java.time.LocalDate;
import static java.util.Objects.isNull;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author lucpe
 */
public class CheckInformationController implements Initializable {

    private User user;
    private RequestApiTransportPass pass;
    @FXML
    private Label txtEmail;

    @FXML
    private Label txtName;
    @FXML
    private Label txtNif;

    private UserService service = new UserService();
    @FXML
    private Label txtPass;

    @FXML
    private Button btnPass;

    @FXML
    private Label lb_cat;

    @FXML
    private Label lb_dis;

    @FXML
    private Label lb_state;

    API apiConnJSON;
    API apiConnXML;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String nif = "---------";
        this.apiConnJSON = new API();
        user = service.getUserByEmail(Session.getEmail());
        txtName.setText(String.format("Nome: %s ", user.getName()));
        txtEmail.setText(String.format("E-mail: %s ", user.getEmail()));
        if (isNull(user.getNif())) {
            txtNif.setText(String.format("Nif: %s", nif));
        } else {
            txtNif.setText(String.format("Nif: %s ", user.getNif()));
        }
        if (pass == null) {
            txtPass.setText(String.format("Passe: %s ", ""));
        } else {
            txtPass.setText(String.format("Passe: %s ", pass.getActive()));
        }
        btnPass.setVisible(false);

        if (txtPass.getText().equals("False") || txtPass.getText().equals(String.format("Passe: %s ", ""))) {
            btnPass.setVisible(true);
        }

        passInfo();

    }

    @FXML
    public void activatePass() throws SubwayException {

        MakePass makePass = new MakePass();
        makePass.makePassForClient(Session.getEmail());

        passInfo();

    }

    public void passInfo() {
        try {

      
            if(Session.getGuidPass() == null) return;
            ResponseTransportPass rPass = this.apiConnJSON.getTransportPassesbyID(Session.getGuidPass());

            if (rPass.getTransportPasses().size() > 0) {
                TransportPass tPass = rPass.getTransportPasses().get(rPass.getTransportPasses().size() - 1);
                

                lb_cat.setText("Categoria: " + tPass.getPassTypeId());

                TransportPassType tPassType = this.apiConnJSON.getTransportPass();

                for (PassType type : tPassType.getTransportPassTypes()) {
                    if (type.getPassTypeId().equals(tPass.getPassTypeId()))
                        lb_dis.setText("Desconto: " + type.getDiscount() * 100 + " %");
                }

                lb_state.setText(tPass.isActive() ? "Estado: Ativo" : "Estado: Não ativo");

                btnPass.setDisable(true);
            }

        } catch (SubwayException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}

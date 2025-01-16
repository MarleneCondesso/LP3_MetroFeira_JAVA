/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.subway2feira.controllers;

import com.subway2feira.models.User;
import com.subway2feira.services.UserService;
import com.subway2feira.utils.AlertBoxController;
import com.subway2feira.utils.PasswordEncryption;
import java.io.IOException;
import java.net.URL;
import static java.util.Objects.isNull;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author 35191
 */
public class PasswordController implements Initializable {

    private LoginController loginController;
    @FXML
    private Label title;
    @FXML
    private HBox principalPane;
    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnSave;

    @FXML
    private ImageView hiddePassword;

    @FXML
    private ImageView showPassword;
    
    @FXML
    private TextField txtShowPassword;
    
    private User user;

    private UserService service = new UserService();
    
    private PasswordEncryption encryption = new PasswordEncryption();
    
    public PasswordController() {
    }

    
    
    
    /**
     * Método para alterar o estilo do botão Save após o rato estar presente no
     * botão.
     * 
     * @param event evento
     */
    @FXML
    void onMouseOver(MouseEvent event) {
        btnSave.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #ffffff; -fx-border-radius: 80; -fx-border-width: 5; -fx-background-radius: 80; -border-color: #46a049;");
    }

    /**
     * Método para alterar o estilo do botão Save após o rato se ausentar do
     * botão.
     * 
     * @param event evento
     */
    @FXML
    void onMouseOut(MouseEvent event) {
        btnSave.setStyle("-fx-background-color: #46a049;-fx-text-fill: #000000; -fx-border-radius: 80;-fx-background-radius: 80;");
    }
    
    
    /**
     * Método para atribuir os valores da view Login à view forgetPassword
     * @param email email
     * @param loginCtrl controlador de login
     * @param title titulo
     */
    public void setPasswordView(String email, String title, LoginController loginCtrl) {

        this.loginController = loginCtrl;

        if (!isNull(email)) {
            this.txtEmail.setText(email);

        } else {
            this.txtEmail = null;
        }

        this.title.setText(title);
    }

    /**
     * Método de Inicialização (interface Initializable).
     *
     * @param url url
     * @param rb Resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showPassword.setImage(new Image("/com/subway2feira/img/iconShow.png"));
        hiddePassword.setImage(new Image("/com/subway2feira/img/iconHidden.png"));
        hiddePassword.setVisible(false);
        showPassword.setVisible(true);
        txtShowPassword.setVisible(false);
    }

    /*
    * Metodo do botao Save
    */
    @FXML
    void savePassword(ActionEvent event) throws IOException {
        
        validationPassword();
        
    }
    
    /*
    * Metodo para mostrar palavra-passe
    */
    @FXML
    void showPassword(MouseEvent event) {
        hiddePassword.setVisible(true);
        showPassword.setVisible(false);
        String passe = txtPassword.getText();
        txtShowPassword.setText(passe);
        txtShowPassword.setVisible(true);
        txtPassword.setVisible(false);
    }  
    
    /*
    * Metodo para esconder palavra-passe
    */
    @FXML
    void hiddePassword(MouseEvent event) {
        hiddePassword.setVisible(false);
        showPassword.setVisible(true);
        String passe = txtShowPassword.getText();
        txtPassword.setText(passe);
        txtShowPassword.setVisible(false);
        txtPassword.setVisible(true);
    }
    
    /*
    * Metodo de validacoes de password.
    */
    public void validationPassword(){
    
        String email = txtEmail.getText();
        
        if (txtEmail.getText().isEmpty() || txtPassword.getText().isEmpty()) {
            
            AlertBoxController alertError = new AlertBoxController(Alert.AlertType.ERROR, "ERRO", "Por favor, insira os dados.");
            
        }
        
        user = service.getUserByEmail(email);
        
        if (user != null){
            
            String password = txtPassword.getText();
            
            byte[] salt = encryption.stringToSalt(user.getSalt());

            String pw = encryption.getEncryptionPw(password, salt);
            
            if (service.getUserByEmailAndPassword(email, pw) != null){
            
                AlertBoxController alertaConf = new AlertBoxController(Alert.AlertType.ERROR, "ERRO", "A palavra-passe é idêntica à anterior \n Por favor, insira uma nova palavra-passe. ");
            } else {
            
                boolean response = service.updatePasswordByEmail(pw, email);
        
                if(response){
                    
                    AlertBoxController alertaConf = new AlertBoxController(Alert.AlertType.CONFIRMATION, "SUCESSO", "A palavra-passe foi alterada com SUCESSO.");
                    
                    try {
                        closeModalView(btnSave);
                    } catch (IOException ex) {
                        System.err.println("ERRO: AO FECHAR A VIEW FORGETPASSWORD!" + ex);
                        
                    }
                } else {
                    AlertBoxController alertaConf = new AlertBoxController(Alert.AlertType.ERROR, "ERRO", "A palavra-passe não foi alterada.");
                }
            }
        } else {
            
            AlertBoxController alertError = new AlertBoxController(Alert.AlertType.ERROR, "ERRO", "Não existe conta com o e-mail inserido! \n Por favor, insira um e-mail válido.");
        }
    }

    /**
     * Método para fechar a view forgetPassword
     *
     * @param btn botão
     * @throws IOException excepção
     */
    public void closeModalView(Button btn) throws IOException {
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.close();
    }

}

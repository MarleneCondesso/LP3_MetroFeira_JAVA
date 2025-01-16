
    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.subway2feira.controllers;

import com.subway2feira.main.App;
import com.subway2feira.models.User;
import com.subway2feira.services.UserService;
import com.subway2feira.utils.AlertBoxController;
import com.subway2feira.utils.PasswordEncryption;
import com.subway2feira.utils.Session;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author 35191
 */
public class UpdatePasswordController implements Initializable {

    @FXML
    private HBox principalPane;
    @FXML
    private Label title;
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private TextField txtShowPassword;
    @FXML
    private ImageView hiddePassword;
    @FXML
    private ImageView showPassword;
    @FXML
    private Button btnSave;
    
    private Scene scene;
    
    private Stage stage;
    
    private User user = new User();

    private UserService service = new UserService();
    
    private PasswordEncryption encryption = new PasswordEncryption();
    /**
     * Initializes the controller class.
     * @param url url
     * @param rb rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showPassword.setImage(new Image("/com/subway2feira/img/iconShow.png"));
        hiddePassword.setImage(new Image("/com/subway2feira/img/iconHidden.png"));
        hiddePassword.setVisible(false);
        showPassword.setVisible(true);
        txtShowPassword.setVisible(false);
        String email = Session.getEmail();
        user = service.getUserByEmail(email);
        title.setText("Alterar Palavra-passe");
        txtEmail.setText(user.getEmail());
    }    

    @FXML
    void hiddePassword(MouseEvent event) {
        hiddePassword.setVisible(false);
        showPassword.setVisible(true);
        String passe = txtShowPassword.getText();
        txtPassword.setText(passe);
        txtShowPassword.setVisible(false);
        txtPassword.setVisible(true);
    }

    @FXML
    void showPassword(MouseEvent event) {
        hiddePassword.setVisible(true);
        showPassword.setVisible(false);
        String passe = txtPassword.getText();
        txtShowPassword.setText(passe);
        txtShowPassword.setVisible(true);
        txtPassword.setVisible(false);
    }

    @FXML
    void onMouseOut(MouseEvent event) {
        btnSave.setStyle("-fx-background-color: #46a049;-fx-text-fill: white;-fx-border-width: 5; -fx-background-radius: 80;");
   
          }

    @FXML
    void onMouseOver(MouseEvent event) {
        btnSave.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-border-radius: 80; -fx-border-width: 5; -fx-background-radius: 80; -fx-border-color: #46a049;");
   
    }

    @FXML
    void savePassword(ActionEvent event) {
        if (!txtShowPassword.getText().isEmpty()) {
            txtPassword.setText(txtShowPassword.getText());
        } else 
            txtShowPassword.setText(txtPassword.getText());
        validationPassword();
    }
    
    /**
    * Metodo de validacoes de password.
    */
    public void validationPassword(){
    String email = txtEmail.getText();
        
        if (txtEmail.getText().isEmpty() || txtPassword.getText().isEmpty() || txtShowPassword.getText().isEmpty()) {
            
            AlertBoxController alertError = new AlertBoxController(Alert.AlertType.ERROR, "ERRO", "Por favor, insira os dados.");
            
        } else {
        
            user = service.getUserByEmail(email);

            if (user != null){

                String password = txtShowPassword.getText();

                byte[] salt = encryption.stringToSalt(user.getSalt());

                String pw = encryption.getEncryptionPw(password, salt);

                if (service.getUserByEmailAndPassword(email, pw) != null){

                    AlertBoxController alertaConf = new AlertBoxController(Alert.AlertType.ERROR, "ERRO", "A palavra-passe é idêntica à anterior \n Por favor, insira uma nova palavra-passe. ");
                } else {

                    boolean response = service.updatePasswordByEmail(pw, email);

                    if(response){

                        AlertBoxController alertaConf = new AlertBoxController(Alert.AlertType.CONFIRMATION, "SUCESSO", "A palavra-passe foi alterada com SUCESSO.");
                        try {
                            showModalView("login");
                        } catch (IOException ex) {
                            System.out.println("ERRO: AO ABRIR LOGIN");
                        }
                        try {
                            closeModalView(btnSave);
                        } catch (IOException ex) {
                            System.out.println("ERRO: AO FECHAR A JANELA");
                        }

                    } else {
                        AlertBoxController alertaConf = new AlertBoxController(Alert.AlertType.ERROR, "ERRO", "A palavra-passe não foi alterada.");
                    }
                }
            } else {

                AlertBoxController alertError = new AlertBoxController(Alert.AlertType.ERROR, "ERRO", "Não existe conta com o e-mail inserido! \n Por favor, insira um e-mail válido.");
            }
            
        }

    }
        /**
     * Método para abrir uma nova janela
     * @param fxml fxml
     * @throws IOException excepção 
     */
    private void showModalView(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(App.viewsPath + fxml + ".fxml"));
        fxmlLoader.load();
        scene = new Scene(fxmlLoader.getRoot());
        stage = new Stage();
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


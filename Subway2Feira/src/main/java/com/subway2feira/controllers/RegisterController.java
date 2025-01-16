package com.subway2feira.controllers;

import com.subway2feira.API.API;
import com.subway2feira.API.Exeception.SubwayException;
import com.subway2feira.API.RequestApiTransportPass;
import com.subway2feira.API.models.PassType;
import com.subway2feira.API.models.ResponseTransportPass;
import com.subway2feira.API.models.TransportPassType;
import com.subway2feira.main.App;
import com.subway2feira.models.MakePass;
import com.subway2feira.models.User;
import com.subway2feira.services.UserService;
import com.subway2feira.utils.AlertBoxController;
import com.subway2feira.utils.CheckCredentials;
import com.subway2feira.utils.PasswordEncryption;
import com.subway2feira.utils.Session;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import static java.util.Objects.isNull;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class RegisterController implements Initializable {

    @FXML
    private Button btnCreate;

    @FXML
    private Button btnLogin;

    @FXML
    private TextField txtEmail;

    @FXML
    private DatePicker dateBirth;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtNif;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private HBox principalPane;

    @FXML
    private ImageView hiddePassword;

    @FXML
    private ImageView showPassword;

    @FXML
    private TextField txtShowPassword;

    private User user;

    private UserService service = new UserService();
    API apiConnJSON = new API();
    API apiConnXML;


    private PasswordEncryption encryption = new PasswordEncryption();

     /**
     * Ação do botao Create Count
     *
     * @param event evento
     */
    @FXML
    void createCount(ActionEvent event) throws SubwayException {
        create();
       

    }
    public void create(){
         String email = txtEmail.getText();
        String name = txtName.getText();
        String pwd = txtPassword.getText();
        String nif = txtNif.getText();
        LocalDate today = LocalDate.now();
        LocalDate birth = dateBirth.getValue();

        if (email.isEmpty() || name.isEmpty() || pwd.isEmpty()) {
            new AlertBoxController(Alert.AlertType.ERROR, "ERRO",
                    "Insira os dados obrigatorios !!");
            return;
        } else if (!nif.isEmpty() && !CheckCredentials.CheckNif(nif)) {
            new AlertBoxController(Alert.AlertType.ERROR, "ERRO",
                    "Nif invalido !!");
            return;
        } else if (!email.isEmpty() && !CheckCredentials.CheckEmail(email)) {
            new AlertBoxController(Alert.AlertType.ERROR, "ERRO",
                    "Email invalido !!");
            return;
        } else if  (birth==null || birth.isAfter(today)){
                new AlertBoxController(Alert.AlertType.ERROR, "ERRO",
                "Data de nascimento invalida !!");
             return;
        } else {

            if (this.service.getUserByEmail(email) == null) {
                try {
                    createUser();
                } catch (SubwayException ex) {
                    System.out.println(ex.getMessage());
                }
            } else {
                new AlertBoxController(Alert.AlertType.ERROR, "ERRO",
                        "Email já registado !!");
            }
        }                       
    }
    
    @FXML
    void tabSwitch(KeyEvent event) {

        Node comp =  (Node)event.getSource();
        switch (event.getCode()) {
            case TAB:
                switchComp(comp);
                break;
            case ENTER:
                if(comp.getId().equals("btnCreate")) create();
                break;
            default:
                return;
        }
  
    }
    
    private void switchComp(Node comp) {
        if (comp.getId().equals("txtName"))
            txtNif.requestFocus();
        else if (comp.getId().equals("txtNif"))
            dateBirth.requestFocus();
        else if (comp.getId().equals("dateBirth")) {
            txtEmail.requestFocus();
        } else if (comp.getId().equals("txtEmail")) {
            txtPassword.requestFocus();
        }else if (comp.getId().equals("txtPassword") || comp.getId().equals("txtShowPassword")){
            btnCreate.requestFocus();
        }
    }
    /**
     * Ação do botao Login
     *
     * @param event evento
     */
    @FXML
    void loginCount(ActionEvent event) {
        try {
            principalPane.getChildren().setAll(App.loadFXML("login"));
        } catch (IOException ex) {
            System.out.println(ex + "ERRO: ABRIR LOGIN!");
        }
    }

    /**
     * Ação do rato no botao login
     *
     * @param event evento
     */
    @FXML
    void onMouseOver(MouseEvent event) {
        btnLogin.setStyle(
                "-fx-background-color: #46a049; -fx-text-fill: #ffffff; -fx-border-radius: 80; -fx-border-width: 5; -fx-background-radius: 80;");
    }

    /**
     * Ação do rato fora do botao Login
     *
     * @param event evento
     */
    @FXML
    void onMouseOut(MouseEvent event) {
        btnLogin.setStyle("-fx-background-color: transparent;-fx-text-fill: #000000");
    }

    /**
     * Método de Inicialização (interface Initializable).
     *
     * @param url url
     * @param rb rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb
    ) {
        showPassword.setImage(new Image("/com/subway2feira/img/iconShow.png"));
        hiddePassword.setImage(new Image("/com/subway2feira/img/iconHidden.png"));
        hiddePassword.setVisible(false);
        txtShowPassword.setVisible(false);

    }

    /**
     * Método para mostrar palavra-passe.
     *
     * @param event evento
     */
    @FXML
    void showPassword(MouseEvent event

    ) {
        hiddePassword.setVisible(true);
        showPassword.setVisible(false);
        String pass = txtPassword.getText();
        txtShowPassword.setText(pass);
        txtShowPassword.setVisible(true);
        txtPassword.setVisible(false);
    }

    /**
     * Método para esconder palavra-passe.
     *
     * @param event evento
     */
    @FXML
    void hiddePassword(MouseEvent event

    ) {
        hiddePassword.setVisible(false);
        showPassword.setVisible(true);
        String pass = txtShowPassword.getText();
        txtPassword.setText(pass);
        txtShowPassword.setVisible(false);
        txtPassword.setVisible(true);
    }

    /**
     * Método para guardar os valores dos compomentes no objeto Utilizador
     */
    private void setValuesToUser() {

        if (isNull(this.user)) {
            this.user = new User();
        }

        this.user.setName(txtName.getText());
        this.user.setEmail(txtEmail.getText());
        this.user.setNif(txtNif.getText());
        String password = txtPassword.getText();
        byte[] salt = null;
        this.user.setDateBirth(dateBirth.getValue());

        try {
            salt = encryption.getSalt();
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("ERRO:" + ex);
        } catch (NoSuchProviderException ex) {
            System.err.println("ERRO:" + ex);
        }

        String pw = encryption.getEncryptionPw(password, salt);
        String saltToString = Base64.getEncoder().encodeToString(salt);

        this.user.setPassword(pw);
        this.user.setSalt(saltToString);
        this.user.setType("Cliente");
    }

    /**
     * Método para Criar Utilizador
     */
    private void createUser() throws SubwayException {
        setValuesToUser();


        Boolean response = service.create(this.user.getName(), this.user.getNif(), this.user.getEmail(),
                this.user.getPassword(), this.user.getType(), this.user.getState(), this.user.getSalt(), this.user.getDateBirth());

        if (response) {
            new AlertBoxController(Alert.AlertType.CONFIRMATION, "SUCESSO",
                    "Utilizador criado com SUCESSO!");
            try {
                principalPane.getChildren().setAll(App.loadFXML("login"));
            } catch (IOException ex) {
                System.out.println("ERRO: AO ABRIR O FXML LOGIN");
            }
            
            MakePass makePass = new MakePass();
            User userPass = service.getUserByEmail(this.user.getEmail());
            makePass.makePassForClient(userPass.getEmail());
           
       } else {

        AlertBoxController alertaErr = new AlertBoxController(Alert.AlertType.ERROR, "ERRO",
                "Não foi possível criar o Utilizador.");
    
        }
    }
    public PassType selectTypePassId(LocalDate birthDate, ArrayList<PassType> passTypes) throws SubwayException{
        
        int yearBirth  = birthDate.getYear();
        
        int age = LocalDate.now().getYear() - yearBirth;

        for (int i = 0; i < passTypes.size(); i++) {
            
            if (passTypes.get(i).getMinAge() >= age || passTypes.get(i).getMaxAge() <=age) {

                return passTypes.get(i);

            }
        }
        
        throw new SubwayException("ERRO: Não encontrou um passe dentro dos limites de idade!");
      
    }
}

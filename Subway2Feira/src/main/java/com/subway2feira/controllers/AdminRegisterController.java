package com.subway2feira.controllers;

import com.subway2feira.models.User;
import com.subway2feira.services.UserService;
import com.subway2feira.utils.AlertBoxController;
import com.subway2feira.utils.PasswordEncryption;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.time.LocalDate;
import java.util.Base64;
import static java.util.Objects.isNull;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.stage.Stage;

public class AdminRegisterController implements Initializable {

    @FXML
    private Button btnCreate;

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

    private PasswordEncryption encryption = new PasswordEncryption();

    /**
     * Ação do botao Create Administrador
     *
     * @param event evento
     */
    @FXML
    void createCount(ActionEvent event) throws IOException {
        create();
        
    }

    public void create(){
        String email = txtEmail.getText();
        LocalDate birth = dateBirth.getValue();

        if (txtPassword.getText().isEmpty()) {
            txtPassword.setText(txtShowPassword.getText());
        }

        if (txtName.getText().isEmpty() || txtEmail.getText().isEmpty() || txtPassword.getText().isEmpty()) {

            AlertBoxController alertError = new AlertBoxController(Alert.AlertType.ERROR, "ERRO", "Por favor, insira os todos os seus dados.");

        } else {

            if (service.getUserByEmail(email) != null) {

                AlertBoxController alerta = new AlertBoxController(Alert.AlertType.ERROR, "ERRO",
                        "Este e-mail pertence a outra conta. \n Por favor, insira um e-mail diferente ou inicie sessão.");

            } else {
                try {
                    createUser();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
    /**
     * Método de Inicialização (interface Initializable).
     *
     * @param url url
     * @param rb rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showPassword.setImage(new Image("/com/subway2feira/img/iconShow.png"));
        hiddePassword.setImage(new Image("/com/subway2feira/img/iconHidden.png"));
        hiddePassword.setVisible(false);
        txtShowPassword.setVisible(false);
    }

    @FXML
    void tabSwitch(KeyEvent event) {

        Node comp = (Node) event.getSource();
        switch (event.getCode()) {
            case TAB:
                switchComp(comp);
                break;
            case ENTER:
                if (comp.getId().equals("btnCreate")) {
                    create();
                }
                break;
            default:
                return;
        }

    }

    private void switchComp(Node comp) {
        if (comp.getId().equals("txtName")) {
            txtNif.requestFocus();
        } else if (comp.getId().equals("txtNif")) {
            dateBirth.requestFocus();
        } else if (comp.getId().equals("dateBirth")) {
            txtEmail.requestFocus();
        } else if (comp.getId().equals("txtEmail")) {
            txtPassword.requestFocus();
        } else if (comp.getId().equals("txtPassword") || comp.getId().equals("txtShowPassword")) {
            btnCreate.requestFocus();
        }
    }

    /**
     * Método para mostrar palavra-passe.
     *
     * @param event evento
     */
    @FXML
    void showPassword(MouseEvent event) {
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
    void hiddePassword(MouseEvent event) {
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

        byte[] salt = null;

        if (isNull(this.user)) {
            this.user = new User();
        }

        this.user.setName(txtName.getText());
        this.user.setEmail(txtEmail.getText());
        this.user.setDateBirth(dateBirth.getValue());
        if (!txtNif.getText().isEmpty()) {
            this.user.setNif(txtNif.getText());
        } else {
            this.user.setNif(null);
        }

        String password = txtPassword.getText();
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
        this.user.setType("Administrador");
    }

    /**
     * Método para Criar Administrador
     */
    private void createUser() throws IOException {
        setValuesToUser();

        Boolean response = service.create(this.user.getName(), this.user.getNif(), this.user.getEmail(),
                this.user.getPassword(), this.user.getType(), this.user.getState(), this.user.getSalt(), this.user.getDateBirth());

        if (response) {

            AlertBoxController alertaConf = new AlertBoxController(Alert.AlertType.CONFIRMATION, "SUCESSO",
                    "Utilizador criado com SUCESSO!");
            closeModalView(btnCreate);
        } else {
            AlertBoxController alertaErr = new AlertBoxController(Alert.AlertType.ERROR, "ERRO",
                    "Não foi possível criar o Utilizador.");
        }

    }

    /**
     * Método para fechar a view adminRegister
     *
     * @param btn botão
     * @throws IOException Erros
     */
    public void closeModalView(Button btn) throws IOException {
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.close();
    }

}

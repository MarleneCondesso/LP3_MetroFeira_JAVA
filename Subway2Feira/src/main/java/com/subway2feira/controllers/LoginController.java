/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.subway2feira.controllers;

import com.subway2feira.API.API;
import com.subway2feira.API.ContentType;
import com.subway2feira.API.Exeception.SubwayException;
import com.subway2feira.API.RequestApiTransportPass;
import com.subway2feira.API.models.ResponseTransportPass;
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
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author Marlene
 */
public class LoginController implements Initializable {

    @FXML
    private Button btnCreate;

    @FXML
    private Button btnSessao;

    @FXML
    private HBox principalPane;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button password;

    @FXML
    private Scene scene;

    @FXML
    public Stage stage;

    @FXML
    private ImageView hiddePassword;

    @FXML
    private ImageView showPassword;

    @FXML
    private TextField txtShowPassword;

    private UserService service = new UserService();

    private PasswordEncryption encryption = new PasswordEncryption();

    private PasswordController passwordController;

    private User user;

    /**
     * Método para iniciar a view após selecionar "Registar contar".
     *
     * @param event evento
     */
    @FXML
    void registerCount(ActionEvent event) {

        try {
            principalPane.getChildren().setAll(App.loadFXML("register"));
        } catch (IOException ex) {
            System.out.println("Erro: ABRIR REGISTO!" + ex);
        }
    }

    /**
     * Método inciar sessão na aplicação.
     *
     * @param event evento
     */
    @FXML
    void sessionCount(ActionEvent event) {

        session();
    }

    private void session() {
        if (txtPassword.getText().isEmpty() || txtEmail.getText().isEmpty()) {
            new AlertBoxController(Alert.AlertType.ERROR, "ERRO",
                    "Por favor, insira os dados.");
            return;
        }

        String email = txtEmail.getText();
        if (!CheckCredentials.CheckEmail(email)) {
            new AlertBoxController(Alert.AlertType.ERROR, "ERRO",
                    "O email introduzido não é válido! \n Por favor, introduza um e-mail válido!!!.");
            return;
        }

        user = service.getUserByEmail(email);

        if (user == null) {
            new AlertBoxController(Alert.AlertType.ERROR, "ERRO",
                    "Email não existe! \n Por favor, resgiste um email.");
            return;
        }

        checkPassword(user);
    }

    /**
     * Método para alterar o estilo do botão Create após o rato estar presente
     * no botão.
     *
     * @param event evento
     */
    @FXML
    void onMouseOver(MouseEvent event) {
        btnCreate.setStyle(
                "-fx-background-color: #46a049; -fx-text-fill: #ffffff; -fx-border-radius: 80; -fx-border-width: 5; -fx-background-radius: 80;");
    }

    /**
     * Método para alterar o estilo do botão Create após o rato se ausentar do
     * botão.
     *
     * @param event evento
     */
    @FXML
    void onMouseOut(MouseEvent event) {
        btnCreate.setStyle("-fx-background-color: transparent;-fx-text-fill: #000000");
    }

    /**
     * Método para inserir a view forgetPassword após selecionar "Esqueceu-se da
     * palavra'passe."
     *
     * @param event evento
     */
    @FXML
    void forgetPassword(ActionEvent event) {

        String email = txtEmail.getText();

        try {
            showModalView("forgetPassword", email, "Nova Palavra-passe");
        } catch (IOException ex) {
            System.out.println("ERRO: AO ABRIR JANELA NOVA PALAVRA PASSE" + ex);
        }

    }

    /**
     * Metodo para mostrar a palavra-passe
     *
     * @param event evento
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

    /**
     * Metodo para esconder a palavra-passe
     *
     * @param event evento
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

    /**
     * Método de Inicialização (interface Initializable).
     *
     * @param url url
     * @param rb  resource
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showPassword.setImage(new Image("/com/subway2feira/img/iconShow.png"));
        hiddePassword.setImage(new Image("/com/subway2feira/img/iconHidden.png"));
        hiddePassword.setVisible(false);
        showPassword.setVisible(true);
        txtShowPassword.setVisible(false);

    }

    /**
     * Método para verificar o tipo de User.
     *
     * @param user utilizador
     */
    private void typeOfUser(User user) {
        String admin = "Administrador";
        String client = "Cliente";

        switch (user.getType()) {
            case "Administrador":
                openMenus(user, admin, "AdminMenu");
                break;

            case "Cliente":
                MakePass verifyPass = new MakePass();
                System.out.println("Vai verificar transport pass do cliente");
                verifyPass.makePassForClient(txtEmail.getText());
                openMenus(user, client, "ClientMenu");
                break;
        }

    }

    private void openMenus(User user, String userType, String menuName) {

        try {
            new AlertBoxController(Alert.AlertType.INFORMATION, "Administrativo",
                    String.format("Seja Bem-Vindo %s %s ao Subway 2 Feira!", userType, user.getName()));

            Session.setEmail(user.getEmail());
            Session.setId(user.getId());
            Session.setType(user.getType());

            User userPass = service.getUserByEmail(user.getEmail());

            Session.setGuidPass(userPass.getGuidPass());

            principalPane.getChildren().setAll(App.loadFXML(menuName));

        } catch (IOException x) {
            System.err.println(String.format("ERRO: AO ABRIR O MENU DE %s : %s", userType, x.getMessage()));
        }

    }

    /**
     * Verificação da password inserida pelo utilizador.
     *
     * @param user utilizador
     */
    private void checkPassword(User user) {

        String password = txtPassword.getText();

        byte[] salt = encryption.stringToSalt(user.getSalt());

        String pw = encryption.getEncryptionPw(password, salt);

        if (!user.getPassword().equals(pw)) {
            new AlertBoxController(Alert.AlertType.ERROR, "ERRO",
                    "Palavra-passe inválida.");
            return;
        }

        typeOfUser(user);
    }

    /**
     * Método para abrir a view forgetPassword
     *
     * @param fxml   fxml
     * @param titulo titulo
     * @param email  email
     * @throws IOException excepção
     */
    private void showModalView(String fxml, String email, String titulo) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(App.viewsPath + fxml + ".fxml"));
        fxmlLoader.load();

        passwordController = fxmlLoader.getController();
        passwordController.setPasswordView(email, titulo, this);

        scene = new Scene(fxmlLoader.getRoot());
        stage = new Stage();
        stage.setTitle(titulo);
        stage.getIcons().add(new Image("/com/subway2feira/img/subwayIcon.png"));
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void tabSwitch(KeyEvent event) {

        Node comp =  (Node)event.getSource();
        switch (event.getCode()) {
            case TAB:
                switchComp(comp);
                break;
            case ENTER:
                if(comp.getId().equals("btnSessao")) session();
                break;
            default:
                return;
        }
  
    }

    private void switchComp(Node comp) {
        if (comp.getId().equals("txtEmail"))
            txtPassword.requestFocus();
        else if (comp.getId().equals("txtPassword"))
            btnSessao.requestFocus();
        else
            txtEmail.requestFocus();
    }

}

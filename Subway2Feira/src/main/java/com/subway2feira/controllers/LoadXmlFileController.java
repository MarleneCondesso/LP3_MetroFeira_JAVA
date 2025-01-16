package com.subway2feira.controllers;

import com.subway2feira.API.API;
import com.subway2feira.API.ContentType;
import com.subway2feira.API.Exeception.SubwayException;
import com.subway2feira.main.App;
import com.subway2feira.models.Trips;
import com.subway2feira.services.AdminService;
import com.subway2feira.utils.AlertBoxController;
import com.subway2feira.utils.XmlFile;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * FXML Controller class
 *
 * @author lucpe
 */
public class LoadXmlFileController implements Initializable {

    @FXML
    private HBox hbox;
    @FXML
    private Button btnSelect;
    @FXML
    private Button btnUpdate;
    @FXML
    private TextField txtPath;
    @FXML
    private TableView<?> tblFile;
    @FXML
    private Button btnXmlList;

    private Stage stage;

    private Scene scene;

    @FXML
    private ComboBox<ContentType> combContentType;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

       
        this.combContentType.setItems(FXCollections.observableArrayList(ContentType.values()));
      

    }

    /**
     * Método para selecionar o Ficheiro pretendido e indicar o caminho no
     * TextField
     *
     * @author lucpe
     */
    @FXML
    void openFile(ActionEvent event) {
        final JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML FILES", "xml", "xml");
        fc.setFileFilter(filter);
        fc.showOpenDialog(fc);
        File f = fc.getSelectedFile();
        txtPath.setText(f != null ? f.getAbsolutePath() : "");

    }

    @FXML
    void UpdateFile(ActionEvent event) throws Exception {
        String path = txtPath.getText();

        if (!path.isEmpty()) {

            AdminService admin = new AdminService();
            XmlFile xml = new XmlFile(path, true);

            for (Trips allTrip : xml.getAllTrips()) {
                admin.addOrUpdateTrip(allTrip);
            }

            new AlertBoxController(Alert.AlertType.CONFIRMATION, "SUCESSO",
                    "Dados carregados com SUCESSO!");

        } else {

            System.err.println("ERRO: DADOS NÃO CARREGADOS!");
        }
    }

    @FXML
    void openListXml(ActionEvent event) {
        try {
            showModalView("listXml");
        } catch (IOException ex) {
            System.out.println("Erro: Ao abrir lista do atual XML " + ex);
        }
    }

    /**
     * Método para abrir a view listar o atual XML
     *
     * @param fxml   fxml
     * @param titulo titulo
     * @param email  email
     * @throws IOException excepção
     */
    private void showModalView(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(App.viewsPath + fxml + ".fxml"));
        fxmlLoader.load();

        scene = new Scene(fxmlLoader.getRoot());
        stage = new Stage();
        stage.setTitle("Lista do Atual XML - Subway2Feira");
        stage.getIcons().add(new Image("/com/subway2feira/img/subwayIcon.png"));
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    void UpdateWeb(ActionEvent event) {
        API api = new API(combContentType.getSelectionModel().getSelectedItem());

        try {
            AdminService admin = new AdminService();
            for (Trips trips :   api.getTrips()) {
                admin.addOrUpdateTrip(trips);
            }
          

            new AlertBoxController(Alert.AlertType.CONFIRMATION, "SUCESSO",
            "Dados carregados com SUCESSO!");

        } catch (SubwayException e) {
            new AlertBoxController(Alert.AlertType.ERROR, "FAIL",
            e.getMessage());

        }
       
        
    }
}

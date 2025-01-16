/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.subway2feira.controllers;

import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.text.html.HTMLDocument.RunElement;

import com.subway2feira.models.TripInfo;
import com.subway2feira.API.API;
import com.subway2feira.API.RequestApi;
import com.subway2feira.API.Exeception.SubwayException;
import com.subway2feira.API.models.ResponseTransportPass;
import com.subway2feira.API.models.TransportPassType;
import com.subway2feira.API.models.PassType;
import com.subway2feira.controllers.components.*;
import com.subway2feira.main.App;
import com.subway2feira.models.Station;
import com.subway2feira.models.Trips;
import com.subway2feira.services.AdminService;
import com.subway2feira.services.TripService;
import com.subway2feira.utils.AlertBoxController;
import com.subway2feira.utils.Path;
import com.subway2feira.utils.Session;
import com.subway2feira.utils.TripsPriceAndTime;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import static java.util.stream.Collectors.toList;

import java.io.Console;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author lucpe
 */
public class PlanTripController implements Initializable {

    @FXML
    private Button btnClean;

    @FXML
    private Button btnConfirm;

    @FXML
    private ComboBox<Station> cbDeparture;

    @FXML
    private ComboBox<Station> cbDestiny;
    private TripService tripService;
    private AdminService adminService;
    private List<Station> stations;
    @FXML
    private ImageView info;
    @FXML
    private AnchorPane panel;
    @FXML
    private Label lb_esta;

    @FXML
    private Label lb_linhas;

    @FXML
    private Label lb_preco;

    @FXML
    private Label lb_tempo;

    @FXML
    private Label title;
    @FXML
    private Label titleTrip;

    @FXML
    private Label lb_viagem;

    private List<Trips> trips;

    private Station departure;
    private Station arrival;
    private boolean insert;
    private boolean click = false;

    Map<String, List<TripInfo>> tMap;

    API apiConnJSON = new API();
    private Double discountPass;
    private Double priceTotalTrip;
    private Double priceWithDiscount;

    @FXML
    private Pane pn_info;

    TreeTableView<TripInfo> treeTableView;

    SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");

    TripInfo fast;
    TripInfo cheap;

    RequestApi selectTriRequestApi;

    double priceTotal = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbDeparture.setPromptText("Insira a partida.");
        cbDestiny.setPromptText("Insira o destino.");
        this.tMap = new HashMap<>();
        tripService = new TripService();
        adminService = new AdminService();
        pn_info.setVisible(false);
        this.stations = this.tripService.getAllStation().stream()
                .sorted((o1, o2) -> o1.getName().compareTo(o2.getName())).collect(toList());

        populationCombox();
    }

    private void populationCombox() {

        cbDeparture.setItems(FXCollections.observableArrayList(this.stations));

        new AutoCompleteComboBoxListener(cbDeparture, cbDestiny, this.stations);
        new AutoCompleteComboBoxListener(cbDestiny, cbDeparture, this.stations);

        cbDeparture.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (this.departure != newValue && newValue != null) {
                this.departure = newValue;
                this.insert = false;
                newTripList();
            }

        });

        cbDestiny.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (this.arrival != newValue && newValue != null) {
                this.arrival = newValue;
                this.insert = false;
                newTripList();
            }

        });

    }

    @FXML
    void cleanFields(ActionEvent event) {
        cbDeparture.getSelectionModel().clearSelection();
        cbDestiny.getSelectionModel().clearSelection();
        cbDeparture.setPromptText("Insira a partida.");
        cbDestiny.setPromptText("Insira o destino.");
        info.setVisible(true);
        panel.getChildren().clear();
        panel.getChildren().add(info);
        this.insert = false;
        pn_info.setVisible(false);
        this.tMap.clear();
        this.treeTableView = new TreeTableView<TripInfo>();
    }

    private Scene scene;
    private Stage stage;

    private PaymentController paymentController;

    /**
     * Método para abrir a view listar o atual XML
     *
     * @param fxml fxml
     * @param titulo titulo
     * @param email email
     * @throws IOException excepção
     */
    private void showModalView(String fxml, RequestApi trip) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(App.viewsPath + fxml + ".fxml"));
        fxmlLoader.load();
        Double priceFinal = 0.0;
        try {
            priceFinal = discountPass(priceTotal);
        } catch (SubwayException e) {

            e.printStackTrace();
        }
        scene = new Scene(fxmlLoader.getRoot());
        stage = new Stage();
        paymentController = fxmlLoader.getController();
        paymentController.setPaymentView(departure.getId(), arrival.getId(),
                LocalDate.now(), TripsPriceAndTime.getTripTime(trips), priceFinal, trip, this.trips, this);
        stage.setTitle("Pagamento da Viagem - Subway2Feira");
        stage.getIcons().add(new Image("/com/subway2feira/img/subwayIcon.png"));
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void save(ActionEvent event) {

        if (insert) {
            AlertBoxController buttonType;

            if (this.cheap.getTripList().equals(this.trips)) {
                buttonType = new AlertBoxController(Alert.AlertType.CONFIRMATION, "Atenção",
                        "Selecionou a viagem mais barata.");

            } else if (this.fast.getTripList().equals(this.trips)) {
                buttonType = new AlertBoxController(Alert.AlertType.CONFIRMATION, "Atenção",
                        "Selecionou a viagem mais rápida.");
            } else {
                buttonType = new AlertBoxController(Alert.AlertType.CONFIRMATION, "Atenção",
                        "Tem a certeza que pretende adicionar esta viagem?");
            }
            if (buttonType.getAlert().getResult().getText().equals("OK")) {
                newTripList();
            } else {
                insert = false;

                buttonType.getAlert().close();
            }

        } else {
            newTripList();
        }

    }

    /**
     *
     * update ao nova viagem
     *
     */
    private void newTripList() {

        this.treeTableView = new TreeTableView<TripInfo>();
        if ((this.departure == null || this.departure.getName() == null)
                || (this.arrival == null || this.arrival.getName() == null)) {
            return;
        }

        if (insert) {

            try {
                showModalView("Payment", selectTriRequestApi);
            } catch (IOException ex) {
                System.out.println("Erro: Ao abrir lista do atual XML " + ex);
            }

            //   Addtrip();
            info.setVisible(true);
            panel.getChildren().clear();
            panel.getChildren().add(info);
            pn_info.setVisible(false);
            this.insert = false;

            return;
        }
        this.insert = true;

        Path path = new Path(adminService.getAllTrip());
        List<List<Trips>> rLists;
        try {
            rLists = path.route(this.departure.getName(), this.arrival.getName());
        } catch (Exception e) {
            AlertBoxController alertError = new AlertBoxController(Alert.AlertType.ERROR, "", e.getMessage());
            return;
        }

        info.setVisible(false);
        this.title.setText("Percursos possíveis");

        VBox root = new VBox();
        // root.setMinWidth(this.panel.getWidth());
        // root.setMaxWidth(this.panel.getWidth());
        // root.fillWidthProperty();
        /**
         * todos os percusos
         */
        this.tMap = getMapTripInfo(rLists);

        /**
         * Criar colunas na tabela
         *
         */
        TableColumn();

        TreeItem<TripInfo> myRoot = new TreeItem<TripInfo>(
                new TripInfo("Root", "", "", "", "", ""));

        TreeItem<TripInfo> allpath = new TreeItem<TripInfo>(
                new TripInfo("Todos os percursos", "", "", "", "", ""));
        TreeItem<TripInfo> fastPath = new TreeItem<TripInfo>(
                new TripInfo("Percurso mais rapido", "", "", "", "", ""));
        TreeItem<TripInfo> cheapPath = new TreeItem<TripInfo>(
                new TripInfo("Percurso mais barato", "", "", "", "", ""));

        this.tMap.entrySet().forEach(action -> {
            allpath.getChildren().add(addItemTripInfo(action));
        });

        rLists.stream()
                .sorted((o1, o2) -> TripsPriceAndTime.getTripTime(o1).compareTo(TripsPriceAndTime.getTripTime(o2)))
                .findFirst().stream().forEach(action -> {
                    fastPath.getChildren().add(addItemTreeview(action));
                    this.fast = newTripInfo(action, null);
                });

        rLists.stream()
                .sorted((o1, o2) -> TripsPriceAndTime.getTripPrice(o1).compareTo(TripsPriceAndTime.getTripPrice(o2)))
                .findFirst().stream().forEach(action -> {
                    this.trips = action;
                    this.insert = true;
                    this.cheap = newTripInfo(action, null);

                    cheapPath.getChildren().add(addItemTreeview(action));
                });

        updateInfoTrip(cheap.getTripList());

        myRoot.getChildren().add(allpath);
        myRoot.getChildren().add(fastPath);
        myRoot.getChildren().add(cheapPath);

        treeTableView.setRoot(myRoot);
        treeTableView.setShowRoot(false);

        treeTableView.setPrefSize(panel.getWidth(), panel.getHeight());

        treeTableView.getSelectionModel().selectedItemProperty()
                .addListener(tableSelectItem(this.tMap));

        root.setSpacing(20);
        root.getChildren().add(treeTableView);

        panel.getChildren().add(root);

        pn_info.setVisible(true);
    }

    private void updateInfoTrip(List<Trips> action) {
        this.lb_viagem.setText(String.format("%s -> %s",
                action.stream().findFirst().get().getDeparture().getStation().getName(),
                action.get(action.size() - 1).getArrival().getStation().getName()));

        this.lb_linhas
                .setText(String.format("Linhas: %s",
                        Arrays.toString(
                                action.stream().map(mapper -> mapper.getLine().getColor()).distinct().toArray())));

        this.lb_preco
                .setText(String.format("Preço: %.2f €",
                        TripsPriceAndTime.getTripPrice(action) + action.get(0).getDeparture().getStation().getPrice()));
        priceTotal = TripsPriceAndTime.getTripPrice(action) + action.get(0).getDeparture().getStation().getPrice();
        selectTriRequestApi = new RequestApi();
        selectTriRequestApi.setCustomer(Session.getEmail());
        selectTriRequestApi.setBegin(action.get(0).getDeparture().getStation().getName());
        selectTriRequestApi.setEnd(action.get(action.size() - 1).getArrival().getStation().getName());
        selectTriRequestApi.setDuration(formatter.format(TripsPriceAndTime.getTripTime(action)));
        Double priceDiscount=0.0;
        try {
            priceDiscount = discountPass(priceTotal);
        } catch (SubwayException ex) {
            System.out.println(ex.getMessage());
        }
        selectTriRequestApi.setPrice(String.format("%.2f", priceDiscount));

        this.lb_tempo
                .setText(String.format("Tempo: %s min.", formatter.format(TripsPriceAndTime.getTripTime(action))));

        this.lb_esta.setText(String.format("Total Estações: %s", action.size()));

        if (this.cheap.getTripList().equals(this.trips)) {
            this.titleTrip.setText("Viagem mais barata");
        } else if (this.fast.getTripList().equals(this.trips)) {
            this.titleTrip.setText("Viagem mais rápida");
        } else {
            this.titleTrip.setText("");
        }

    }

    /**
     *
     * Cria um novo objecto TreeView Do tipo tripInfo
     *
     * @param list
     * @return
     */
    private TreeItem<TripInfo> addItemTripInfo(Map.Entry<String, List<TripInfo>> list) {

        TreeItem<TripInfo> item = new TreeItem<TripInfo>(list.getValue().get(0));

        for (int i = 1; i < list.getValue().size(); i++) {
            item.getChildren().add(new TreeItem<TripInfo>(list.getValue().get(i)));
        }

        return item;
    }

    /**
     *
     * criar itens
     *
     * @param action ação
     * @return devolve
     */
    private TreeItem<TripInfo> addItemTreeview(List<Trips> action) {

        TreeItem<TripInfo> treeItem = new TreeItem<TripInfo>(newTripInfo(action, null));

        for (int i = 0; i < action.size(); i++) {

            treeItem.getChildren().add(new TreeItem<TripInfo>(newTripInfo(null, action.get(i))));

        }

        return treeItem;
    }

    /**
     *
     * Quando seleciona um valor na tabela
     *
     * @param tg table
     * @return devolver
     */
    private ChangeListener<TreeItem<TripInfo>> tableSelectItem(Map<String, List<TripInfo>> tg) {
        return (ChangeListener<TreeItem<TripInfo>>) (observable, oldValue, newValue) -> {

            TreeItem<TripInfo> selectedItem = (TreeItem<TripInfo>) newValue;

            if (tg.keySet().contains(selectedItem.getValue().toString())) {

                insert = true;
                trips = selectedItem.getValue().getTripList();
                updateInfoTrip(selectedItem.getValue().getTripList());
            }
        };
    }

    /**
     *
     * adiciona colunas na tabela
     *
     */
    private void TableColumn() {
        treeTableView.getColumns().addAll(makeColumn("Nº Estações", "nStations", 150),
                makeColumn("Partida", "nDep", 150),
                makeColumn("Chegada", "nArr", 150), makeColumn("Nº Linhas", "nLines", 120),
                makeColumn("Tempo (Min.)", "nTime", 120), makeColumn("Preço", "nPrice", 120));
    }

    /**
     *
     * Cria colunas
     *
     * @param title titulo
     * @param propertyName propriedade do nome
     * @return devolver
     */
    private TreeTableColumn<TripInfo, String> makeColumn(String title, String propertyName, int width) {
        TreeTableColumn<TripInfo, String> tableColumn = new TreeTableColumn<TripInfo, String>(title);
        tableColumn.setPrefWidth(width);

        tableColumn.setCellValueFactory(new TreeItemPropertyValueFactory<TripInfo, String>(propertyName));
        return tableColumn;
    }

    /**
     *
     * Map id string e trips quando seleciona uma trip, procura no map
     *
     * @param trips viagens
     * @return devolver
     */
    private Map<String, List<TripInfo>> getMapTripInfo(List<List<Trips>> trips) {

        Map<String, List<TripInfo>> tMap = new HashMap<>();

        for (List<Trips> tList : trips) {

            List<TripInfo> tInfos = new ArrayList<>();

            TripInfo tInfo = newTripInfo(tList, null);

            tInfos.add(tInfo);

            tList.forEach(action -> {
                tInfos.add(newTripInfo(null, action));
            });

            tMap.put(tInfo.toString(), tInfos);
        }

        return tMap;

    }

    /**
     *
     * Cria uma novo objecto TripInfo
     *
     * @param list lista
     * @param trips viagens
     * @return devolver
     */
    private TripInfo newTripInfo(List<Trips> list, Trips trips) {

        String countStation = list != null ? String.valueOf(list.size()) : "";

        String timeTrip = list != null ? formatter.format(TripsPriceAndTime.getTripTime(list))
                : formatter.format(trips.getDuration());

        String priceTrip = list != null
                ? String.format("%.2f €",
                        TripsPriceAndTime.getTripPrice(list) + list.get(0).getDeparture().getStation().getPrice())
                : String.format("%.2f €", trips.getArrival().getStation().getPrice());

        String lineCount = list != null
                ? String.valueOf(list.stream().map(mapper -> mapper.getLine().getColor()).distinct().count())
                : trips.getLine().getColor();

        String stationDeparture = list != null ? list.stream().findFirst().get().getDeparture().getStation().getName()
                : trips.getDeparture().getStation().getName();

        String stationArrival = list != null ? list.get(list.size() - 1).getArrival().getStation().getName()
                : trips.getArrival().getStation().getName();

        return new TripInfo(countStation, lineCount, timeTrip, priceTrip, stationDeparture, stationArrival, list);

    }

    /**
     * Ação do rato fora do botão de Informação
     *
     * @param event evento
     */
    void onMouseOut(MouseEvent event) {
        btnConfirm.setStyle(
                "-fx-background-color: transparent;-fx-text-fill: black;-fx-border-width: 5;  -fx-border-radius: 80; -fx-background-radius: 80;-fx-border-color: #46a049;");
    }

    /**
     * Ação do rato no botão de Informação
     *
     * @param event evento
     */
    void onMouseOver(MouseEvent event) {
        btnClean.setStyle(
                "-fx-background-color: #46a049;; -fx-text-fill: white; -fx-border-radius: 80; -fx-border-width: 5; -fx-background-radius: 80;");
    }

    public <K extends Comparable, V> Map<K, V> sortByKeys(Map<K, V> map) {
        // create a list of map keys and sort it
        List<K> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);

        // create an empty insertion-ordered `LinkedHashMap`
        Map<K, V> linkedHashMap = new LinkedHashMap<>();

        // for every key in the sorted list, insert key-value
        // pair in `LinkedHashMap`
        for (K key : keys) {
            linkedHashMap.put(key, map.get(key));
        }

        return linkedHashMap;
    }
    
    /**
     * Calcula o preço final que o cliente terá que pegar pela sua viagem
     * Consoante o valor de desconto do Passe de Transporte associado à sua conta.
     * 
     * @return Preço final com desconto
     * @throws SubwayException 
     */
    public Double discountPass(Double totalPriceTrip) throws SubwayException {

        //Request a API para os tipos de Transport Pass
        TransportPassType response = this.apiConnJSON.getTransportPass();
        
        
        if (Session.getGuidPass()!=null) {
            //Request a API para o Transport Pass do Cliente
            ResponseTransportPass responseTransportPass = this.apiConnJSON.getTransportPassesbyID(Session.getGuidPass());
            String isActive = responseTransportPass.getTransportPasses().get(0).getValidation();
            
            if (isActive == "Valido" ) {
            
                //Guardar num array os valores do request da API do Tipos de Transport Pass
                ArrayList<PassType> values = response.getTransportPassTypes();
                
                for (int i = 0; i < values.size(); i++) {
                    if (values.get(i).getPassTypeId().equals(responseTransportPass.getTransportPasses().get(0).getPassTypeId())) {
                        discountPass = values.get(i).getDiscount();
                        priceTotalTrip = totalPriceTrip;
                        priceWithDiscount = (1 - discountPass) * priceTotalTrip;
                        return priceWithDiscount;
                    }
                }
            } else {
                priceWithDiscount = totalPriceTrip;
            }
        } else {
            
            priceWithDiscount = totalPriceTrip;
            
        }
        return priceWithDiscount;

    }
}

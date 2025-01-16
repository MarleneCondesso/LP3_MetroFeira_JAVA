package com.subway2feira.controllers;

import com.subway2feira.API.API;
import com.subway2feira.API.RequestApi;
import com.subway2feira.main.App;
import com.subway2feira.models.Ticket;
import com.subway2feira.models.Trips;
import com.subway2feira.services.TripService;
import com.subway2feira.utils.AlertBoxController;
import java.net.URL;

import java.io.IOException;
import java.util.ResourceBundle;

import com.subway2feira.utils.MoneyChange;
import com.subway2feira.utils.Session;
import com.subway2feira.utils.TripsPriceAndTime;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PaymentController implements Initializable {

    private double total = 0;

    @FXML
    private Spinner<Integer> money2;

    @FXML
    private Spinner<Integer> money1;

    @FXML
    private Spinner<Integer> money0_5;

    @FXML
    private Spinner<Integer> money0_2;

    @FXML
    private Spinner<Integer> money0_1;

    @FXML
    private Spinner<Integer> money0_05;

    @FXML
    private Spinner<Integer> money10;

    @FXML
    private Spinner<Integer> money5;

    @FXML
    private Button btnTicket;
    @FXML
    private Button btnPayment;
    @FXML
    private Label lblTotal;
    @FXML
    private Label lblFalta;
    @FXML
    private Button btnClean;
    @FXML
    private Label lb_troco;
    @FXML
    private AnchorPane panel_bills;
    @FXML
    private AnchorPane panel_coins;

    Ticket ticket;
    private Scene scene;
    private Stage stage;

    double totalToPay = 0;
    double totalPay = 0;

    PlanTripController planTripController;
    @FXML
    private Label lblTroco;
    private ViewTicketController viewTicketController;
    TripService tripserv = new TripService();
    List<Trips> trips;
    int tripid;
    RequestApi rapi;

    int departureID;
    int arrivalID;
    LocalDate date = LocalDate.now();
    Time duration;
    Double price;
    Double discontPass;
    /**
     * 
     * @param price          valor a pagar
     * @param planController Controller da view Agendar Viagem
     */
    public void setPaymentView(int departureID, int arrivalID, LocalDate date, Time duration, Double price,
            RequestApi trip, List<Trips> trips, PlanTripController planController) {

        this.planTripController = planController;
        this.departureID = departureID;
        this.arrivalID = arrivalID;
        this.date = date;
        this.duration = duration;
        this.trips = trips;
        
        
        this.price = price;
        this.totalToPay = price;
        
                
        this.rapi = trip;

        lblTotal.setText(String.format("%.2f", Math.abs(totalToPay)).concat(" €"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.money10.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
        this.money5.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
        this.money2.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
        this.money1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
        this.money0_5.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
        this.money0_2.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
        this.money0_1.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
        this.money0_05.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
        btnPayment.setDisable(true);
    }

    @FXML
    void selectMoney(MouseEvent event) {
        totalPay = money10.getValue() * 10 + money5.getValue() * 5 + money2.getValue() * 2 + money1.getValue() * 1
                + money0_5.getValue() * 0.5
                + money0_2.getValue() * 0.2 + money0_1.getValue() * 0.1 + money0_05.getValue() * 0.05;

        double diferenca = totalToPay - totalPay;

        if (diferenca < 0) {
            lblTroco.setText("Valor a receber:");
            lblFalta.setText(String.format("%.2f", Math.abs(diferenca)).concat(" €"));

        } else {
            lblFalta.setText(String.format("%.2f", Math.abs(diferenca)).concat(" €"));
        }

        if (totalPay >= totalToPay) {
            money10.setDisable(true);
            money5.setDisable(true);
            money2.setDisable(true);
            money1.setDisable(true);
            money0_5.setDisable(true);
            money0_2.setDisable(true);
            money0_1.setDisable(true);
            money0_05.setDisable(true);
            AlertBoxController alert = new AlertBoxController(Alert.AlertType.INFORMATION, "Atenção",
                    "Já ultrapassou o valor a pagar!");
            btnPayment.setDisable(false);
        } else {
            lblFalta.setText(String.format("%.2f", Math.abs(diferenca)).concat(" €"));
        }

        System.out.println(diferenca);
    }

    @FXML
    private void btnclean(ActionEvent event) {
        this.money10.getValueFactory().setValue(0);
        this.money5.getValueFactory().setValue(0);
        this.money2.getValueFactory().setValue(0);
        this.money1.getValueFactory().setValue(0);
        this.money0_5.getValueFactory().setValue(0);
        this.money0_2.getValueFactory().setValue(0);
        this.money0_1.getValueFactory().setValue(0);
        this.money0_05.getValueFactory().setValue(0);
        money10.setDisable(false);
        money5.setDisable(false);
        money2.setDisable(false);
        money1.setDisable(false);
        money0_5.setDisable(false);
        money0_2.setDisable(false);
        money0_1.setDisable(false);
        money0_05.setDisable(false);
        totalPay = 0;
        lblFalta.setText("");
        lblTroco.setText("Pagamento em Falta:");
        lb_troco.setText("");
        panel_bills.setDisable(false);
        panel_coins.setDisable(false);
        btnPayment.setDisable(true);

    }

    @FXML
    void payment(ActionEvent event) {
        API api = new API();
        String Qr = "";
        try {
            ticket = api.getTicket(rapi);
            Qr = ticket.getUserQR();
            System.out.println(ticket.getUserQR());
            addTrip(Qr.toString());
            btnTicket.setDisable(false);
            btnPayment.setDisable(true);

        } catch (Exception e) {
            new AlertBoxController(Alert.AlertType.ERROR, "Atenção", e.getMessage());
        }
        changeMoney();
    }

    private void changeMoney() {
        MoneyChange moneyChange = new MoneyChange();
        double diff = totalPay - totalToPay;
        double[][] _moneyChange = moneyChange.getMoneyChange(diff);

        StringBuilder st = new StringBuilder();
        for (double[] ds : _moneyChange) {
            if (ds[1] > 0)
                st.append(String.format("%.2f €: %d \n", ds[0], (int) ds[1]));
        }

        panel_bills.setDisable(true);
        panel_coins.setDisable(true);

        lb_troco.setText(st.toString());
    }

    @FXML
    void btnticket(ActionEvent event) throws IOException {
        showModalView("ViewTicket", ticket.getUserQR(), rapi);
    }

    /**
     * Método para abrir a view listar o atual XML
     *
     * @param fxml   fxml
     * @param titulo titulo
     * @param email  email
     * @throws IOException excepção
     */
    private void showModalView(String fxml, String qrCode, RequestApi rApi) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(App.viewsPath + fxml + ".fxml"));
        fxmlLoader.load();

        scene = new Scene(fxmlLoader.getRoot());
        stage = new Stage();
        viewTicketController = fxmlLoader.getController();
        viewTicketController.qrCode(qrCode, rApi);
        stage.setTitle("Lista do Atual XML - Subway2Feira");
        stage.getIcons().add(new Image("/com/subway2feira/img/subwayIcon.png"));
        stage.setScene(scene);
        stage.show();
    }

    void addTrip(String QrCode) {
        Boolean response = this.tripserv.addTripUser(Session.getId(), departureID, arrivalID,
                date, TripsPriceAndTime.getTripTime(this.trips), this.totalToPay, QrCode);

        if (response) {
            int id = this.tripserv.getLastTripUserID();

            for (Trips tp : this.trips) {
                this.tripserv.addPath(id, tp.getId());
            }
            AlertBoxController alertaConf = new AlertBoxController(Alert.AlertType.INFORMATION, "SUCESSO",
                    "Viagem agendada com SUCESSO!");
        } else {
            AlertBoxController alertError = new AlertBoxController(Alert.AlertType.ERROR, "ERRO",
                    "Base de dados. Por favor, Tente novamente.");
        }

    }
}

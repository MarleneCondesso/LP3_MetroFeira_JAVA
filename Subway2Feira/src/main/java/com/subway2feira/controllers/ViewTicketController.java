package com.subway2feira.controllers;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import com.subway2feira.API.API;
import com.subway2feira.API.RequestApi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class ViewTicketController {


    @FXML
    private Label lb_date;

    @FXML
    private Label lb_price;

    @FXML
    private Label lb_trip;

    @FXML
    private HBox principalPane;

    @FXML
    private ImageView qrcode;

    @FXML
    private Label title;

    public void qrCode(String code,  RequestApi rapi){
        String data = code;
        String base64Image = data.split(",")[1];
       // base64Image = base64Image.substring(0, base64Image.length() - 1);
        byte[] decodeImg = Base64.getDecoder().decode(base64Image);
        qrcode.setImage(new Image(new ByteArrayInputStream(decodeImg)));



        lb_trip.setText(String.format("%s > %s", rapi.getBegin(), rapi.getEnd()));
        lb_price.setText(String.format("%s €", rapi.getPrice()));
        lb_date.setText(String.format("%s min.", rapi.getDuration()));
    }
}
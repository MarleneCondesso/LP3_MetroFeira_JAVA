package com.subway2feira.API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.stream.Collectors;

import com.subway2feira.API.Exeception.SubwayException;
import com.subway2feira.API.models.ClassConverter;

class Connect {
    // endpoint
    protected final String endpoint = "https://services.inapa.com";

    // api
    protected final String trip = "/Subway2Feira/api/trip";
    protected final String map = "/Subway2Feira/api/map";
    protected final String transportPassType = "/Subway2Feira/api/TransportPassType";
    protected final String createPass ="/Subway2Feira/api/TransportPass/";
    
    // auth
    protected String user = "G2";
    protected String pass = "SJ$pEgYO(Y";

    // Content-Type JSON || XML
    protected ContentType contentType = ContentType.JSON;

    public Connect() {

    }

    public Connect(ContentType contentType) {
        this.contentType = contentType;
    }

    public Connect(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }

    public Connect(String user, String pass, ContentType contentType) {
        this.user = user;
        this.pass = pass;
        this.contentType = contentType;
    }

    /**
     *
     * Retorna conexão API método GET Map
     * 
     * @param urlGet url API
     * @return Retorna HttpURLConnection
     * @throws IOException
     * @throws SubwayException
     */
    protected HttpURLConnection gConnection(String urlGet) throws SubwayException {

        try {
            URL url = new URL(this.endpoint + urlGet);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("content-Type", contentType.getType());
            httpURLConnection.setRequestProperty("Authorization", "Basic " + this.convertBase64());
           
            return httpURLConnection;
        } catch (Exception e) {
            throw new SubwayException("Error conexão base dados: " + e.getMessage());
        }

    }
        protected HttpURLConnection dConnection(String urlGet) throws SubwayException {

        try {
            URL url = new URL(this.endpoint + urlGet);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("DELETE");
            httpURLConnection.setRequestProperty("content-Type", contentType.getType());
            httpURLConnection.setRequestProperty("Authorization", "Basic " + this.convertBase64());
            return httpURLConnection;
        } catch (Exception e) {
            throw new SubwayException("Error conexão base dados: " + e.getMessage());
        }

    }

    /**
     * 
     * Retorna conexão API método POST Trip
     * 
     * @param request requesitos POST
     * @param urlPost url API
     * @return Retorna HttpURLConnection
     * @throws SubwayException
     * @throws IOException
     */
    protected <T extends ClassConverter> HttpURLConnection pConnection(T request, String urlPost) throws SubwayException {

        try {
            URL url = new URL(this.endpoint + urlPost);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Accept", contentType.getType());
            httpURLConnection.setRequestProperty("Content-Type", contentType.getType());
            httpURLConnection.setRequestProperty("Authorization", "Basic " + this.convertBase64());

            httpURLConnection.setDoOutput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            byte[] input = contentType == ContentType.JSON ? request.toJsonString().getBytes("utf-8")
                    : request.toXmlString().getBytes("utf-8");

            os.write(input, 0, input.length);
            return httpURLConnection;
        } catch (Exception e) {
            throw new SubwayException("Error conexão base dados: " + e.getMessage());
        }

    }
        protected <T extends ClassConverter> HttpURLConnection putConnection(T request, String urlPost) throws SubwayException {

        try {
            URL url = new URL(this.endpoint + urlPost);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("PUT");
            httpURLConnection.setRequestProperty("Accept", contentType.getType());
            httpURLConnection.setRequestProperty("Content-Type", contentType.getType());
            httpURLConnection.setRequestProperty("Authorization", "Basic " + this.convertBase64());

            httpURLConnection.setDoOutput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            byte[] input = contentType == ContentType.JSON ? request.toJsonString().getBytes("utf-8")
                    : request.toXmlString().getBytes("utf-8");

            os.write(input, 0, input.length);
            return httpURLConnection;
        } catch (Exception e) {
            throw new SubwayException("Error conexão base dados: " + e.getMessage());
        }

    }
  
    /**
     * 
     * Caso o Status seja igual 200 retorna string JSON/XML
     * Caso o status seja diferente retorna erro
     * 
     * @param httpURLConnection
     * @return Erro || JSON/XML
     * @throws SubwayException
     * @throws IOException
     */
    protected String errorReturn(HttpURLConnection httpURLConnection) throws SubwayException {
        
        try {
                switch (httpURLConnection.getResponseCode()) {
                case 400:
                    throw new SubwayException("400 - Pedido invalido");
                case 500:
                    throw new SubwayException("500 - Erro no pedido");
                case 406:
                    throw new SubwayException("406 - Parâmetros inválidos");
                case 405:
                    throw new SubwayException("405 - O pedido não é um método GET");
                case 200:
                case 201:
                case 202:
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8")); 
                    return br.lines().collect(Collectors.joining());

                default:
                    throw new SubwayException("Ocorreu um erro no pedido");
            }
        } catch (Exception e) {
            throw new SubwayException(e.getMessage());
        }

    }

    /**
     * 
     * String > Base64
     * 
     * @return String convertida em Base64
     */
    protected String convertBase64() {
        return Base64.getEncoder().encodeToString(String.format("%s:%s", this.user, this.pass).getBytes());
    }

    public String getEndpoint() {
        return this.endpoint;
    }

    public String getTripPoint() {
        return this.trip;
    }

    public String getMapPoint() {
        return this.map;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return this.pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public ContentType getContentType() {
        return this.contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

}

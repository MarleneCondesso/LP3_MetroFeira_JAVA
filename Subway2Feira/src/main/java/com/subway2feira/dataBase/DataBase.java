/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.subway2feira.dataBase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marlene
 */
public class DataBase {

    private String driver;
    private String connectionString;
    private String url;
    private String server;
    private int port;
    private String databaseName;
    private String username;
    private String password;

    private static DataBase singleton;

    private static Connection connection;

    public static DataBase getInstance() {
        if (singleton == null) {
            singleton = fromProperties();
        }

        return singleton;
    }

    private static DataBase fromProperties() {

        return new DataBase("sqlserver", "CTESPBD.DEI.ISEP.IPP.PT", 1433, "LP3_G2_2122", "LP3_G2_2122",
                "DeficienteseMongos2022");

    }

    private DataBase(String driver, String server, int port, String database, String username, String password) {
        setDriver(driver);
        setServer(server);
        setPort(port);
        setDatabaseName(database);
        setUsername(username);
        setPassword(password);
        setUrl();
    }

    private DataBase() {
    }

    public Connection connect() {
        try {

            connection = DriverManager.getConnection(getUrl(), username, password);

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return connection;
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public String getConnectionString() {
        return connectionString;
    }

    public String getUrl() {
        return url;
    }

    public String getDriver() {
        return driver;
    }

    public String getServer() {
        return server;
    }

    public int getPort() {
        return port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    private String getUsername() {
        return username;
    }

    private String getPassword() {
        return password;
    }

    private void setDriver(String driver) {
        this.driver = driver;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    private void setServer(String server) {
        this.server = server;
    }

    private void setPort(int port) {
        this.port = port;
    }

    private void setUrl() {
        this.url = "jdbc:" + getDriver() + "://" + getServer() + ':' + getPort() + ';' + "databaseName="
                + getDatabaseName();
    }

    private void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

}

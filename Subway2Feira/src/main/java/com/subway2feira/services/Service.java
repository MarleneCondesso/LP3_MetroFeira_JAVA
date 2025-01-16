/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.subway2feira.services;

import com.subway2feira.dataBase.DataBase;
import com.subway2feira.utils.AlertBoxController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.control.Alert;

/**
 *
 * @author Marlene
 */
public abstract class Service {

    private static DataBase database;

    protected Service() {

        database = DataBase.getInstance();
    }

    /**
     * Atribui os parametros ao statement. Esta funcao, para cada objeto na 
     * lista de
     * "params", indentifica o tipo de dado e associa ao statement conformemente.
     * 
     * @param statement statement.
     * @param params    Lista de valores no formato original e espectavel para
     *                  correlacao com o statement.
     * @return PreparedStatement com os valores dos parametros atribuidos.
     * @throws Exception
     * @throws SQLException
     */
    private PreparedStatement setParams(PreparedStatement statement, ArrayList<Object> params)
            throws Exception, SQLException {
        ResultSet rs = null;

        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
        }

        return statement;
    }

    /**
     * Adquire os dados dos registos obtidos de uma query/statement 
     * e mapeia para um
     * ArrayList.
     * 
     * @param rs rs
     * @param returnValues Coletanea de key-value pair <Key,Value> 
     * que identifica o campo e tipo de dados a retornar respetivamente.
     * @return ArrayList com coletanea de key-value pair contendo
     * o nome dos campo e o seu valor, respetivamente.
     * @throws Exception excepção
     * @throws SQLException excepção SQL
     */
    private ArrayList<HashMap<String, Object>> getResultValues(ResultSet rs, ArrayList<String> returnValues)
            throws Exception, SQLException {

        ArrayList<HashMap<String, Object>> records = new ArrayList<>();
        HashMap<String, Object> row;

        if (rs != null) {
            while (rs.next()) {

                row = new HashMap<>();

                for (String column : returnValues) {
                    row.put(column, (Object) rs.getObject(column));
                }

                records.add(row);

            }
        }

        return records;
    }

    /**
     * Executa uma query/statement na base de dados e devolve os valores numa
     * coletanea.
     * 
     * @param query    Query a ser exectuada com ou sem o caracter de parametro
     *                     (?).
     * @param params       Lista de objectos para correlacao com a query.
     * @param returnValues Coletanea de key-valueque 
     * identifica o campo e tipo de dados a retornar respetivamente.
     * @return ArrayList com coletanea de key-value pair contendo 
     * o nome dos campo e o seu valor, respetivamente.
     */
    protected ArrayList<HashMap<String, Object>> get(String query, ArrayList<Object> params,
            ArrayList<String> returnValues) {
        Connection connection = database.connect();

        ArrayList<HashMap<String, Object>> records = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = connection.prepareStatement(query);
            statement = setParams(statement, params);

            rs = statement.executeQuery();
            records = getResultValues(rs, returnValues);

            connection.close();

        } catch (SQLException e) {
            AlertBoxController alert = new AlertBoxController(Alert.AlertType.ERROR, "Service Error", e.getMessage());
            records = null;
        } catch (Exception e) {
            AlertBoxController alert = new AlertBoxController(Alert.AlertType.ERROR, "Service Erro", e.getMessage());
        } finally {
            connection = null;
        }

        return records;
    }

    /**
     * Executa uma Query DML Insert com os valores passados por parametro.
     * 
     * @param query  Query a ser exectuada com ou sem o caracter de parametro(?).
     * @param params Lista de objectos para correlacao com a query.
     * @return TRUE se executado com sucesso. Falso se ocorreu um erro.
     */
    protected boolean insert(String query, ArrayList<Object> params) {
        return executeUpdateAs("INSERT INTO ", query, params);
    }

    /**
     * Executa uma Query DML UPDATE com os valores passados por parametro.
     * 
     * @param query  Query a ser exectuada com ou sem o caracter de parametro(?).
     * @param params Lista de objectos para correlacao com a query.
     * @return TRUE se executado com sucesso. Falso se ocorreu um erro.
     */
    protected boolean update(String query, ArrayList<Object> params) {
        return executeUpdateAs("UPDATE ", query, params);
    }

    /**
     * Executa uma Query DML DELETE com os valores passados por parametro.
     * 
     * @param query  Query a ser exectuada com ou sem o caracter de parametro(?).
     * @param params Lista de objectos para correlacao com a query.
     * @return TRUE se executado com sucesso. Falso se ocorreu um erro.
     */
    protected boolean delete(String query, ArrayList<Object> params) {
        return executeUpdateAs("DELETE FROM ", query, params);
    }

    /**
     * Executa uma Query DML EXEC procedure com os valores passados por parametro.
     * 
     * @param query  Query a ser exectuada com ou sem o caracter de parametro(?).
     * @param params Lista de objectos para correlacao com a query.
     * @return TRUE se executado com sucesso. Falso se ocorreu um erro.
     */
    protected boolean procedure(String query, ArrayList<Object> params) {
        return executeUpdateAs("EXEC ", query, params);
    }

    /**
     * Executa uma query DML na base de dados com os valores passados por parametro
     * 
     * @param statement Valor de declaracao de query {INSERT INTO | UPDATE | DELETE
     *                  FROM }
     * @param query    Query a ser exectuada com ou sem o caracter de parametro
     *                  (?).
     * @param params   Lista de objectos para correlacao com a query.
     * @return TRUE se executado com sucesso. Falso se ocorreu um erro.
     */
    private boolean executeUpdateAs(String statement, String query, ArrayList<Object> params) {
        query = statement + query.trim();
        return executeUpdate(query, params);
    }

    /**
     * Executa uma query DML na base de dados com os valores passados por parametro
     * 
     * @param query Query a ser exectuada com ou sem o caracter de parametro (?).
     * @param params Lista de objectos para correlacao com a query.
     * @return TRUE se executado com sucesso. Falso se ocorreu um erro.
     */
    private boolean executeUpdate(String query, ArrayList<Object> params) {

        Connection connection = database.connect();

        boolean wasSuccessful;

        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(query);
            statement = setParams(statement, params);

            int g = statement.executeUpdate();

            connection.close();

            wasSuccessful = true;
        } catch (Exception e) {
            AlertBoxController alert = new AlertBoxController(Alert.AlertType.ERROR, "Service Error", e.getMessage());
            wasSuccessful = false;
        } finally {
            connection = null;
        }

        return wasSuccessful;
    }
}

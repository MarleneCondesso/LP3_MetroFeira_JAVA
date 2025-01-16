/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.subway2feira.services;

import com.subway2feira.models.User;
import java.sql.Date;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Marlene
 */
public class UserService extends Service {

    /**
     * Método para converter os objetos passados como paramêtros para o Modelo
     * Utilizador
     *
     * @param name nome
     * @param nif nif
     * @param email email
     * @param password pass
     * @param type tipo
     * @param salt salt
     * @return Utilizador com os dados
     */
    private User hashMapToUser(Object id, Object name, Object nif, Object email, Object password, Object userType,
            Object state,
            Object salt , Object dataNascimento, Object guidPass) {
       
        Date birthDate;
        birthDate = (Date) dataNascimento;
        
        User u = new User();
        u.setId((Integer) id);
        u.setName((String) name);
        u.setNif((String) nif);
        u.setEmail((String) email);
        u.setPassword((String) password);
        u.setType((String) userType);
        u.setState((String) state);
        u.setSalt((String) salt);
        u.setDateBirth((LocalDate) birthDate.toLocalDate());
        u.setGuidPass((String) guidPass);

        return u;
    }
        /**
     * Método para converter os objetos passados como paramêtros para o Modelo
     * Utilizador
     *
     * @param name nome
     * @param nif nif
     * @param email email
     * @param password pass
     * @param type tipo
     * @param salt salt
     * @return Utilizador com os dados
     */
    private User hashMapToClient(Object id, Object name, Object nif, Object email, Object password, Object userType,
            Object state,
            Object salt , Object dataNascimento) {

        Date birthDate;
        birthDate = (Date) dataNascimento;
        User u = new User();
        u.setId((Integer) id);
        u.setName((String) name);
        u.setNif((String) nif);
        u.setEmail((String) email);
        u.setPassword((String) password);
        u.setType((String) userType);
        u.setState((String) state);
        u.setSalt((String) salt);
        u.setDateBirth((LocalDate) birthDate.toLocalDate());

        return u;
    }

    /**
     * Método para retornar uma lista de todos os Utilizadores Ativos
     *
     * @return Lista de Utilizadores
     */
    public ArrayList<User> getAllClient() {

        ArrayList<User> users = new ArrayList<>();

        ArrayList<String> values = new ArrayList();

        values.add("id");
        values.add("name");
        values.add("nif");
        values.add("email");
        values.add("password");
        values.add("userType");
        values.add("state");
        values.add("salt");
        values.add("dataNascimento");
        values.add("GUID");

        ArrayList<HashMap<String, Object>> data = super.get("SELECT * FROM [User] WHERE state = 1 AND userType = 'Cliente'", null, values);

        for (HashMap<String, Object> user : data) {

            // Associa as chaves da Base de Dados ao valor Objeto
            users.add(hashMapToUser(user.get("id"), user.get("name"), user.get("nif"), user.get("email"),
                    user.get("password"),
                    user.get("userType"), user.get("state"), user.get("salt"), user.get("dataNascimento"), user.get("GUID")));
        }
        return users;
    }

    /**
     * Método para retornar um Utilizador como parâmetros e-mail e password.
     *
     * @param email email
     * @param password pass
     * @return Utilizador
     */
    public User getUserByEmailAndPassword(String email, String password) {

        User user = null;

        String query = "SELECT * FROM [User] WHERE email = ? AND password = ?";

        ArrayList<Object> params = new ArrayList();

        params.add(email);
        params.add(password);

        ArrayList<String> values = new ArrayList();

        values.add("id");
        values.add("name");
        values.add("nif");
        values.add("email");
        values.add("password");
        values.add("userType");
        values.add("state");
        values.add("salt");
        values.add("dataNascimento");
        values.add("GUID");

        ArrayList<HashMap<String, Object>> data = super.get(query, params, values);

        if (data.size() == 1) {

            HashMap<String, Object> value = data.get(0);

            user = hashMapToUser(value.get("id"), value.get("name"), value.get("nif"), value.get("email"),
                    value.get("password"),
                    value.get("userType"), value.get("state"), value.get("salt"), value.get("dataNascimento"), value.get("GUID"));

        }

        return user;
    }

    /**
     * Método para retornar o Utilizador passando como paramêntro o e-mail.
     *
     * @param email email
     * @return o utilizador.
     */
    public User getUserByEmail(String email) {

        User user = null;

        String query = "SELECT * FROM [User] WHERE email =?";

        ArrayList<Object> params = new ArrayList();

        params.add(email);

        ArrayList<String> values = new ArrayList();

        values.add("id");
        values.add("name");
        values.add("nif");
        values.add("email");
        values.add("password");
        values.add("userType");
        values.add("state");
        values.add("salt");
        values.add("dataNascimento");
        values.add("GUID");

        ArrayList<HashMap<String, Object>> data = super.get(query, params, values);

        if (data.size() == 1) {

            HashMap<String, Object> value = data.get(0);

            user = hashMapToUser(value.get("id"), value.get("name"), value.get("nif"), value.get("email"),
                    value.get("password"),
                    value.get("userType"), value.get("state"), value.get("salt"), value.get("dataNascimento"), value.get("GUID"));

        }
        return user;
    }

    /**
     * Método para criar registo do Utilizador na Base de Dados
     *
     * @param name nome
     * @param nif nif
     * @param email email
     * @param password pass
     * @param type tipo
     * @param state estado
     * @param salt salt
     * @return se o registo foi concluído com sucesso ou sem sucesso.
     */
    public boolean create(String name, String nif, String email, String password, String type, String state, String salt, LocalDate dateBirth) {
        String query = "[User] (name, nif, email, password, userType, state, salt, dataNascimento) VALUES (?,?,?,?,?,?,?,?)";
        
        state = "1";
        ArrayList<Object> params = new ArrayList();
        params.add(name);
        params.add(nif);
        params.add(email);
        params.add(password);
        params.add(type);
        params.add(state);
        params.add(salt);
        params.add(dateBirth);

        return super.insert(query, params);

    }

    /**
     * Método para atualizar a password do Utilizador com os paramêtros e-mail e
     * password.
     *
     * @param email email
     * @param password pass
     * @return se a alteração da palavra-passe foi concluída com sucesso ou sem
     *         sucesso.
     */
    public boolean updatePasswordByEmail(String email, String password) {

        String query = "[User] SET password=? WHERE email=?";

        ArrayList<Object> params = new ArrayList();

        params.add(email);
        params.add(password);

        return super.update(query, params);
    }
    /**
     * Método para atualizar o GUID do Utilizador com os paramêtros e-mail.
     *
     * @param email email
     * @param password pass
     * @return se a alteração da palavra-passe foi concluída com sucesso ou sem
     *         sucesso.
     */
    public boolean updateGuidByEmail(String email, String guid) {

        String query = "[User] SET GUID=? WHERE email=?";

        ArrayList<Object> params = new ArrayList();

        params.add(guid);
        params.add(email);
       

        return super.update(query, params);
    }

    /**
     * Método para desativar o Utilizador na Base de Dados com o paramêtro
     * e-mail.
     *
     * @param email email
     * @return se o estado da conta foi alterado com sucesso ou sem sucesso.
     */
    public boolean disableByEmail(String state, String email) {

        String query = "[User] SET state=? WHERE email=?";

        ArrayList<Object> params = new ArrayList();

        params.add(state);
        params.add(email);

        return super.update(query, params);
    }

}

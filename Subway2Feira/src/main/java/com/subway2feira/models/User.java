package com.subway2feira.models;

import java.time.LocalDate;


/**
 *
 * @author Marlene
 */
public class User {

    private Integer id;
    private String name;
    private String email;
    private LocalDate dateBirth;
    private String nif;
    private String password;
    private String type;
    private String state;
    private String salt;
    private String guidPass;

    public User() {
    }

    public User(Integer id, String name, String nif, String email, LocalDate dateBirth,String password, String type, String state,
            String salt, String guidPass) {
        this.id = id;
        this.name = name;
        this.nif = nif;
        this.email = email;
        this.dateBirth = dateBirth;
        this.password = password;
        this.type = type;
        this.state = state;
        this.salt = salt;
        this.guidPass = guidPass;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(LocalDate dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
    public String getGuidPass() {
        return guidPass;
    }

    public void setGuidPass(String guidPass) {
        this.guidPass = guidPass;
    }

}

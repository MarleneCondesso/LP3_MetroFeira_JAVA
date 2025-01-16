/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.subway2feira.utils;

import java.sql.Date;


/**
 *
 * @author 35191
 */
public class Session {

    private  static String type;
    
    private static String email;

    private static Integer id;
    
    private static Date date;

    private static String guidPass;

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        Session.email = email;
    }

    public static Integer getId() {
        return id;
    }

    public static void setId(Integer id) {
        Session.id = id;
    }

    public static String getType() {
        return type;
    }

    public static void setType(String type) {
        Session.type = type;
    }
    public static String getGuidPass() {
        return guidPass;
    }

    public static void setGuidPass(String guidPass) {
        Session.guidPass = guidPass;
    }

    /**
     * @return the date
     */
    public static Date getDate() {
        return date;
    }

    /**
     * @param aDate the date to set
     */
    public static void setDate(Date aDate) {
        date = aDate;
    }
}

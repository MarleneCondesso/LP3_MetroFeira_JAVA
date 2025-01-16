/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.subway2feira.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author lucpe
 */
public class CheckCredentials {
  
    public static boolean CheckEmail(String email){
        String emailRegex = "^[a-zA-Z0-9_!#$%&’*+=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern emailPat = Pattern.compile(emailRegex);
        Matcher matcher = emailPat.matcher(email);
        if (matcher.matches()) 
            return true;
        return false;
    }
    
    public static boolean CheckNif(String nif){
        String nifRegex ="[0-9]{9}";
        Pattern nifPat = Pattern.compile(nifRegex);
        Matcher matcher = nifPat.matcher(nif);
        if (matcher.matches()) 
            return true;
        return false;
    }
}

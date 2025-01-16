/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.subway2feira.API.models;

import com.google.gson.Gson;

/**
 *
 * @author lucpe
 */
public abstract class ClassConverter {
   public abstract String toJsonString();

    public abstract String toXmlString();
}

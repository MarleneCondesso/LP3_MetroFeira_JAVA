/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.subway2feira.utils;

import java.util.ArrayList;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Before;

public class PathTest {

  @Before
  public void setup() {

  }

  @Test
  public void test() throws Exception {

    Path caminho = new Path(new ArrayList());

    Throwable exception = assertThrows(Exception.class,
        () -> {
          caminho.route("Porto", "Aveiro");
        });

    assertEquals("Caminho inválido!! Por favor verifique", exception.getMessage());

    exception = assertThrows(Exception.class,
        () -> {
          caminho.route("Porto", "Porto");
        });

    assertEquals("Local de Destino e local de partida são iguais!!!", exception.getMessage());
  }
}

package com.subway2feira.utils;

import org.junit.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Before;

public class XmlFileTest {

    @Before
    public void setup() {

    }

    @Test
    public void test() {

        String pathFile = "src/main/resources/com/subway2feira/xml/Subway2Feira.xml";

        XmlFile xmlFile = new XmlFile(pathFile,true);

        assertTrue(xmlFile.fileExist());
    }
}

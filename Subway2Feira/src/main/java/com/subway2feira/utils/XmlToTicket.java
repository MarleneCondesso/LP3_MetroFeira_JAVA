package com.subway2feira.utils;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.subway2feira.API.Exeception.SubwayException;
import com.subway2feira.models.Ticket;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XmlToTicket {
    private String source;

    public XmlToTicket(String source) {
        this.source = source;
    }

    private Document stringToDocument() throws Exception {

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        return documentBuilder.parse(new InputSource(new StringReader(this.source)));

    }

    public Ticket get() throws SubwayException{

        try {

            NodeList nodeList = this.stringToDocument().getElementsByTagName("Result");

            Element element = ((Element) nodeList.item(0));
    
            String status = element.getElementsByTagName("Status").item(0).getTextContent();
            String userQR = element.getElementsByTagName("UserQR").item(0).getTextContent();
    
            return new Ticket(status, userQR);
            
        } catch (Exception e) {
            throw new SubwayException("Erro converter resposta do servidor em objecto Ticket");
        }
    }

}

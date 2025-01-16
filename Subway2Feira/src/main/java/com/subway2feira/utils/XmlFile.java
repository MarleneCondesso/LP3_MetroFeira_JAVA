package com.subway2feira.utils;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.function.Function;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.subway2feira.models.Line;
import com.subway2feira.models.Station;
import com.subway2feira.models.StationLine;
import com.subway2feira.models.Trips;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlFile {
    private String source;
    private boolean isfile;
    DateFormat formatter = new SimpleDateFormat("mm:ss");

    private ArrayList<Line> lines;
    private ArrayList<Station> stations;

    public XmlFile(String source, boolean isfile) {
        this.source = source;
        this.isfile = isfile;
    }

    public boolean fileExist() {

        if (new File(this.source).exists())
            return true;

        return false;
    }

    /**
     * 
     * Converte ficheiro xml na classe Line
     * 
     * @return Devolve um array das linhas do metro
     */
    public ArrayList<Line> getAllLine() throws Exception {
        return get(this.fileToDocument(), "Line", getLines);
    }

    /**
     * 
     * Converte ficheiro xml na classe Stations
     * 
     * @return Devolve array de todas estações e linhas que representa
     */
    public ArrayList<Station> getAllStations() throws Exception {
        return get(this.fileToDocument(), "Station", getStationLines);
    }

    /**
     * 
     * Converte ficheiro xml na classe trips ligaçoes da estações
     * 
     * @return Devolve array das estações com suas ligações
     */
    public ArrayList<Trips> getAllTrips() throws Exception {
        this.lines = this.getAllLine();
        this.stations = this.getAllStations();
        ArrayList<Trips> tp = get(this.fileToDocument(), "Trip", getTrip);
        tp.addAll(getInvertStation(tp));
        return tp;
    }

    /**
     * 
     * Reverte as estações de entrada o que de departure passa arrival
     * 
     * @param station estação
     * @return O inverso das estações
     */
    private ArrayList<Trips> getInvertStation(ArrayList<Trips> station) {

        ArrayList<Trips> tp = new ArrayList<>();

        station.forEach(action -> tp.add(makeTrips(action)));

        return tp;

    }

    /**
     * 
     * Estação inversa
     * 
     * @param Trips viagens
     * @return devolve
     */
    private Trips makeTrips(Trips Trips) {
        return new Trips(Trips.getDeparture(), Trips.getArrival(), Trips.getLine(), Trips.getDuration());
    }

    /**
     * 
     * Retorna o valor de dentro do documento
     * 
     * @return um novo documento
     * @throws ParserConfigurationException excepção
     * @throws SAXException excepção
     * @throws IOException excepção
     * @throws IllegalArgumentException excepção
     */
   private Document fileToDocument() throws Exception {

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        if (!this.isfile)
            return documentBuilder.parse(new InputSource(new StringReader(this.source)));

        File xmlFile = new File(this.source);

        return documentBuilder.parse(xmlFile);

    }

    /**
     * 
     * Função generica recebe o nome do node para pesquisar e retorna a lista
     * pretendida
     * 
     * @param <T> t
     * @param doc documento
     * @param nodeName nome
     * @param inc inc
     * @return objecto T para ser convertido na classe desejada
     */
    private <T> ArrayList<T> get(Document doc, String nodeName, Function<Element, T> fun) {

        ArrayList<T> arrayType = new ArrayList<>();

        NodeList nodeList = doc.getElementsByTagName(nodeName);

        for (int i = 0; i < nodeList.getLength(); i++) {

            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                T value = fun.apply((Element) node);

                if (value != null)
                    arrayType.add(value);
            }

        }
        return arrayType;

    }

    /**
     * 
     * Recebe uma Objecto Element e retorna as ligações das estações
     * 
     * @param lElement elemento
     * @return ligações das estações
     */
    private Function<Element, Trips> getTrip = (lElement) -> {  try{
      return getTrip2(lElement);
    }catch (Exception ex) {
      ex.printStackTrace();
    }
    return new Trips();
    };
            
    private Trips getTrip2( Element lElement ) throws ParseException{
  return new Trips(
            new StationLine(getstation(lElement.getElementsByTagName("Arrival").item(0).getTextContent())),
            new StationLine(getstation(lElement.getElementsByTagName("Departure").item(0).getTextContent())),        
            this.lines.stream().filter(
                    action -> action.getLetter().equals(lElement.getElementsByTagName("Line").item(0).getTextContent()))
                    .findFirst().get(),
        new java.sql.Time(formatter.parse(lElement.getElementsByTagName("Duration").item(0).getTextContent()).getTime()));
}
    private Station getstation(String name){
        return this.stations.stream().filter(
            action -> action.getName().equals(name))
            .findFirst().get();
    }


    /**
     * 
     * Converte o node num Array das linhas metro
     * 
     * @param lElement elemento
     * @return Array das linhas
     */
    private ArrayList<String> getStationLinesElement(Element lElement) {

        ArrayList<String> lines = new ArrayList<>();

        for (int i = 0; i < lElement.getElementsByTagName("Line").getLength(); i++)
            lines.add(lElement.getElementsByTagName("Line").item(i).getTextContent());

        return lines;
    }

    /**
     * 
     * Converte o node num objecto de estações
     * 
     * @param lElement elemento
     * @return as ligações das estações
     */
  private Function<Element, Station> getStationLines = lElement ->
            new Station(lElement.getElementsByTagName("Name").item(0).getTextContent() , 
                    Double.parseDouble(lElement.getElementsByTagName("Price").item(0).getTextContent()));
    /**
     * 
     * Converte o node num objecto de linhas
     * 
     * @param lElement elemento
     * @return linhas metro
     */
    private Function<Element, Line> getLines = lElement -> !lElement.getAttribute("Key").isEmpty()
            ? new Line(lElement.getAttribute("Key"), lElement.getElementsByTagName("Name").item(0).getTextContent())
            : null;

}

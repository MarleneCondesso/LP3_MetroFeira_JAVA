package com.subway2feira.utils;

import java.util.ArrayList;
import java.util.List;

import com.subway2feira.models.Trips;
import static java.util.stream.Collectors.toList;

public class Path {

    private List<Trips> trips;
    private List<List<Trips>> finalRoutes;

    public Path(List<Trips> trips) {
        this.trips = trips;
        this.finalRoutes = new ArrayList<>();
    }

    /**
     * 
     * Lista de todos os caminho que posso usar para ir do ponto departure ate
     * arrival
     * 
     * @param departure partida
     * @param arrival chegada
     * @return devolve todos os caminhos
     */
    public List<List<Trips>> route(String departure, String arrival) throws Exception {
      if (departure.equals(arrival)){
        throw new Exception("Local de Destino e local de partida são iguais!!!");
      }

        List<Trips> nextP = getNextPosition(departure);

        for (Trips Trips : nextP)
            makeRoutes(Trips, new ArrayList<>(), arrival);
            if (finalRoutes.size()== 0){
        throw new Exception("Caminho inválido!! Por favor verifique");
      }
        return finalRoutes;
    }

    /**
     * 
     * Calcula todas as rotas possiveis desde minha partida ate chegada
     * 
     * @param Trips viagens
     * @param myPtah percurso
     * @param arrival chegada
     */
    private void makeRoutes(Trips Trips, List<Trips> myPtah, String arrival) {

        ifArrival(Trips, myPtah, arrival);

        List<Trips> newTrips = newPath(Trips, myPtah);

        List<Trips> possTrips = getNextPosition(Trips.getArrival().getStation().getName());

        for (Trips myPossibility : possTrips)
            if (!newTrips.contains(myPossibility) && ifIPass(newTrips, myPossibility))
                makeRoutes(myPossibility, newTrips, arrival);

    }

    /**
     * 
     * Verifica de cheguei ao meu destino, caso Sim adiciona a rota a lista final
     * 
     * @param Trips viagens
     * @param myPtah caminho
     * @param arrival chegada
     */
    private void ifArrival(Trips Trips, List<Trips> myPtah, String arrival) {

        if (Trips.getArrival().getStation().getName().equals(arrival)) {
            myPtah.add(Trips);
            this.finalRoutes.add(myPtah);

        }
    }

    /**
     * 
     * Verifica se ja passei na estação
     * 
     * @param newTrips novas viagens
     * @param Trips2 viagens
     * @return devolve se já passamos numa estação ou não.
     */
    private Boolean ifIPass(List<Trips> newTrips, Trips Trips2) {
        return newTrips.stream().filter(action -> action.getArrival().getStation().getName()
                .equals(Trips2.getArrival().getStation().getName())
                || action.getDeparture().getStation().getName().equals(Trips2.getDeparture().getStation().getName()))
                .collect(toList()).size() == 0 ? true : false;
    }

    /**
     * 
     * Adicionar novo caminho a lista com as minhas passagens
     * 
     * @param Trips viagens
     * @param myPtah percurso
     * @return devolve
     */
    private List<Trips> newPath(Trips Trips, List<Trips> myPtah) {
        List<Trips> newTrips = new ArrayList<>();
        newTrips.addAll(myPtah);
        newTrips.add(Trips);
        return newTrips;
    }

    /**
     * 
     * Quais as proximas estações
     * 
     * @param arrival chegada
     * @return As estações que se pode dirigir
     */
    private List<Trips> getNextPosition(String arrival) {
        return this.trips.stream().filter(find -> find.getDeparture().getStation().getName().equals(arrival)).collect(toList());
    }

    /**
     * 
     * Reverte as estações de entrada o que de departure passa arrival
     * 
     * @param station estação
     * @return O inverso das estações
     */
    private List<Trips> getInvertStation(List<Trips> station) {

        List<Trips> tp = new ArrayList<>();

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
        return new Trips(Trips.getDeparture(), Trips.getArrival(), Trips.getLine(),Trips.getDuration());
    }

    public List<Trips> getTrips() {
        return this.trips;
    }

    /**
     * 
     * Atualiza a lista de estação
     * 
     * @param Trips viagens
     */
    public void setTrips(List<Trips> Trips) {
        this.trips = Trips;
    }
}

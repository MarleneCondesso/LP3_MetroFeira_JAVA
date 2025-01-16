package com.subway2feira.services;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import com.subway2feira.models.Line;
import com.subway2feira.models.Station;
import com.subway2feira.models.StationLine;
import com.subway2feira.models.Trips;
import com.subway2feira.utils.XmlFile;

import org.junit.Before;

public class AdminServiceTest {

    AdminService adminService = new AdminService();
    XmlFile xmlFile;

    @Before
    public void setup() {

    }

    @Test
    public void testAddOrUpdateTrip() {

        for (Trips trips : testAddtrips()) {

            Trips trip = new Trips(0, trips.getLine(), trips.getArrival(), trips.getDeparture(), trips.getDuration());

            boolean state = adminService.addOrUpdateTrip(trip);

            assertEquals(true, state);
        }

    }

    public ArrayList<Trips> testAddtrips() {

        xmlFile = new XmlFile("src/main/resources/com/subway2feira/xml/Subway2Feira.xml",true);
        ArrayList<Trips> trips = new ArrayList<>();
        try {

            trips = xmlFile.getAllTrips();

        } catch (Exception e) {
            // TODO: handle exception
        }

        return trips;
    }

    @Test
    public void testGetAllTrip() {

        ArrayList<Trips> trips = adminService.getAllTrip();

        assertTrue(0 != trips.size());
    }

}

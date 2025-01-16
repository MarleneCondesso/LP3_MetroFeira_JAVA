package com.API;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Time;

import com.subway2feira.API.API;
import com.subway2feira.API.ContentType;
import com.subway2feira.API.RequestApi;
import com.subway2feira.API.Exeception.SubwayException;
import com.subway2feira.API.RequestApiTransportPass;
import com.subway2feira.API.models.ResponseDeletePass;
import com.subway2feira.API.models.ResponseTransportPass;
import com.subway2feira.API.models.TransportPass;
import com.subway2feira.API.models.TransportPassType;
import com.subway2feira.models.Ticket;
import com.subway2feira.models.Trips;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class APITest {

    API apiConnJSON;
    API apiConnXML;
    RequestApi rq;

    @Before
    public void setup() {
        this.apiConnJSON = new API(ContentType.JSON);
        this.apiConnXML = new API(ContentType.XML);

        long now = System.currentTimeMillis();
        Time sqlTime = new Time(now);
        rq = new RequestApi();
        rq.setCustomer("Tiago");
        rq.setBegin("Tiago");
        rq.setEnd("Tiago");
        rq.setPrice("2.655");
        rq.setDuration(sqlTime.toString());
    }

    @Test
    public void testGetTicketJson() {

        try {
            Ticket ticket = this.apiConnJSON.getTicket(this.rq);

            assertNotNull(ticket);

        } catch (SubwayException e) {
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void testGetTicketXML() {
        try {
            Ticket ticket = this.apiConnXML.getTicket(this.rq);

            assertNotNull(ticket);

        } catch (SubwayException e) {
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void testGetTripsJSON() {
        try {
            List<Trips> trips = this.apiConnJSON.getTrips();

            assertFalse(trips.isEmpty());

        } catch (SubwayException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testGetTripsXML() {
        try {
            List<Trips> trips = this.apiConnJSON.getTrips();

            assertFalse(trips.isEmpty());

        } catch (SubwayException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testGetTransportPassType() {
        try {

            TransportPassType transportPassType = this.apiConnJSON.getTransportPass();

            assertEquals(transportPassType.getStatus(), "OK");

        } catch (SubwayException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testCreatePass() {
        try {
            RequestApiTransportPass pass = new RequestApiTransportPass();
            pass.setClientID("12");
            pass.setPassTypeID("Sub99");
            pass.setExpirationDate("02-22-2025");
            pass.setActive(true);

            ResponseTransportPass responseTransportPass = this.apiConnJSON.createTransportPass(pass);
          
            assertEquals(responseTransportPass.getStatus(), "OK");

        } catch (SubwayException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testGetTransportPasses() {
        try {
            ResponseTransportPass responseTransportPass = this.apiConnJSON.getTransportPasses();

            assertEquals(responseTransportPass.getStatus(), "OK");

        } catch (SubwayException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testGetTransportPassesbyID() {

        try {
            ResponseTransportPass responseTransportPass = this.apiConnJSON
                    .getTransportPassesbyID("dd5920d5-ba87-4d1e-933f-0847cc8357b7");

            assertEquals(responseTransportPass.getStatus(), "OK");

        } catch (SubwayException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testDeleteTransportPass() {
        try {
            ResponseDeletePass responseDeletePass = this.apiConnJSON
                    .deleteTransportPass("fc0236b7-d12e-4256-b698-2a4dd648b1dd");

            assertEquals(responseDeletePass.getStatus(), "OK");

        } catch (SubwayException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testUpdatePass() {

        try {

            RequestApiTransportPass rPass = new RequestApiTransportPass();

            rPass.setClientID("12");
            rPass.setPassTypeID("Sub12");
            rPass.setExpirationDate("02-22-1893");
            rPass.setActive(false);

            ResponseTransportPass responseTransportPass = this.apiConnJSON.updateTransportPass(rPass,
                    "b713769c-cdf9-4772-bbe8-2b6ac7c8481e");

            assertEquals(responseTransportPass.getStatus(), "OK");

        } catch (SubwayException e) {
            System.out.println(e.getMessage());
        }

    }
}

package com.subway2feira.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CheckCredentialsTest {
    @Test
    public void testCheckEmailTrue() {

        assertEquals(true,CheckCredentials.CheckEmail("andre@gmail.com"));
    }

    @Test
    public void testCheckNifTrue() {
        
        assertEquals(true,CheckCredentials.CheckNif("123456789"));

    }

    @Test
    public void testCheckEmailFalse() {

        assertEquals(false,CheckCredentials.CheckEmail("andregmailcom"));
    }

    @Test
    public void testCheckNifFalse() {
        
        assertEquals(false,CheckCredentials.CheckNif("12389"));

    }
}

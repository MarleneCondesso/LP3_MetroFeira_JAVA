package com.subway2feira.utils;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;

public class MoneyChangeTest {
  
    MoneyChange moneyChange;

    @Before
    public void setup(){
        this.moneyChange = new MoneyChange();
    }

  
    @Test
    public void testGetMoneyChange() {

        double[][] _moneyChange = this.moneyChange.getMoneyChange(98.45);
        double[][] expectedOutput = initMoneyChange();

        assertArrayEquals(expectedOutput, _moneyChange);

    }





    private double[][] initMoneyChange() {
        return new double[][] {
                { 10, 9 },
                { 5, 1 },
                { 2, 1 },
                { 1, 1 },
                { 0.5, 0 },
                { 0.2, 2 },
                { 0.1, 0 },
                { 0.05, 1 }
        };
    }
}

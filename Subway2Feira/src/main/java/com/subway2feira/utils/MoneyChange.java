package com.subway2feira.utils;

public class MoneyChange {

    private double[][] initMoneyChange() {
        return new double[][] {
                { 10, 0 },
                { 5, 0 },
                { 2, 0 },
                { 1, 0 },
                { 0.5, 0 },
                { 0.2, 0 },
                { 0.1, 0 },
                { 0.05, 0 }
        };
    }
    
    private float change(double coin, double totalapy){
        return (float)totalapy/(float)coin;
    }

    /**
     * 
     * Retorna double[valor moeda][Total de moedas] 
     * 
     * @param totalRecive
     * @return retorna total de moedas a receber
     */
    public double[][] getMoneyChange(Double totalRecive) {
        
        double[][] _moneyChange = initMoneyChange();
        
        for (double[] ds : _moneyChange) {
       
            float total = change(ds[0], totalRecive);
            totalRecive %= ds[0]; 
            ds[1] =  (int)total;

            if(totalRecive == 0)
                return _moneyChange;
    
        }

        return _moneyChange;
    }

}

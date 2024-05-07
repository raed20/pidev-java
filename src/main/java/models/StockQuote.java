package models;

import io.fair_acc.dataset.spi.financial.api.ohlcv.IOhlcv;

public class StockQuote {


    public StockQuote() {

    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public long getVolume() {
        return volume;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }
private  String name;
    private double open;
    private double high;
    private double low;
    private double close;
    private long volume;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    private int day ;
    public StockQuote(String name ,int day,double open, double high, double low, double close, long volume) {
        this.name = name;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }


    @Override
    public String toString() {
        return "Stock: " + name + ", Open: " + open + ", High: " + high + ", Low: " + low + ", Close: " + close + ", Volume: " + volume;
    }


    public IOhlcv getOpenInterest() {
        return null;
    }
}

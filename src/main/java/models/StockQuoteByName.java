package models;

import io.fair_acc.dataset.spi.financial.api.ohlcv.IOhlcv;

public class StockQuoteByName {
    private  String name;
    private IOhlcv open;

    public StockQuoteByName(String symbol, int day, double open, double high, double low, double close, long volume) {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IOhlcv getOpen() {
        return open;
    }

    public void setOpen(IOhlcv open) {
        this.open = open;
    }

    public IOhlcv getHigh() {
        return high;
    }

    public void setHigh(IOhlcv high) {
        this.high = high;
    }

    public IOhlcv getLow() {
        return low;
    }

    public void setLow(IOhlcv low) {
        this.low = low;
    }

    public IOhlcv getClose() {
        return close;
    }

    public void setClose(IOhlcv close) {
        this.close = close;
    }

    public IOhlcv getVolume() {
        return volume;
    }

    public void setVolume(IOhlcv volume) {
        this.volume = volume;
    }
private IOhlcv day;

    public IOhlcv getDay() {
        return day;
    }

    public void setDay(IOhlcv day) {
        this.day = day;
    }

    private IOhlcv high;
    private IOhlcv low;
    private IOhlcv close;
    private IOhlcv volume;


}

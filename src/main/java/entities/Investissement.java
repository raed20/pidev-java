package entities;

import javafx.beans.property.*;

public class Investissement {
    private final IntegerProperty id;
    private final LongProperty montant;
    private final ObjectProperty<java.util.Date> dateInvest;
    private final FloatProperty totalValue;
    private final StringProperty stockName;
    private final FloatProperty changerate;
    private final FloatProperty price;
    public Integer getOpport() {
        return opport;
    }

    public void setOpport(Integer opport) {
        this.opport = opport;
    }

    private int opport;

    public Investissement() {
        this.id = new SimpleIntegerProperty();
        this.montant = new SimpleLongProperty();
        this.dateInvest = new SimpleObjectProperty<>();
        this.totalValue = new SimpleFloatProperty();
        this.stockName = new SimpleStringProperty();
        this.changerate = new SimpleFloatProperty();
        this.price = new SimpleFloatProperty();
    }

    // Getters and setters for each property
    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public long getMontant() {
        return montant.get();
    }

    public LongProperty montantProperty() {
        return montant;
    }

    public void setMontant(long montant) {
        this.montant.set(montant);
    }

    public java.util.Date getDateInvest() {
        return dateInvest.get();
    }

    public ObjectProperty<java.util.Date> dateInvestProperty() {
        return dateInvest;
    }

    public void setDateInvest(java.util.Date dateInvest) {
        this.dateInvest.set(dateInvest);
    }

    public float getTotalValue() {
        return totalValue.get();
    }

    public FloatProperty totalValueProperty() {
        return totalValue;
    }

    public void setTotalValue(float totalValue) {
        this.totalValue.set(totalValue);
    }

    public String getStockName() {
        return stockName.get();
    }

    public StringProperty stockNameProperty() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName.set(stockName);
    }

    public float getChangerate() {
        return changerate.get();
    }

    public FloatProperty changerateProperty() {
        return changerate;
    }

    public void setChangerate(float changerate) {
        this.changerate.set(changerate);
    }

    public float getPrice() {
        return price.get();
    }

    public FloatProperty priceProperty() {
        return price;
    }

    public void setPrice(float price) {
        this.price.set(price);
    }


}

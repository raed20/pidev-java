package entities;

public class Investissement {
    private Integer id;
    private Long montant;
    private java.util.Date dateInvest;

    private Float totalValue;
    private String stockName;
    private Float changerate;
    private Float price;

    public Integer getOpport() {
        return opport;
    }

    public void setOpport(Integer opport) {
        this.opport = opport;
    }

    private int opport;

    public Investissement() {
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getMontant() {
        return montant;
    }

    public void setMontant(Long montant) {
        this.montant = montant;
    }

    public java.util.Date getDateInvest() {
        return dateInvest;
    }

    public void setDateInvest(java.util.Date dateInvest) {
        this.dateInvest = dateInvest;
    }


    public Float getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Float totalValue) {
        this.totalValue = totalValue;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public Float getChangerate() {
        return changerate;
    }

    public void setChangerate(Float changerate) {
        this.changerate = changerate;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }}

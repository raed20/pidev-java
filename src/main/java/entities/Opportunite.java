package entities;
import java.util.Collection;

public class Opportunite {
    private Integer id;
    private String description;
    private Float prix;
    private String name;
    private Float lastprice;
    private Float yesterdaychange;
    private Float marketcap;
    private Collection<Investissement> investissements;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrix() {
        return prix;
    }

    public void setPrix(Float prix) {
        this.prix = prix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getLastprice() {
        return lastprice;
    }

    public void setLastprice(Float lastprice) {
        this.lastprice = lastprice;
    }

    public Float getYesterdaychange() {
        return yesterdaychange;
    }

    public void setYesterdaychange(Float yesterdaychange) {
        this.yesterdaychange = yesterdaychange;
    }

    public Float getMarketcap() {
        return marketcap;
    }

    public void setMarketcap(Float marketcap) {
        this.marketcap = marketcap;
    }

    public Collection<Investissement> getInvestissements() {
        return investissements;
    }

    public void setInvestissements(Collection<Investissement> investissements) {
        this.investissements = investissements;
    }


}

package entities;

import java.util.HashMap;
import java.util.Map;

public class Panier {
    // Attributes
    private int id;
    private Map<Product, Integer> products;

    // Constructors

    public Panier() {
        this.products = new HashMap<>(); // Initialize the products map
    }

    public Panier(int id, Map<Product, Integer> products) {
        this.id = id;
        this.products = products;
    }

    public Panier(Map<Product, Integer> products) {
        this.products = products;
    }

    // Getters & Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Product, Integer> products) {
        this.products = products;
    }


    // Display

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", products=" + products +
                '}';
    }


}

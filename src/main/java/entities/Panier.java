package entities;

import javafx.scene.control.Spinner;

import java.util.HashMap;
import java.util.Map;

public class Panier {
    //Attributes
    private int id;
    private Map<Product, Integer> products;

    //Constructors

    public Panier() {
    }

    public Panier(int id, Map<Product, Integer> products) {
        this.id = id;
        this.products = products;
    }

    public Panier(Map<Product, Integer> products) {
        this.products = products;
    }

    //Getters & Setters

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

    //Display

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", products=" + products +
                '}';
    }

    public void setProducts(int id, Spinner<Integer> qtyLabel) {
        // Initialize the products map if it's not already initialized
        if (this.products == null) {
            this.products = new HashMap<>();
        }
        // Add the product and quantity to the map
        Product product = new Product(); // Assuming you have a way to get the product
        int quantity = qtyLabel.getValue();
        this.products.put(product, quantity);
    }
}

package entities;

import java.util.Map;

public class Cart {
    //Attributes
    private int id;
    private Map<Product, Integer> products;

    //Constructors

    public Cart() {
    }

    public Cart(int id, Map<Product, Integer> products) {
        this.id = id;
        this.products = products;
    }

    public Cart(Map<Product, Integer> products) {
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
}

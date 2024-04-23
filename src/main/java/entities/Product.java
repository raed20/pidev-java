package entities;

public class Product {
    //Attributes
    private int id;
    private String name;
    private double price;
    private String description;
    private String image;
    private double discount;
    private int category_id;
    private int panier_id;

    //Constructors


    public Product() {
    }

    public Product(int id, String name, double price, String description, String image, double discount, int category_id) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image=image;
        this.discount=discount;
        this.category_id = category_id;
    }

    public Product(String name, double price, String description, String image, double discount, int category_id) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.image=image;
        this.discount=discount;
        this.category_id = category_id;
    }

    //Getters & Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getCategory() {
        return category_id;
    }

    public void setCategory(int category_id) {
        this.category_id = category_id;
    }

    //Display


    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", discount=" + discount +
                ", category_id=" + category_id +
                '}';
    }
}

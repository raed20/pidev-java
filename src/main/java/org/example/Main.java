package org.example;

import entities.Panier;
import services.PanierService;
import tools.MyConnection;

import java.util.List;

public class Main {
    public static void main(String[] args) {

       MyConnection cnx=new MyConnection();


        // Initialize PanierService
        PanierService panierService = new PanierService(cnx);

        // Retrieve all carts from the database
        List<Panier> paniers = panierService.getAll();

        // Display the retrieved carts
        for (Panier panier : paniers) {
            System.out.println(panier);
        }   
//
//        CategoryService cs=new CategoryService(cnx);
//        Category category=new Category("Car");
//        cs.add(category);
//
//        System.out.println(cs.getAll());
//
//        ProductService productService=new ProductService(cnx);

//        // Create a new product
//        Product product = new Product();
//        product.setName("Test Product");
//        product.setPrice(19.99);
//        product.setDescription("This is a test product");
//        product.setImage("test_image.jpg"); // Assuming you have an image file named test_image.jpg
//        product.setDiscount(0.1); // 10% discount
//        product.setCategory(1); // Assuming the category ID exists in the database
//
//        // Add the product
//        productService.add(product);
//
//        // Test retrieving the product
//        int productId = 1; // Assuming this is the ID of the product you just added
//        Product retrievedProduct = productService.getOne(productId);
//        System.out.println("Retrieved Product: " + retrievedProduct);

//        Category category1=new Category();
//        category1.setId(7);
//        category1.setName("Cars");
//        cs.update(category1);
//        //cs.delete(6);
//       System.out.println(cs.getAll());
//        System.out.println(cs.getOne(7));
//        ProductService ps=new ProductService();
//        Product product=new Product("Samsung",1.500,"telephone sumsung galaxy M12",new Category("Telephone"));
//        ps.add(product);
//        System.out.println(ps.getAll());
//        Product product1=new Product("Nokia",1.500,"telephone NOKIA 3310",category);
//        ps.add(product1);
//        System.out.println(ps.getOne(2));
//        ps.delete(1);
        // Test adding a product
//        ProductService productService = new ProductService();
//        CategoryService categoryService = new CategoryService();
//
//        // Create a sample category
//        Category category = new Category();
       // category.setName("Electro");
       // categoryService.add(category);
        // Create a product
//        Product product = new Product();
//        product.setName("Laptop");
//        product.setPrice(999.99);
//        product.setDescription("High-performance laptop");
//        product.setCategory(category.getId());

        //productService.add(new Product("Lenovo",15.500,"lenovo ideapad3",3));

//        System.out.println(productService.getAll());
//        System.out.println(productService.getOne(13));
//        productService.delete(10);

//        ProductService productService=new ProductService(cnx);
//        Product product1 = productService.getOne(14);
//        Product product2 = productService.getOne(15);
//
//     System.out.println(product1);
//        Map<Product, Integer> products = new HashMap<>();
//        products.put(product1, 2);
//        products.put(product2, 3);
//        Panier panier = new Panier(products);
//        PanierService panierService = new PanierService(cnx);
//        //cartService.add(cart);
//     panier.getProducts().put(product1, 15);
//     panierService.update(panier);
//     System.out.println(panier);
//     panierService.delete(5);
//     System.out.println(panierService.getOne(5));

    }
}
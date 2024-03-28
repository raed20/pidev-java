package org.example;

import entities.Cart;
import entities.Category;
import entities.Product;
import services.CartService;
import services.CategoryService;
import services.ProductService;
import tools.MyConnection;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

       MyConnection cnx=new MyConnection();
       // MyConnection cnx1=new MyConnection();

//        CategoryService cs=new CategoryService();
//        Category category=new Category("cars");
//        //cs.add(category);
//
//        System.out.println(cs.getAll());
//
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

        ProductService productService=new ProductService(cnx);
        Product product1 = productService.getOne(14);
        Product product2 = productService.getOne(15);

     System.out.println(product1);
        Map<Product, Integer> products = new HashMap<>();
        products.put(product1, 2);
        products.put(product2, 3);
        Cart cart = new Cart(products);
        CartService cartService = new CartService(cnx);
        //cartService.add(cart);
     cart.getProducts().put(product1, 15);
     cartService.update(cart);
     System.out.println(cart);
     cartService.delete(5);
     System.out.println(cartService.getOne(5));
    }
}
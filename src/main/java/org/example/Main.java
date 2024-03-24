package org.example;

import entities.Category;
import services.CategoryService;
import tools.MyConnection;
public class Main {
    public static void main(String[] args) {

       MyConnection cnx=MyConnection.getInstance();
        MyConnection cnx1=MyConnection.getInstance();
       // MyConnection cnx1=new MyConnection();

        CategoryService cs=new CategoryService();
        Category category=new Category("cars");
        //cs.add(category);

        System.out.println(cs.getAll());

        Category category1=new Category();
        category1.setId(7);
        category1.setName("Cars");
        cs.update(category1);
        //cs.delete(6);
        System.out.println(cs.getAll());
        System.out.println(cs.getOne(7));
    }
}
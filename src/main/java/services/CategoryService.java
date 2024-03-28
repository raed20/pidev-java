package services;

import entities.Category;
import interfaces.IService;
import tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryService implements IService<Category> {

    Connection connection= MyConnection.getInstance().getConnection();
//    @Override
//    public void add(Category category){
//        String req="INSERT INTO `category`(`name`) VALUES ('"+category.getName()+"')";
//        try {
//            Statement st=connection.createStatement();
//            st.executeUpdate(req);
//            System.out.println("Category added succesfully !");
//        }catch (SQLException e){
//            throw new RuntimeException(e);
//        }
//    }


    @Override
    public void add(Category category){
        String req="INSERT INTO category(name) VALUES (?)";
        try {
            PreparedStatement ps=connection.prepareStatement(req);
            ps.setString(1,category.getName());
            ps.executeUpdate();
            System.out.println("Category added succesfully !");
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Category category) {
        String query = "UPDATE category SET name = ? WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, category.getName());
            statement.setInt(2, category.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Category Updated !");
    }
    @Override
    public void delete(int id) {
        String req = "DELETE FROM category WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(req);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Category Deleted !");
    }
    @Override
    public List<Category> getAll(){
        List<Category> categories=new ArrayList<>();
        String req = "SELECT * FROM category";
        try {
            Statement st=connection.createStatement();
            ResultSet res = st.executeQuery(req);
            while (res.next()) {
                Category category=new Category();
                category.setId(res.getInt("id"));
                category.setName(res.getString(2));
                categories.add(category);
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }
    @Override
    public Category getOne(int id){
        String req = "SELECT * FROM category WHERE id = ?";
        Category category = null;
        try {
            PreparedStatement st = connection.prepareStatement(req);
            st.setInt(1, id);
            try (ResultSet res = st.executeQuery()) {
                if (res.next()) {
                    category = new Category();
                    category.setId(res.getInt("id"));
                    category.setName(res.getString("name"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return category;
    }
}

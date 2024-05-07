package services;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Blog;
import entities.Commentaire;
import interfaces.IBlog;
import tools.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BlogService implements IBlog {
    private MyConnection connection;
    private static final Logger LOGGER = Logger.getLogger(MyConnection.class.getName());

    private Statement st;
    private ResultSet rs;


    public BlogService(MyConnection connection) {
        this.connection = connection;
    }

    public BlogService() {
        MyConnection cs=MyConnection.getInstance();
        try {
            st=cs.getConnection().createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(BlogService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void addBlog(Blog blog) {
        String query = "INSERT INTO Blog (Title, Description, Content, Img) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, blog.getTitle());
            statement.setString(2, blog.getDescription());
            statement.setString(3, blog.getContent());
            statement.setString(4, blog.getImg());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQL Exception occurred:", e);
        }
    }

    @Override
    public void deleteBlog(int id) {
        String query = "DELETE FROM Blog WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQL Exception occurred:", e);
        }
    }

    @Override
    public void updateBlog(Blog blog) {
        String query = "UPDATE blog SET Title = ?, Description = ?, Content = ?, Img = ?, Rating = ?, Vu = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, blog.getTitle());
            statement.setString(2, blog.getDescription());
            statement.setString(3, blog.getContent());
            statement.setString(4, blog.getImg());
            statement.setDouble(5, blog.getRating());
            statement.setInt(6, blog.getVu());
            statement.setInt(7, blog.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQL Exception occurred:", e);
        }
    }


    @Override
    public List<Blog> getAllBlog() {
        List<Blog> blogs = new ArrayList<>();
        String query = "SELECT * FROM blog";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Blog blog = new Blog();
                blog.setId(resultSet.getInt("id"));
                blog.setTitle(resultSet.getString("Title"));
                blog.setDescription(resultSet.getString("Description"));
                blog.setContent(resultSet.getString("Content"));
                blog.setImg(resultSet.getString("Img"));
                blog.setRating(resultSet.getFloat("Rating"));
                blog.setVu(resultSet.getInt("Vu"));
                blogs.add(blog);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQL Exception occurred:", e);
        }
        return blogs;
    }

    @Override
    public List<Blog> displayAllList() {
        String query = "SELECT * FROM blog";
        List<Blog> list=new ArrayList<>();

        try {
            st=MyConnection.getInstance().getConnection().createStatement();
            rs=st.executeQuery(query);
            while(rs.next()){
                Blog b=new Blog();
                b.setId(rs.getInt(1));
                b.setTitle(rs.getString(2));
                b.setDescription(rs.getString(3));
                b.setContent(rs.getString(4));
                b.setImg(rs.getString(5));
                b.setRating(rs.getFloat(6));
                b.setVu(rs.getInt(7));

                list.add(b);
            }

        } catch (SQLException ex) {
            Logger.getLogger(BlogService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public List<Commentaire> getCommentaireByBlogId(int blogId) {
        String query = "SELECT * FROM Commentaire WHERE blog_id = ?";
        List<Commentaire> list = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, blogId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Commentaire commentaire = new Commentaire();
                    commentaire.setId(resultSet.getInt("id"));
                    commentaire.setContent(resultSet.getString("Content"));
                    commentaire.setBlog_id(resultSet.getInt("blog_id"));
                    list.add(commentaire);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(BlogService.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }



}
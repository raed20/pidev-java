package services;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Blog;
import entities.Commentaire;
import interfaces.IBlog;
import tools.MyConnection;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BlogService implements IBlog {
    private MyConnection connection;
    private static final Logger LOGGER = Logger.getLogger(MyConnection.class.getName());


    public BlogService(MyConnection connection) {
        this.connection = connection;
    }

    @Override
    public void addBlog(Blog Blog) {
        String query = "INSERT INTO Blog (Title, Description, Content) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, Blog.getTitle());
            statement.setString(2, Blog.getDescription());
            statement.setString(3, Blog.getContent());
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
    public void updateBlog(Blog Blog) {
        String query = "UPDATE Blog SET Title = ?, Description = ?, Content = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, Blog.getTitle());
            statement.setString(2, Blog.getDescription());
            statement.setString(3, Blog.getContent());

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQL Exception occurred:", e);
        }
    }

    @Override
    public List<Blog> getAllBlog() {
        List<Blog> Blogs = new ArrayList<>();
        String query = "SELECT * FROM Blog";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Blog blog = new Blog();
                blog.setId(resultSet.getInt("id"));
                blog.setTitle(resultSet.getString("Title"));
                blog.setDescription(resultSet.getString("Description"));
                blog.setContent(resultSet.getString("Content"));
                // Fetch Commentaire entity for this Blog
                CommentaireService commentaireServiceService = new CommentaireService(connection);
                blog.setComment(commentaireServiceService.getCommentaireById(resultSet.getInt("comment_id")).getId());
                Blogs.add(blog);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQL Exception occurred:", e);
        }
        return Blogs;
    }
    @Override
    public List<Blog> getCommentaireByBlogId(int CommentaireId) {
        String query = "SELECT * FROM commentaire WHERE id = (SELECT comment_id FROM blog WHERE id = ?)";
        List<Blog> blogs = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, CommentaireId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Commentaire commentaire = new Commentaire();
                    commentaire.setId(resultSet.getInt("id"));
                    commentaire.setContent(resultSet.getString("Content"));
                    Blog blog = new Blog();
                    blog.setComment(commentaire.getId());
                    blogs.add(blog);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An SQL Exception occurred:", e);
        }
        return blogs;
    }
}
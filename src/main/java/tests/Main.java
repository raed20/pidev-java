package tests;

import entities.Blog;
import entities.Commentaire;
import entities.Investissement;
import entities.Opportunite;
import services.BlogService;
import services.CommentaireService;
import services.InvestissementService;
import services.OpportuniteService;
import tools.MyConnection;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        MyConnection connection = new MyConnection(); // Assuming you have a MyConnection class

        BlogService blogService = new BlogService(connection);
        CommentaireService commentService = new CommentaireService(connection);

        // Test Add operation for Blog
        System.out.println("Adding a new blog...");
        Blog newBlog = new Blog();
        // Set properties for the new investment
        newBlog.setTitle("Title"); // Example value, replace with actual value
        newBlog.setDescription("Descr"); // Example value, replace with actual value
        newBlog.setContent("Content"); // Example value, replace with actual value


        try {
            blogService.addBlog(newBlog);
            System.out.println("New blog added successfully: " + newBlog);
        } catch (Exception e) {
            System.err.println("Error adding new blog: " + e.getMessage());
        }

        // Test Add operation for Comment
        System.out.println("Adding a new comment...");
        Commentaire newCommentaire = new Commentaire();
        // Set properties for the new comment
        newCommentaire.setContent("New comment "); // Example value, replace with actual value


        try {
            commentService.addCommentaire(newCommentaire);
            System.out.println("New comment added successfully: " + newCommentaire);
        } catch (Exception e) {
            System.err.println("Error adding new comment: " + e.getMessage());
        }

    }
}
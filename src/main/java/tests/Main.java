package tests;

import entities.Blog;
import entities.Commentaire;
import services.BlogService;
import tools.MyConnection;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Create a MyConnection object
        MyConnection connection = new MyConnection();

        // Instantiate BlogService
        BlogService blogService = new BlogService(connection);

        // Assuming you want to get the comments for a specific blog with ID 1
        int blogId = 7;

        // Test getCommentaireByBlogId
        List<Commentaire> blogsWithComments = blogService.getCommentaireByBlogId(blogId);

        // Print the blogs with comments
        System.out.println("Blogs with Comments:");
        for (Commentaire commentaire : blogsWithComments) {
            // System.out.println("Blog ID: " + commentaire.getId());
            // System.out.println("Title: " + commentaire.getTitle());
            // System.out.println("Description: " + commentaire.getDescription());
            // System.out.println("Content: " + commentaire.getContent());
            // Add code to display comments associated with this blog if needed
        }

        // Close the connection
        connection.closeConnection();
    }
}

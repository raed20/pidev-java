package tests;

import entities.Blog;
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
        List<Blog> blogsWithComments = blogService.getCommentaireByBlogId(blogId);

        // Print the blogs with comments
        System.out.println("Blogs with Comments:");
        for (Blog blog : blogsWithComments) {
            System.out.println("Blog ID: " + blog.getId());
            System.out.println("Title: " + blog.getTitle());
            System.out.println("Description: " + blog.getDescription());
            System.out.println("Content: " + blog.getContent());
            // Add code to display comments associated with this blog if needed
        }

        // Close the connection
        connection.closeConnection();
    }
}

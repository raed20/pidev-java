package tests;

<<<<<<< HEAD
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.CountryController;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        try {
            // Read the JSON file containing country data
            String jsonString = new String(Files.readAllBytes(Paths.get("\\Users\\gigab\\Desktop\\pidev-java\\src\\main\\resources\\CountryCodes.json")));

            // Parse the JSON string into a JSONArray
            JSONArray countries = new JSONArray(jsonString);

            // Search for the dial code of a country by name
            String countryName = "Tunisia"; // The country name to search for
            String dialCode = findDialCode(countries, countryName);
            if (dialCode != null) {
                System.out.println("The dial code for " + countryName + " is " + dialCode);
            } else {
                System.out.println("Dial code not found for " + countryName);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
=======
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
>>>>>>> f6759ea7b6dfbab61e3d1719b5ef12c97bc0ee5f
        }
    }

<<<<<<< HEAD
    private static String findDialCode(JSONArray countries, String countryName) {
        for (int i = 0; i < countries.length(); i++) {
            try {
                JSONObject country = countries.getJSONObject(i);
                if (country.getString("name").equals(countryName)) {
                    return country.getString("dial_code");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null; // Dial code not found
=======
        // Close the connection
        connection.closeConnection();
>>>>>>> f6759ea7b6dfbab61e3d1719b5ef12c97bc0ee5f
    }
}

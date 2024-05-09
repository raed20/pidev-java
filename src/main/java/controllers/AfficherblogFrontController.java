package controllers;

import entities.Blog;
import entities.Commentaire;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;

import java.util.Optional;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import services.BlogService;

import java.awt.*;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.io.FileInputStream;
import java.util.logging.Logger;
import java.util.logging.Level;

import services.CommentaireService;
import tools.MyConnection;
import javafx.scene.text.Font;
import org.controlsfx.control.Rating;

import javax.sound.midi.Synthesizer;


public class AfficherblogFrontController implements Initializable {

    @FXML
    private TilePane blog_tile;
    private Synthesizer synthesizer;
    Blog blog = new Blog();
    MyConnection myConnection = new MyConnection(); // Creating an instance of MyConnection

    BlogService bs = new BlogService(myConnection);
    CommentaireService cs = new CommentaireService(myConnection);



    private Image image;



    private SpinnerValueFactory<Integer> spin;

    public float calculateAverageRating() {
        // Fetch all blogs
        List<Blog> blogs = bs.displayAllList();

        // Initialize variables to calculate sum and count
        float sum = 0;
        int count = 0;

        // Iterate over each blog
        for (Blog blog : blogs) {
            // Add the rating to the sum
            sum += blog.getRating();
            // Increment the count
            count++;
        }

        // Calculate the average rating
        float averageRating = (count > 0) ? (sum / count) : 0;

        return averageRating;
    }

    public void setData(Blog blog) {
        this.blog = blog;

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        show_blog(null);
    }


    private String serializeBlog(Blog blog) {
        // Serialize the blog content for sharing on Twitter
        String serializedBlog = "Check out this blog: " + blog.getTitle() + ". " +
                "Description: " + blog.getDescription() + ". " +
                "Content: " + blog.getContent();
        return serializedBlog;
    }

    public void shareTwitter(Blog blog) {
        // Encode the blog content for URL
        String blogContent = serializeBlog(blog);
        try {
            String encodedContent = URLEncoder.encode(blogContent, "UTF-8");

            // Construct the Twitter share URL
            String twitterUrl = "https://twitter.com/intent/tweet?text=" + encodedContent;

            // Open the Twitter share URL in the default browser
            Desktop.getDesktop().browse(new URI(twitterUrl));
        } catch (UnsupportedEncodingException e) {
            System.err.println("Error encoding blog content: " + e.getMessage());
        } catch (IOException | URISyntaxException e) {
            System.err.println("Error opening Twitter share URL: " + e.getMessage());
        }
    }
    private void show_blog(ActionEvent event) {


        blog_tile.getChildren().clear();

        List<Blog> items = new ArrayList<>();
        items=bs.displayAllList();

        for (Blog item : items) {

            Image image;
            try {
                image = new Image(new FileInputStream(item.getImg()));
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(150);
                imageView.setFitWidth(150);

                Label title = new Label(item.getTitle());
                title.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333333;");
                Label vu = new Label();
                IntegerProperty viewCountProperty = new SimpleIntegerProperty(item.getVu());
                vu.textProperty().bind(viewCountProperty.asString());




                VBox vBox = new VBox();

                vBox.getChildren().addAll(imageView, title,vu);

                vBox.setAlignment(Pos.CENTER);
                vBox.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-padding: 10px;");
                blog_tile.getChildren().add(vBox);





                vBox.setOnMouseClicked(e -> {
                    // Create a new stage to display the  details
                    Stage detailsStage = new Stage();
                    detailsStage.setTitle("Blog Details");
                    item.setVu(item.getVu() + 1);
                    viewCountProperty.set(item.getVu());
                    bs.updateBlog(item);
                    // Create text fields to display the  details
                    Label description = new Label(item.getDescription());
                    description.setStyle("-fx-background-radius: 20; -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.5) , 10,0,0,1 );");

                    Label content = new Label(item.getContent());
                    content.setStyle("-fx-background-radius: 20; -fx-effect: dropshadow( gaussian , rgba(0,0,0,0.5) , 10,0,0,1 );");

                    // Fetch comments for the blog item
                    List<Commentaire> comments = bs.getCommentaireByBlogId(item.getId());

                    // Create a ListView to display the comments
                    ObservableList<String> commentsList = FXCollections.observableArrayList();
                    ListView<String> commentsListView = new ListView<>(commentsList);
                    commentsListView.setPrefSize(200, 100); // Set preferred width and height



                    //commentsListView.getItems().clear(); // Clear the items in case the ListView is reused
                    for (Commentaire comment : comments) {
                        commentsListView.getItems().add(comment.getContent());
                    }



                    // Create a text field and a button for adding new comments
                    TextField newCommentField = new TextField();
                    Button addCommentButton = new Button("Add Comment");

                    addCommentButton.setOnAction(event1 -> {
                        // Add the logic to add a new comment here
                        String newCommentContent = newCommentField.getText();

                        // Define the banned words
                        List<String> bannedWords = new ArrayList<>();
                        bannedWords.add("word1");
                        bannedWords.add("word2");
                        // Add more banned words as needed

                        // Iterate over banned words
                        for (String word : bannedWords) {
                            // Check if the comment contains the banned word
                            int index = newCommentContent.toLowerCase().indexOf(word.toLowerCase());
                            while (index >= 0) {
                                // Replace the middle letters with "*"
                                String replacement = word.substring(0, 1) +
                                        "*".repeat(word.length() - 2) +
                                        word.substring(word.length() - 1);
                                // Replace the banned word with the modified version
                                newCommentContent = newCommentContent.substring(0, index) + replacement + newCommentContent.substring(index + word.length());
                                // Check for the next occurrence of the banned word
                                index = newCommentContent.toLowerCase().indexOf(word.toLowerCase(), index + replacement.length());
                            }
                        }

                        // Add the comment to the database
                        Commentaire newComment = new Commentaire();
                        newComment.setContent(newCommentContent);
                        newComment.setBlog_id(item.getId());

                        // Add the new comment to the database
                        cs.addCommentaire(newComment);

                        // Clear the comment field
                        newCommentField.clear();

                        // Refresh the comments ListView
                        commentsList.clear();
                        List<Commentaire> updatedComments = bs.getCommentaireByBlogId(item.getId());
                        for (Commentaire comment : updatedComments) {
                            commentsList.add(comment.getContent());
                        }
                    });


                    ContextMenu contextMenu = new ContextMenu();
                    MenuItem deleteMenuItem = new MenuItem("Delete");
                    contextMenu.getItems().add(deleteMenuItem);

                    deleteMenuItem.setOnAction(event3 -> {
                        String selectedComment = commentsListView.getSelectionModel().getSelectedItem();
                        Optional<Commentaire> selectedCommentaire = comments.stream()
                                .filter(comment -> comment.getContent().equals(selectedComment))
                                .findFirst();
                        selectedCommentaire.ifPresent(comment -> {
                            cs.deleteCommentaire(comment.getId());
                            comments.remove(comment); // Remove the comment from the list
                            commentsList.remove(selectedComment); // Remove the comment from the observable list
                            commentsListView.refresh(); // Refresh the ListView
                        });
                    });

                    commentsListView.setContextMenu(contextMenu);
                    commentsListView.setOnMouseClicked(event4 -> {
                        if (event4.getButton() == MouseButton.SECONDARY) {
                            contextMenu.show(commentsListView, event4.getScreenX(), event4.getScreenY());
                        }
                    });


                    Rating rating = new Rating();
                    rating.setPartialRating(true);
                    rating.setRating(item.getRating());

                    float averageRating = calculateAverageRating();
                    // Create a label to display the average rating
                    Label averageRatingLabel = new Label(String.format("Average Rating: %.1f", averageRating));
                    averageRatingLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666666;");

                    Button shareButton = new Button("Share Blog");
                    shareButton.setOnAction(event2 -> shareTwitter(item)); // Call shareTwitter method when clicked


                    rating.ratingProperty().addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                            item.setRating(newValue.floatValue());
                            bs.updateBlog(item);
                        }
                    });
                    // Add the text fields and button to a VBox
                    VBox addCommentBox = new VBox(10);
                    addCommentBox.getChildren().addAll(newCommentField, addCommentButton, rating, shareButton,averageRatingLabel);

                    // Add the text fields to a grid pane
                    GridPane detailsPane = new GridPane();
                    detailsPane.setAlignment(Pos.CENTER);
                    detailsPane.setVgap(20);
                    detailsPane.setHgap(20);
                    detailsPane.setStyle("-fx-background-color: #ffffff; -fx-padding: 20px;");

                    Label descriptionLabel = new Label("Description: ");
                    descriptionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                    descriptionLabel.setStyle("-fx-text-fill: #333333;");

                    Label contentLabel = new Label("Content: ");
                    contentLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                    contentLabel.setStyle("-fx-text-fill: #333333;");

                    Label commentLabel = new Label("Comments: ");
                    commentLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                    commentLabel.setStyle("-fx-text-fill: #333333;");

// Style the description and content labels
                    description.setStyle("-fx-font-size: 14px; -fx-text-fill: #555555;");
                    content.setStyle("-fx-font-size: 14px; -fx-text-fill: #555555;");

// Style the comments ListView
                    commentsListView.setStyle("-fx-font-size: 14px; -fx-background-color: #f0f0f0;");
                    commentsListView.setFocusTraversable(false);

// Style the new comment field and add comment button
                    newCommentField.setStyle("-fx-font-size: 14px; -fx-background-color: #CACACA;");
                    addCommentButton.setStyle("-fx-font-size: 14px; -fx-background-color: #4CAF50; -fx-text-fill: #ffffff; -fx-padding: 8px 16px; -fx-border-radius: 5px;");

// Add the components to the detailsPane
                    detailsPane.add(descriptionLabel, 0, 0);
                    detailsPane.add(description, 1, 0);
                    detailsPane.add(contentLabel, 0, 1);
                    detailsPane.add(content, 1, 1);
                    detailsPane.add(commentLabel, 0, 2);
                    detailsPane.add(commentsListView, 1, 2);
                    detailsPane.add(addCommentBox, 1, 3); // Add the VBox containing the new comment field and button

// Add the grid pane to the scene
                    Scene detailsScene = new Scene(detailsPane, 600, 400);
                    detailsStage.setScene(detailsScene);

                    // Show the stage
                    detailsStage.show();

                });



            } catch (FileNotFoundException ex) {
                Logger.getLogger(AfficherblogFrontController.class.getName()).log(Level.SEVERE, null, ex);
            }






        }


    }

}
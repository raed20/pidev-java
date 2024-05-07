package controllers;

import entities.Blog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import services.BlogService;
import tools.MyConnection;

import javax.swing.text.AbstractDocument;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddBlogController implements Initializable {

    @FXML
    private TextField Content;

    @FXML
    private Button addButton;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button cancelButton;

    @FXML
    private Label contentValidationLabel;

    @FXML
    private TextField decription;

    @FXML
    private Label descriptionValidationLabel;

    @FXML
    private TextField title;

    @FXML
    private Label titleValidationLabel;

    @FXML
    private ImageView img_blog;
    @FXML
    private Label ImageValidationLabel;



    private BlogService blogService;

    private boolean validationRequired = false;

    private String selectedBlog;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the service with the database connection
        MyConnection myConnection = new MyConnection(); // Creating an instance of MyConnection
        blogService = new BlogService(myConnection); // Passing myConnection to the constructor
        title.textProperty().addListener((observable, oldValue, newValue) -> {
            if (validationRequired) validateTitle();
        });
        decription.textProperty().addListener((observable, oldValue, newValue) -> {
            if (validationRequired) validateDescription();
        });
        Content.textProperty().addListener((observable, oldValue, newValue) -> {
            if (validationRequired) validateContent();
        });
        img_blog.imageProperty().addListener((observable, oldValue, newValue) -> {
            if (validationRequired) validateImage();
        });
        addButton.setOnAction(event -> handleAddButtonAction());
        cancelButton.setOnMouseClicked(event -> handleCancelButtonAction());

    }

    @FXML
    void upload_img(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")+ "/Downloads"));
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image files", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp");
        fileChooser.getExtensionFilters().add(imageFilter);
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            Image img = new Image(selectedFile.toURI().toString());

            selectedBlog =selectedFile.getAbsolutePath();
            selectedBlog = selectedBlog.replace(File.separator, "/");
            img_blog.setImage(img);


        }
    }

    private void validateTitle() {
        String Title = title.getText();
        if (Title.isEmpty()) {
            titleValidationLabel.setText("Title is required.");
        } else {
            titleValidationLabel.setText("");
        }
    }

    private void validateDescription() {
        String Description = decription.getText();
        if (Description.isEmpty()) {
            descriptionValidationLabel.setText("Description is required.");
        } else {
            descriptionValidationLabel.setText("");
        }
    }


    private void validateContent() {
        String content = Content.getText();
        if (content.isEmpty()) {
            contentValidationLabel.setText("Content is required.");
        } else {
            titleValidationLabel.setText("");
        }
    }

    private void validateImage() {
        if (img_blog.getImage() == null) {
            ImageValidationLabel.setText("Image is required.");
        } else {
            ImageValidationLabel.setText("");
        }
    }

    private boolean validateInputs() {
        validationRequired = true;
        validateTitle();
        validateDescription();
        validateContent();
        validateImage();


        // Check if any validation message is displayed
        boolean isValid = titleValidationLabel.getText().isEmpty() &&
                descriptionValidationLabel.getText().isEmpty() &&
                contentValidationLabel.getText().isEmpty()&&
                ImageValidationLabel.getText().isEmpty();

        // Reset validationRequired flag
        validationRequired = false;

        return isValid;
    }

    private void handleAddButtonAction() {
        if (validateInputs()) {
            // Retrieve data from text fields
            String Title = title.getText();
            String Description = decription.getText();
            String content = Content.getText();
            // Create a new blog object
            Blog newblog = new Blog();
            newblog.setTitle(Title);
            newblog.setDescription(Description);
            newblog.setContent(content);
            newblog.setImg(selectedBlog);


            // Add the new opportunity to the database
            blogService.addBlog(newblog);
            loadPage("/JavaFX/BackOffice/Blog/AfficheBack.fxml");
        }
    }

    @FXML
    private void handleCancelButtonAction() {
        loadPage("/JavaFX/BackOffice/Blog/AfficheBack.fxml");
    }

    private void loadPage(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            anchorPane.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any potential errors loading the FXML file
        }
    }


}

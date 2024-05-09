package controllers;

import entities.Blog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

import java.io.File;
import java.io.IOException;

public class updateblogController {

    @FXML
    private TextField Content;

    @FXML
    private Label ImageValidationLabel;

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
    private ImageView img_blog;

    @FXML
    private TextField title;

    @FXML
    private Label titleValidationLabel;

    @FXML
    private Button updateButton;


    private BlogService blogService;
    private boolean validationRequired = false;
    Blog blogUpdate = new Blog();

    private String selectedBlog;


    public void initialize() {
        MyConnection myConnection = new MyConnection();
        blogService = new BlogService(myConnection);

        title.textProperty().addListener((observable, oldValue, newValue) -> {
            if (validationRequired) validatetitle();
        });

        decription.textProperty().addListener((observable, oldValue, newValue) -> {
            if (validationRequired) validateDescription();
        });

        Content.textProperty().addListener((observable, oldValue, newValue) -> {
            if (validationRequired) validatecontent();
        });

        img_blog.imageProperty().addListener((observable, oldValue, newValue) -> {
            if (validationRequired) validateImage();
        });



        updateButton.setOnAction(event -> handleUpdateButtonAction());
        cancelButton.setOnAction(event -> handleCancelButtonAction());
    }

    private void validatetitle() {
        if (title.getText().isEmpty()) {
            titleValidationLabel.setText("Title is required.");
        } else {
            titleValidationLabel.setText("");
        }
    }

    private void validateDescription() {
        if (decription.getText().isEmpty()) {
            titleValidationLabel.setText("Description is required.");
        } else {
            titleValidationLabel.setText("");
        }
    }

    private void validatecontent() {
        if (Content.getText().isEmpty()) {
            titleValidationLabel.setText("Content is required.");
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
        validatetitle();
        validateDescription();
        validatecontent();
        validateImage(); // Also validate the image

        return titleValidationLabel.getText().isEmpty() &&
                descriptionValidationLabel.getText().isEmpty() &&
                contentValidationLabel.getText().isEmpty() &&
                ImageValidationLabel.getText().isEmpty();
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
    public void setBlog(Blog blog) {
        this.blogUpdate = blog;

        // Pre-populate fields with blog data
        title.setText(blog.getTitle());
        decription.setText(blog.getDescription());
        Content.setText(blog.getContent());

        File imgFile = new File(blog.getImg());
        Image img = new Image(imgFile.toURI().toString());
        img_blog.setImage(img);
    }

    private void handleUpdateButtonAction() {
        if (validateInputs()) {
            // Retrieve data from text fields
            String Title = title.getText();
            String Description = decription.getText();
            String content = Content.getText();

            // Create a new Blog object

            blogUpdate.setTitle(Title);
            blogUpdate.setDescription(Description);
            blogUpdate.setContent(content);
            blogUpdate.setImg(selectedBlog);


            // Perform the update operation
            blogService.updateBlog(blogUpdate);
            // You can handle the success case here, such as showing a success message
            System.out.println(blogUpdate.getId());
            loadPage("/JavaFX/BackOffice/Blog/AfficheBack.fxml");
        }
    }

    private void handleCancelButtonAction() {
        loadPage("/Javafx/BackOffice/Blog/AfficheBack.fxml");
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


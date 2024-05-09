package controllers;

import entities.Blog;
import interfaces.IBlog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import services.BlogService;
import tools.MyConnection;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class BlogBackController {

    @FXML
    private TableColumn<?, ?> Description;

    @FXML
    private TableColumn<?, ?> Title;

    @FXML
    private Button addButton;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button deleteButton;

    @FXML
    private TableColumn<?, ?> idColumn;

    @FXML
    private TableView<Blog> blogTable;

    @FXML
    private Button updateButton;
    private IBlog blogService;

    public BlogBackController() {
        MyConnection connection = new MyConnection();
        this.blogService = new BlogService(connection);
    }

    @FXML
    public void initialize() {
        // Initialize columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        Title.setCellValueFactory(new PropertyValueFactory<>("Title"));
        Description.setCellValueFactory(new PropertyValueFactory<>("Description"));
        //addButton.setOnMouseClicked(event -> loadPage("/Javafx/BackOffice/Blog/AddBlog.fxml"));

        // Load data into TableView
        loadBlog();

        // Add buttons to each row for update and delete
        addButtonToTable();
        addButton.setOnMouseClicked(event -> loadPage("/JavaFX/BackOffice/Blog/AddBlog.fxml"));

    }

    private void loadBlog() {
        List<Blog> blogs = blogService.getAllBlog();
        ObservableList<Blog> observableblogs = FXCollections.observableArrayList(blogs);
        blogTable.setItems(observableblogs);
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

    private void addButtonToTable() {
        TableColumn<Blog, Void> colBtn = new TableColumn<>("Actions");

        Callback<TableColumn<Blog, Void>, TableCell<Blog, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Blog, Void> call(final TableColumn<Blog, Void> param) {
                final TableCell<Blog, Void> cell = new TableCell<>() {
                    private final Button deleteButton = new Button("Delete");
                    private final Button updateButton = new Button("Update");

                    {
                        deleteButton.setOnAction(event -> {
                            Blog blog = getTableView().getItems().get(getIndex());
                            deleteBlog(blog);
                        });
                        updateButton.setOnAction(event -> {
                            Blog blog = getTableView().getItems().get(getIndex());
                            updateBlog(blog);
                        });

                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox buttons = new HBox(updateButton, deleteButton);
                            setGraphic(buttons);
                        }
                    }
                };
                return cell;
            }
        };
        colBtn.setCellFactory(cellFactory);

        blogTable.getColumns().add(colBtn);
    }

    private void deleteBlog(Blog blog) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Blog");
        alert.setContentText("Are you sure you want to delete this blog?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            blogService.deleteBlog(blog.getId());
            blogTable.getItems().remove(blog);
            showAlert(Alert.AlertType.INFORMATION, "Blog Deleted", "Blog deleted successfully.");
        }
    }

    private void updateBlog(Blog blog) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/JavaFX/BackOffice/Blog/updateBlog.fxml"));
        Parent root;
        try {
            root = loader.load();
            updateblogController controller = loader.getController();
            controller.setBlog(blog);
            anchorPane.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any potential errors loading the FXML file
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}

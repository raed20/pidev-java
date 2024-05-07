package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import entities.Bank;
import org.controlsfx.control.textfield.TextFields;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import services.BankService;

public class BankController {

    @FXML private Label idField;
    @FXML private TextField banknameField;
    @FXML private TextField adresseField;
    //@FXML private TextField codeswift;
    @FXML private Button imageField;
    @FXML private Button dashbord;
    @FXML private TextField phonenum;
    @FXML private ImageView imageView;
    @FXML private TableView<Bank> tableView;
    @FXML private TableColumn<Bank, Integer> colId;
    @FXML private TableColumn<Bank, String> colnom;
    @FXML private TableColumn<Bank, String> coladresse;
    @FXML private TableColumn<Bank, String> colcodeswift;
    @FXML private TableColumn<Bank, String> colphone;
    @FXML private TableColumn<Bank, String> colImage;

    private BankService b = new BankService();
    private final CountryController countryController = new CountryController();


    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colnom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        coladresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colcodeswift.setCellValueFactory(new PropertyValueFactory<>("codeSwift"));
        colImage.setCellValueFactory(new PropertyValueFactory<>("logo"));
        colphone.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        loadTableData();

        TextFields.bindAutoCompletion(adresseField, input -> {
            String userText = input.getUserText();
            try {
                return countryController.getSuggestions(userText);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        adresseField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                String dialCode = findAndPrintDialCode(newValue);
                if (dialCode != null) {
                    phonenum.setText(dialCode);
                }
            }
        });


        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectData();
            }
        });
        dashbord.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafx/BackOffice/DashbordBack.fxml"));
            Parent form;
            try {
                form = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Get the current scene from the button
            Scene currentScene = dashbord.getScene();

            // Set the new content in the current scene
            currentScene.setRoot(form);
        });
    }

        private boolean validateInput() {
        String errorMessage = "";

        // Bank Name validation
        String bankName = banknameField.getText();
        if (bankName == null || bankName.isEmpty()) {
            errorMessage += "Bank name is required.\n";
        } else if (!bankName.matches("[a-zA-Z]{1,12}")) {
            errorMessage += "Bank name must be between 1 and 12 letters and contain only letters.\n";
        }

        // Address validation
        String address = adresseField.getText();
        if (address == null || address.isEmpty()) {
            errorMessage += "Address is required.\n";
        } else if (address.length() > 30) {
            errorMessage += "Address must be maximum 20 characters.\n";
        }

        // CodeSwift validation (assuming it's a code format)
        /*String codeSwift = codeswift.getText();
        if (codeSwift == null || codeSwift.isEmpty()) {
            errorMessage += "CodeSwift is required.\n";
        } else if (!codeSwift.matches("[a-zA-Z0-9]{8,11}")) {
            errorMessage += "CodeSwift must be between 8 and 11 characters and contain only uppercase letters and numbers.\n";
        }*/

        // Phone Number validation
        String phoneNumber = phonenum.getText();
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            errorMessage += "Phone number is required.\n";
        } else if (!phoneNumber.matches("\\+216\\d{10}")) {
            errorMessage += "Phone number must be 13 digits long.\n";
        }

        // Image validation
        String imagePath = imageField.getText();
        if (imagePath == null || imagePath.isEmpty() || !new File(imagePath).exists()) {
            errorMessage += "A valid image file must be selected.\n";
        }

        if (!errorMessage.isEmpty()) {
            showAlert("Validation Error", errorMessage);
            return false;
        }
        return true;
    }


    public void selectData(){

        Bank data = tableView.getSelectionModel().getSelectedItem();

        int num = tableView.getSelectionModel().getSelectedIndex();

        if((num-1) < -1)
            return;

        idField.setText(String.valueOf(data.getId()));
        banknameField.setText(data.getNom());
        adresseField.setText(data.getAdresse());
        //codeswift.setText(data.getCodeSwift());
        phonenum.setText(data.getPhoneNum());

        String picture ="file:" +  data.getLogo();

        Image image = new Image(picture, 110, 110, false, true);

        imageView.setImage(image);


    }

    private void loadTableData() {
        ObservableList<Bank> observableList = FXCollections.observableArrayList(b.findAll());
        tableView.setItems(observableList);
    }

    @FXML
    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imageField.setText(file.getAbsolutePath());
            imageView.setImage(new Image(file.toURI().toString()));
        }
    }



    @FXML
    private void insert() {
        if (validateInput()) {
            Bank ban = new Bank(
                    banknameField.getText(),
                    adresseField.getText(),
                    generateRandomSwiftCode(),
                    imageField.getText(),
                    phonenum.getText()
                    );
            if (!b.isBankExists(ban)) {
                Bank savedBank = b.save(ban);

                if (savedBank != null) {
                    tableView.getItems().add(savedBank);
                    clearFields();
                    showAlert("Success", "Bank added successfully.");
                } else {
                    showAlert("Error", "Failed to add the Bank.");
                }
            }
            else showAlert("Error", "Bank already exist.");
        }
    }

    @FXML
    private void update() {
        if (validateInput()) {
            Bank selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setNom(banknameField.getText());
                selected.setAdresse(adresseField.getText());
                selected.setCodeSwift(selected.getCodeSwift());
                //selected.setCodeSwift(codeswift.getText());
                selected.setPhoneNum(phonenum.getText());
                selected.setLogo(imageField.getText());
                if (!b.isBankExists(selected)) {
                    if (b.update(selected) != null) {
                        tableView.refresh();
                        showAlert("Success", "Bank updated successfully.");
                    } else {
                        showAlert("Error", "Failed to update bank.");
                    }
                } else {
                    showAlert("Error", "Bank already exist.");
                }
            } else {
                    showAlert("Error", "No bank selected.");
                }

        }
    }
    @FXML
    private void delete() {
        int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Bank selected = tableView.getItems().get(selectedIndex);
            b.deleteById(selected.getId());
            tableView.getItems().remove(selectedIndex);
            showAlert("Success", "Bank deleted successfully.");
        } else {
            showAlert("Error", "No Bank selected.");
        }
    }

    @FXML
    private void clearFields() {
        idField.setText("");
        banknameField.clear();
        adresseField.clear();
        //codeswift.clear();
        phonenum.clear();
        imageView.setImage(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void textfieldDesign(){

        if(idField.isFocused()){

            idField.setStyle("-fx-border-width:2px; -fx-background-color:#fff");
            banknameField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            adresseField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            //codeswift.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            phonenum.setStyle("-fx-border-width:1px; -fx-background-color:transparent");

        }else if(banknameField.isFocused()){

            idField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            banknameField.setStyle("-fx-border-width:2px; -fx-background-color:#fff");
            adresseField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            //codeswift.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            phonenum.setStyle("-fx-border-width:1px; -fx-background-color:transparent");

        }else if(adresseField.isFocused()){

            idField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            banknameField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            adresseField.setStyle("-fx-border-width:2px; -fx-background-color:#fff");
            //codeswift.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            phonenum.setStyle("-fx-border-width:1px; -fx-background-color:transparent");

        }/*else if(codeswift.isFocused()){

            idField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            banknameField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            adresseField.setStyle("-fx-border-width:1px; -fx-background-color:transparent");
            codeswift.setStyle("-fx-border-width:2px; -fx-background-color:#fff");
            phonenum.setStyle("-fx-border-width:2px; -fx-background-color:transparent");

        }*/

    }
    public static String generateRandomSwiftCode() {
        StringBuilder swiftCode = new StringBuilder();
        Random random = new Random();

        // Générer les 4 premiers caractères (lettre majuscule)
        for (int i = 0; i < 4; i++) {
            char randomChar = (char) (random.nextInt(26) + 'A');
            swiftCode.append(randomChar);
        }

        // Ajouter les caractères suivants (chiffres ou lettres majuscules)
        for (int i = 0; i < 6; i++) {
            if (random.nextBoolean()) {
                char randomChar = (char) (random.nextInt(10) + '0');
                swiftCode.append(randomChar);
            } else {
                char randomChar = (char) (random.nextInt(26) + 'A');
                swiftCode.append(randomChar);
            }
        }

        return swiftCode.toString();
    }

    public String findAndPrintDialCode(String countryName) {
        try {
            // Read the JSON file containing country data
            String jsonString = new String(Files.readAllBytes(Paths.get("\\Users\\gigab\\Desktop\\pidev-java\\src\\main\\resources\\CountryCodes.json")));

            // Parse the JSON string into a JSONArray
            JSONArray countries = new JSONArray(jsonString);

            // Search for the dial code of a country by name
            String dialCode = findDialCode(countries, countryName);
            if (dialCode != null) {
                return dialCode;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

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
    }
}



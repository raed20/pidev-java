/*package tests;

import com.google.maps.GeoApiContext;
import com.google.maps.PlaceAutocompleteRequest;
import com.google.maps.PlacesApi;
import com.google.maps.model.AutocompletePrediction;

import javax.mail.Session;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Initialize the API context with your API key
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyCYfsThRn_cPFBF_d1tZtYYh8ujGTV5ypc")
                .build();

        // Get user input for autocomplete query
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter autocomplete query: ");
        String input = scanner.nextLine();
        PlaceAutocompleteRequest.SessionToken sessionToken =new PlaceAutocompleteRequest.SessionToken();


        try {
            // Perform autocomplete request
            AutocompletePrediction[] autocompletePredictions = PlacesApi.placeAutocomplete(context, input,sessionToken).await();

            // Print autocomplete predictions
            for (AutocompletePrediction prediction : autocompletePredictions) {
                System.out.println(prediction.description);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}*/

package tests;

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
        }
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

package controllers;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CountryController {

    private static final String REST_COUNTRIES_API_URL = "https://restcountries.com/v3.1/name/";

    public List<String> getSuggestions(String input) throws IOException {
        List<String> suggestions = new ArrayList<>();
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(REST_COUNTRIES_API_URL + input);
        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");

        JSONArray jsonArray = new JSONArray(responseString);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject country = jsonArray.getJSONObject(i);
            suggestions.add(country.getJSONObject("name").getString("common"));
        }

        return suggestions;
    }
}

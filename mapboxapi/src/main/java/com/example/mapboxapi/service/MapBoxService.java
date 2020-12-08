package com.example.mapboxapi.service;

import com.example.mapboxapi.model.ResponseModel;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MapBoxService {
    private final String accessTokenSuffix = "access_token=pk.eyJ1IjoibWVwaGkwMDciLCJhIjoiY2tpZDR1bTM1MHFtczJycGVxbGpndW9lcSJ9.sDX7nF4iXJWmvORvG0dfmw";
    private final String uriPrefix = "https://api.mapbox.com/geocoding/v5/mapbox.places/";
    private final String limits = "limit=10&";
    private final String poi = "types=poi&";
    private final String searchfor ="historical%20museums%20palaces.json?";

    public List<ResponseModel> getItinerary(String country) throws IOException, InterruptedException, JSONException {
        List<String> bbox = getBboxCordinatesOfCountry(country);
        StringBuilder bboxUrl = new StringBuilder("bbox=");
        for(int i=0; i<bbox.size(); i++){
            bboxUrl.append(bbox.get(i));
            if(i+1<bbox.size()){
                bboxUrl.append(",");
            }
        }
        bboxUrl.append("&");
        String url = uriPrefix+searchfor+limits+poi+bboxUrl.toString()+accessTokenSuffix;
        JSONObject jsonResponseBody = getJsonResponse(url);
        List<ResponseModel> responseBody = new ArrayList();
        if(jsonResponseBody.has("features")){
            JSONArray jsonGetFeatures = jsonResponseBody.getJSONArray("features");
            for(int i=0; i<jsonGetFeatures.length(); i++) {
                ResponseModel resp = new ResponseModel();
                JSONObject json = jsonGetFeatures.getJSONObject(i);
                resp.setName(json.get("text").toString());
                JSONObject getProperties = json.getJSONObject("properties");
                resp.setCategories(Arrays.asList(getProperties.get("category").toString().split(", ")));
                JSONObject getRegion = (JSONObject) json.getJSONArray("context").get(0);
                resp.setRegion(getRegion.get("text").toString());
                responseBody.add(resp);
            }
        }
        return responseBody;
    }

    private List<String> getBboxCordinatesOfCountry(String country) throws IOException, InterruptedException, JSONException {
        String url = new StringBuilder(uriPrefix + country + ".json?" + accessTokenSuffix).toString();
        JSONObject jsonResponseBody = getJsonResponse(url);
        JSONObject jsonGetBBox = new JSONObject(jsonResponseBody.getJSONArray("features").get(0).toString());
        List<String> bbox = new ArrayList();
        if (!jsonResponseBody.has("errors")) {
            if (jsonGetBBox.has("bbox")) {
                JSONArray bboxfromJSON = jsonGetBBox.getJSONArray("bbox");
                for(int i=0; i<bboxfromJSON.length(); i++){
                    bbox.add(bboxfromJSON.get(i).toString());
                }

            }
        }
        return bbox;
    }

    private JSONObject getJsonResponse(String url) throws IOException, InterruptedException, JSONException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader jsonBodyReader = new StringReader(httpResponse.body());
        BufferedReader in = new BufferedReader(jsonBodyReader);
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return new JSONObject(response.toString());
    }
}

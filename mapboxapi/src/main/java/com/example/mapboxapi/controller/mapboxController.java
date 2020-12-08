package com.example.mapboxapi.controller;

import com.example.mapboxapi.model.ResponseModel;
import com.example.mapboxapi.service.MapBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class mapboxController {
    @Autowired
    MapBoxService mapBoxService;

    @GetMapping("/itinerary/{country}")
    public List<ResponseModel>getItinerary(@PathVariable String country) throws IOException, InterruptedException, JSONException {
        List<ResponseModel> response = mapBoxService.getItinerary(country);
        return response;
    }

    @GetMapping("/itinerary")
    public List<ResponseModel>getItinerary() throws IOException, InterruptedException, JSONException {
        List<ResponseModel> response = mapBoxService.getItinerary("istanbul");
        return response;
    }
}

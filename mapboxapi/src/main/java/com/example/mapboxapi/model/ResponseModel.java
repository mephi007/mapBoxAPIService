package com.example.mapboxapi.model;

import lombok.Data;

import java.util.List;

@Data
public class ResponseModel {
    private String name;
    private List<String> categories;
    private String region;

}

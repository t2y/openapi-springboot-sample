package com.example.springboot.configuration;

import org.springframework.http.MediaType;

public class ExtraMediaType {

    public static MediaType TEXT_TSV = new MediaType("text", "tab-separated-values");
    public static MediaType TEXT_CSV = new MediaType("text", "csv");
}

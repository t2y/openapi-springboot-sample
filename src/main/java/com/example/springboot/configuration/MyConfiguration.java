package com.example.springboot.configuration;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.val;

@Configuration
public class MyConfiguration {

    @Bean
    public HttpMessageConverters customConverters() {
        val usersTsvConverter = new UsersTsvConverter();
        return new HttpMessageConverters(usersTsvConverter);
    }
}

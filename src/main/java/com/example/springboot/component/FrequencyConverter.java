package com.example.springboot.component;

import org.openapitools.model.Frequency;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FrequencyConverter implements Converter<String, Frequency> {

    @Override
    public Frequency convert(String source) {
        log.info(source);
        try {
            return Frequency.fromValue(source);
        } catch (Exception e) {
            throw new RuntimeException("frequency is invalid");
        }
    }
}

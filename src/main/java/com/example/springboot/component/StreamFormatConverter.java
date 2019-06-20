package com.example.springboot.component;

import org.openapitools.model.StreamFormat;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StreamFormatConverter implements Converter<String, StreamFormat> {

    @Override
    public StreamFormat convert(String source) {
        try {
            return StreamFormat.fromValue(source);
        } catch (Exception e) {
            return StreamFormat.CSV;
        }
    }
}

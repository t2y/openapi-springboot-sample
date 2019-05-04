package com.example.springboot.configuration;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openapitools.model.User;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UsersTsvConverter extends AbstractHttpMessageConverter<List<User>> {

    private static final Set<Class<?>> SUPPORTED_CLASSES = new HashSet<>(8);

    static {
        SUPPORTED_CLASSES.add(new ArrayList<User>().getClass());
    }

    protected UsersTsvConverter() {
        super(ExtraMediaType.TEXT_TSV);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return SUPPORTED_CLASSES.contains(clazz);
    }

    @Override
    protected List<User> readInternal(Class<? extends List<User>> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    protected void writeInternal(List<User> t, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        log.info("writeInternal");
        val out = outputMessage.getBody();
        val writer = new PrintWriter(out);
        for (val user : t) {
            writer.print(user.getId() + "\t");
            writer.print(user.getUsername() + "\t");
            writer.print(user.getBirthday());
            writer.println();
        }
        writer.close();
        out.flush();
    }
}

package com.example.springboot.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openapitools.model.User;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

    private static final String DATA_PATH = "data.db";

    @Getter
    private final Map<Long, User> data = new HashMap<>();

    public UserService() throws IOException, URISyntaxException {
        this.initData();
        log.info("UserService is initialized");
    }

    private void initData() throws IOException, URISyntaxException {
        val url = this.getClass().getClassLoader().getResource(DATA_PATH);
        if (!url.toString().startsWith("file:")) {
            return;
        }

        val path = Paths.get(url.toURI());
        for (val line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
            val values = line.split(",");
            val id = Long.valueOf(values[0]);
            val username = values[1];
            val birthday = LocalDate.parse(values[2]);
            val user = new User().id(id).username(username).birthday(birthday);
            log.debug(user.toString());
            this.data.put(id, user);
        }
    }

    public List<User> getUsers() {
        return this.data.values().stream().collect(Collectors.toList());
    }

    public User getUser(Long id) {
        return this.data.get(id);
    }

    public boolean addUser(User user) {
        if (this.data.containsKey(user.getId())) {
            return false;

        }
        this.data.put(user.getId(), user);
        return true;
    }

    public boolean updateUser(Long id, User user) {
        this.data.put(id, user);
        return true;
    }

    public boolean deleteUser(Long id) {
        this.data.remove(id);
        return true;
    }
}

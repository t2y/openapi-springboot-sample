package com.example.springboot.api;

import java.util.List;

import javax.validation.Valid;

import org.openapitools.api.UsersApi;
import org.openapitools.model.Format;
import org.openapitools.model.InlineResponse201;
import org.openapitools.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.configuration.ExtraMediaType;
import com.example.springboot.service.UserService;

import io.swagger.annotations.ApiParam;
import lombok.val;
import lombok.var;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UsersApiController implements UsersApi {

    @Autowired
    private UserService userService;

    private HttpHeaders getHeaders(Format format) {
        var headers = new HttpHeaders();
        switch (format) {
        case TSV:
            headers.setContentDispositionFormData("attatchment", "test.tsv");
            headers.setContentType(ExtraMediaType.TEXT_TSV);
            break;
        case JSON:
            break;
        default:
            assert false;
        }
        return headers;
    }

    @Override
    public ResponseEntity<List<User>> getUsers(
            @ApiParam(value = "", defaultValue = "null") @Valid @RequestParam(value = "format", required = false, defaultValue = "null") Format format) {
        val auth = SecurityContextHolder.getContext().getAuthentication();
        log.info(auth.getAuthorities().toString());
        val users = this.userService.getUsers();
        val headers = this.getHeaders(format);
        return new ResponseEntity<>(users, headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<InlineResponse201> createUser(
            @ApiParam(value = "Created user object", required = true) @Valid @RequestBody User user) {
        var statusCode = HttpStatus.CREATED;
        val result = this.userService.addUser(user);
        if (!result) {
            statusCode = HttpStatus.CONFLICT;
        }
        val res = new InlineResponse201().id(user.getId());
        return new ResponseEntity<>(res, statusCode);
    }

    @Override
    public ResponseEntity<User> getUserById(
            @ApiParam(value = "The id that needs to be fetched. Use 1 for testing. ", required = true) @PathVariable("id") Long id) {
        var statusCode = HttpStatus.OK;
        val user = this.userService.getUser(id);
        if (user == null) {
            statusCode = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(user, statusCode);
    }

    @Override
    public ResponseEntity<Void> deleteUser(
            @ApiParam(value = "The name that needs to be deleted", required = true) @PathVariable("id") Long id) {
        this.userService.deleteUser(id);
        return null;
    }

    @Override
    public ResponseEntity<Void> updateUser(
            @ApiParam(value = "name that need to be updated", required = true) @PathVariable("id") Long id,
            @ApiParam(value = "Updated user object", required = true) @Valid @RequestBody User user) {
        user.setId(id);
        this.userService.updateUser(id, user);
        return null;
    }
}
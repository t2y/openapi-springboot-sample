package com.example.springboot.api;

import org.openapitools.api.PingApi;
import org.openapitools.model.InlineResponse200;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class PingApiController implements PingApi {

    @Override
    @ApiOperation(value = "", nickname = "getPing", notes = "Returns pong message", response = InlineResponse200.class, tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "pong message", response = InlineResponse200.class) })
    @RequestMapping(value = "/ping", produces = { "application/json" }, method = RequestMethod.GET)
    public ResponseEntity<InlineResponse200> getPing() {
        val authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(authentication.toString());
        val pong = new InlineResponse200().message("pong");
        return new ResponseEntity<>(pong, HttpStatus.OK);
    }
}

package com.example.springboot.api;

import java.io.ByteArrayInputStream;

import javax.validation.Valid;

import org.openapitools.api.StreamApi;
import org.openapitools.model.StreamFormat;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.configuration.ExtraMediaType;

import io.swagger.annotations.ApiParam;
import lombok.val;
import lombok.var;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class StreamApiController implements StreamApi {

    private HttpHeaders getHeader(StreamFormat format) {
        var headers = new HttpHeaders();
        switch (format) {
        case TSV:
            headers.setContentDispositionFormData("attatchment", "test.tsv");
            headers.setContentType(ExtraMediaType.TEXT_TSV);
            break;
        case CSV:
            headers.setContentDispositionFormData("attatchment", "test.csv");
            headers.setContentType(ExtraMediaType.TEXT_CSV);
            break;
        default:
            throw new IllegalStateException();
        }
        return headers;
    }

    public ResponseEntity<ByteArrayResource> getByte(
            @ApiParam(value = "", allowableValues = "tsv, csv", defaultValue = "csv") @Valid StreamFormat format) {
        log.info("getByte");
        val resource = new ByteArrayResource("test,example\n".getBytes());
        val headers = this.getHeader(format);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    public ResponseEntity<InputStreamResource> getInput(
            @ApiParam(value = "", allowableValues = "tsv, csv", defaultValue = "csv") @Valid StreamFormat format) {
        log.info("getInput");
        val byteInputStream = new ByteArrayInputStream("test,data\n".getBytes());
        val resource = new InputStreamResource(byteInputStream);
        val headers = this.getHeader(format);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}

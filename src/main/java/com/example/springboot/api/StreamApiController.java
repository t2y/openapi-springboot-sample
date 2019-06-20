package com.example.springboot.api;

import java.io.IOException;
import java.io.InputStream;

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

    class MyData extends InputStream {

        private static final int MAX_LOOP = 50000;

        private final byte[] DATA = "test,data\n".getBytes();
        private int index = 0;

        @Override
        public int read() throws IOException {
            log.info("read with no parameter called");
            return -1;
        }

        @Override
        public int read(byte[] data, int offset, int length) throws IOException {
            val message = String.format("read with 3 parameters, offset: %d, length: %d", offset, length);
            log.info(message);
            if (this.index > MAX_LOOP) {
                return -1;
            }
            int i = 0;
            while (i < length) {
                System.arraycopy(this.DATA, 0, data, offset, length);
                i += this.DATA.length;
            }
            this.index++;
            return data.length;
        }

        @Override
        public int read(byte[] data) throws IOException {
            log.info("read with buffered data");
            if (this.index > MAX_LOOP) {
                return -1;
            }
            System.arraycopy(this.DATA, 0, data, 0, this.DATA.length);
            this.index++;
            return this.DATA.length;
        }
    }

    public ResponseEntity<InputStreamResource> getInput(
            @ApiParam(value = "", allowableValues = "tsv, csv", defaultValue = "csv") @Valid StreamFormat format) {
        log.info("getInput");
        val resource = new InputStreamResource(new MyData());
        val headers = this.getHeader(format);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}

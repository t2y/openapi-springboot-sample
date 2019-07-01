package com.example.springboot.api;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
        case ZIP:
            headers.setContentDispositionFormData("attatchment", "test.zip");
            headers.setContentType(ExtraMediaType.APPLICATION_ZIP);
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
        private final StreamFormat format;
        private int index = 0;

        private ByteArrayOutputStream byteArrayOutputStream = null;
        private ZipOutputStream zipOutputStream = null;

        public MyData(StreamFormat format) {
            this.format = format;
            if (this.format == StreamFormat.ZIP) {
                this.byteArrayOutputStream = new ByteArrayOutputStream();
                val bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
                this.zipOutputStream = new ZipOutputStream(bufferedOutputStream);
                try {
                    this.zipOutputStream.putNextEntry(new ZipEntry("text.data"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

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

            switch (this.format) {
            case CSV:
            case TSV:
                System.arraycopy(this.DATA, 0, data, 0, this.DATA.length);
                this.index++;
                return this.DATA.length;
            case ZIP:
                this.zipOutputStream.write(this.DATA);
                this.zipOutputStream.flush();
                this.index++;
                if (this.index > MAX_LOOP) {
                    this.zipOutputStream.closeEntry();
                    this.zipOutputStream.close();
                }
                val out = this.byteArrayOutputStream.toByteArray();
                System.arraycopy(out, 0, data, 0, out.length);
                return out.length;
            default:
                throw new UnsupportedOperationException();
            }
        }
    }

    public ResponseEntity<InputStreamResource> getInput(
            @ApiParam(value = "", allowableValues = "tsv, csv, zip", defaultValue = "csv") @Valid StreamFormat format) {
        log.info("getInput");
        val resource = new InputStreamResource(new MyData(format));
        val headers = this.getHeader(format);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}

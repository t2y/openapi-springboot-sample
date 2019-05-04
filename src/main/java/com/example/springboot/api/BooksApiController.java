package com.example.springboot.api;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.openapitools.api.BooksApi;
import org.openapitools.model.Book;
import org.openapitools.model.Format;
import org.openapitools.model.Frequency;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.exception.MyBadRequestException;

import io.swagger.annotations.ApiParam;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class BooksApiController implements BooksApi {

    @Override
    public ResponseEntity<Void> createBook(
            @ApiParam(value = "Created book object", required = true) @Valid @RequestBody Book book) {
        log.info("called createdBook: " + book.toString());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Book> getBookById(
            @ApiParam(value = "The id that needs to be fetched. Use 1 for testing. ", required = true) @PathVariable("id") Integer id) {
        log.info("called getBookById: " + String.valueOf(id));
        val book = new Book().id(id.longValue());
        return new ResponseEntity<Book>(book, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Book>> getBooks(
            @ApiParam(value = "", required = true, defaultValue = "null") @PathVariable("frequency") Frequency frequency,
            @ApiParam(value = "") @Valid @RequestParam(value = "offset", required = false) Integer offset,
            @ApiParam(value = "") @Valid @RequestParam(value = "limit", required = false) Integer limit,
            @ApiParam(value = "", defaultValue = "null") @Valid @RequestParam(value = "format", required = false, defaultValue = "null") Format format) {
        log.info("called : getBooks");
        log.info(frequency.toString());
        if (format != null) {
            log.info(format.toString());
        }
        if (frequency.toString() == "yearly") {
            throw new MyBadRequestException("exception handling test");
        }
        List<Book> books = Collections.emptyList();
        return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
    }
}

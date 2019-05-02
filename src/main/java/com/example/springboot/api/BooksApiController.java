package com.example.springboot.api;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.openapitools.api.BooksApi;
import org.openapitools.model.Book;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class BooksApiController implements BooksApi {

    @Override
    @ApiOperation(value = "Create book", nickname = "createBook", notes = "This can only be done by the logged in user.", tags = {
            "books", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation") })
    @RequestMapping(value = "/books/create", consumes = { "application/json" }, method = RequestMethod.POST)
    public ResponseEntity<Void> createBook(
            @ApiParam(value = "Created book object", required = true) @Valid @RequestBody Book book) {
        log.info("called createdBook: " + book.toString());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Override
    @ApiOperation(value = "Get book by book id", nickname = "getBookById", notes = "", response = Book.class, tags = {
            "books", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Book.class),
            @ApiResponse(code = 400, message = "Invalid username supplied"),
            @ApiResponse(code = 404, message = "User not found") })
    @RequestMapping(value = "/books/{id}", produces = { "application/json" }, method = RequestMethod.GET)
    public ResponseEntity<Book> getBookById(
            @ApiParam(value = "The id that needs to be fetched. Use 1 for testing. ", required = true) @PathVariable("id") Integer id) {
        log.info("called getBookById: " + String.valueOf(id));
        val book = new Book().id(id.longValue());
        return new ResponseEntity<Book>(book, HttpStatus.OK);
    }

    @Override
    @ApiOperation(value = "", nickname = "getBooks", notes = "Returns a list of books", response = Book.class, responseContainer = "List", tags = {})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully returned a list of books", response = Book.class, responseContainer = "List") })
    @RequestMapping(value = "/books/list", produces = { "application/json" }, method = RequestMethod.GET)
    public ResponseEntity<List<Book>> getBooks() {
        log.info("called : getBooks");
        List<Book> books = Collections.emptyList();
        return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
    }
}

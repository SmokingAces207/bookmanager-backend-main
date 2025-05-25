package com.acme.bookmanagement.controller;

import com.acme.bookmanagement.model.BookModel;
import com.acme.bookmanagement.service.BookService;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/graphql")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @QueryMapping
    public List<BookModel> findAllBooks() {
        return bookService.findAll();
    }

    @QueryMapping
    public BookModel findBookById(@Argument("id") Long id) {
        return bookService.findById(id);
    }

    @QueryMapping
    public List<BookModel> findBooksByDateRange(@Argument LocalDate startDate, @Argument LocalDate endDate) {
        return bookService.findBooksByDateRange(startDate, endDate);
    }

    @MutationMapping
    public BookModel createBook(@Argument("book") BookModel bookModel) {
        return bookService.create(bookModel);
    }

    @MutationMapping
    public String deleteBook(@Argument("id") Long id) {
        return bookService.delete(id);
    }
}

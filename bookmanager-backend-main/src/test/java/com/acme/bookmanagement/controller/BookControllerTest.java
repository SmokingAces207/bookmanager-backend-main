package com.acme.bookmanagement.controller;

import com.acme.bookmanagement.model.BookModel;
import com.acme.bookmanagement.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@GraphQlTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockBean
    private BookService bookService;

    private final BookModel book1 = new BookModel(1L, "title-1", "author-1", LocalDate.of(2021, 2, 3));
    private final BookModel book2 = new BookModel(2L, "title-2", "author-2", LocalDate.of(2021, 2, 5));

    @Test
    void shouldGetAllBooks() {
        when(bookService.findAll()).thenReturn(List.of(book1, book2));

        graphQlTester.documentName("findAllBooks")
                .execute()
                .path("findAllBooks")
                .entityList(BookModel.class)
                .hasSize(2)
                .contains(book1, book2);
    }

    @Test
    void shouldFindBookById() {
        when(bookService.findById(1L)).thenReturn(book1);

        graphQlTester.documentName("findBookById")
                .variable("id", 1L)
                .execute()
                .path("findBookById")
                .entity(BookModel.class)
                .isEqualTo(book1);
    }

    @Test
    void shouldFindBooksByDateRange() {
        when(bookService.findBooksByDateRange(any(LocalDate.class), any(LocalDate.class))).thenReturn(List.of(book1));

        graphQlTester.documentName("findBooksByDateRange")
                .variable("startDate", "2021-02-03")
                .variable("endDate", "2021-02-04")
                .execute()
                .path("findBooksByDateRange")
                .entityList(BookModel.class)
                .hasSize(1)
                .contains(book1);
    }

    @Test
    void shouldCreateBook() {
        Map<String, Object> bookInput = Map.of(
                "title", "new title",
                "author", "new author",
                "publishedDate", "2022-01-01"
        );
        BookModel createdBook =
                new BookModel(3L, "new title", "new author", LocalDate.of(2022, 1, 1));

        when(bookService.create(any(BookModel.class))).thenReturn(createdBook);

        graphQlTester.documentName("createBook")
                .variable("book", bookInput)
                .execute()
                .path("createBook")
                .entity(BookModel.class)
                .isEqualTo(createdBook);
    }

    @Test
    void shouldDeleteBook() {
        when(bookService.delete(1L)).thenReturn("Deleted book with id 1");

        graphQlTester.documentName("deleteBook")
                .variable("id", 1L)
                .execute()
                .path("deleteBook")
                .entity(String.class)
                .isEqualTo("Deleted book with id 1");
    }
}
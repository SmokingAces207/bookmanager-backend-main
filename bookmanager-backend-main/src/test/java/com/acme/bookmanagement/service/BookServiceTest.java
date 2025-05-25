package com.acme.bookmanagement.service;

import com.acme.bookmanagement.entity.AuthorEntity;
import com.acme.bookmanagement.entity.BookEntity;
import com.acme.bookmanagement.model.BookModel;
import com.acme.bookmanagement.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    private final BookRepository bookRepository = mock(BookRepository.class);
    private final BookService bookService = new BookService(bookRepository);

    private final BookEntity book1 = new BookEntity(
            1L, "title-1", LocalDate.of(2021, 2, 3), new AuthorEntity(null, "author-1"));

    private final BookEntity book2 = new BookEntity(
            2L, "title-2", LocalDate.of(2021, 2, 4), new AuthorEntity(null, "author-2"));

    private final BookEntity book3 = new BookEntity(
            3L, "title-3", LocalDate.of(2021, 5, 10), new AuthorEntity(null, "author-3"));

    @Test
    void testFindAll() {
        when(bookRepository.findAll()).thenReturn(List.of(book1, book2, book3));
        List<BookModel> result = bookService.findAll();
        assertEquals(3, result.size());
        assertEquals("title-1", result.get(0).getTitle());
        assertEquals("title-2", result.get(1).getTitle());
        assertEquals("title-3", result.get(2).getTitle());

        assertResultModel(result.get(0));
    }

    @Test
    void testFindById_found() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        BookModel result = bookService.findById(1L);
        assertNotNull(result);

        assertResultModel(result);
    }

    @Test
    void testFindById_notFound() {
        when(bookRepository.findById(4L)).thenReturn(Optional.empty());
        BookModel result = bookService.findById(4L);
        assertNull(result);
    }

    @Test
    void testFindBooksByDateRange() {
        LocalDate start = LocalDate.of(2021, 2, 1);
        LocalDate end = LocalDate.of(2021, 2, 5);

        when(bookRepository.findAllByPublishedDateBetween(start, end)).thenReturn(List.of(book1, book2));
        List<BookModel> result = bookService.findBooksByDateRange(start, end);

        assertEquals(1, result.size());
        assertResultModel(result.get(0));
    }

    @Test
    void testCreate_validBook() {
        BookModel input = new BookModel(null, "title-1", "author-1", LocalDate.of(2021, 2, 3));
        when(bookRepository.save(any(BookEntity.class))).thenReturn(book1);

        BookModel result = bookService.create(input);
        assertNotNull(result);
        assertResultModel(result);
    }

    @Test
    void testCreate_invalidBook_throwsException() {
        BookModel invalid = new BookModel(null, "", "", null);
        assertThrows(InvalidParameterException.class, () -> bookService.create(invalid));
    }

    @Test
    void testDelete_existingBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        doNothing().when(bookRepository).delete(book1);

        String message = bookService.delete(1L);
        assertEquals("Book successfully deleted, with id: 1", message);
    }

    @Test
    void testDelete_nonExistingBook_throwsException() {
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> bookService.delete(2L));
    }

    @Test
    void testToModel() {
        BookModel result = bookService.toModel(book1);
        assertEquals("title-1", result.getTitle());
        assertEquals("author-1", result.getAuthor());
    }

    @Test
    void testToEntity() {
        BookModel model = new BookModel(1L, "title-1", "author-1", LocalDate.of(2021, 2, 3));
        BookEntity result = bookService.toEntity(model);

        assertEquals("title-1", result.getTitle());
        assertEquals("author-1", result.getAuthorEntity().getName());
    }

    private void assertResultModel(BookModel result) {
        assertEquals(1L, result.getId());
        assertEquals("title-1", result.getTitle());
        assertEquals(LocalDate.of(2021, 2, 3), result.getPublishedDate());
        assertEquals("author-1", result.getAuthor());
    }
}

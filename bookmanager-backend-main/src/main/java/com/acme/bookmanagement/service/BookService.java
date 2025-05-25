package com.acme.bookmanagement.service;

import com.acme.bookmanagement.entity.AuthorEntity;
import com.acme.bookmanagement.entity.BookEntity;
import com.acme.bookmanagement.model.BookModel;
import com.acme.bookmanagement.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookModel> findAll() {
        return bookRepository.findAll().stream().map(this::toModel).toList();
    }

    public BookModel findById(Long id) {
        Optional<BookEntity> bookEntity = bookRepository.findById(id);
        return bookEntity.map(this::toModel).orElse(null);
    }

    public List<BookModel> findBooksByDateRange(LocalDate startDate, LocalDate endDate) {
        return bookRepository.findAllByPublishedDateBetween(startDate, endDate)
                .stream()
                .map(this::toModel)
                .toList();
    }

    public BookModel create(BookModel bookModel) {
        if (bookModel.getAuthor().isBlank()
                || bookModel.getTitle().isBlank()
                || bookModel.getPublishedDate() == null) {
            throw new InvalidParameterException("Title, Author and Published Date must all be populated");
        }
        return toModel(bookRepository.save(toEntity(bookModel)));
    }

    public String delete(Long id) {
        Optional<BookEntity> bookEntity = bookRepository.findById(id);
        if (bookEntity.isEmpty()) {
            throw new EntityNotFoundException("No entity found with the following id: " + id);
        } else {
            bookRepository.delete(bookEntity.get());
            return "Book successfully deleted, with id: " + bookEntity.get().getId();
        }
    }

    public BookEntity toEntity(BookModel bookModel) {
        // Could use objectMapper
        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(bookModel.getId());
        bookEntity.setTitle(bookModel.getTitle());
        bookEntity.setPublishedDate(bookModel.getPublishedDate());

        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setName(bookModel.getAuthor());

        bookEntity.setAuthorEntity(authorEntity);
        return bookEntity;
    }

    public BookModel toModel(BookEntity bookEntity) {
        // Could use objectMapper
        BookModel bookModel = new BookModel();
        bookModel.setId(bookEntity.getId());
        bookModel.setTitle(bookEntity.getTitle());
        bookModel.setPublishedDate(bookEntity.getPublishedDate());
        bookModel.setAuthor(bookEntity.getAuthorEntity().getName());
        return bookModel;
    }
}


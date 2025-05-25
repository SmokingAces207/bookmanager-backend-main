package com.acme.bookmanagement.repository;

import com.acme.bookmanagement.entity.AuthorEntity;
import com.acme.bookmanagement.entity.BookEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class BookRepositoryTest {

    @Autowired
    private BookRepository repo;

    private BookEntity bookEntity;

    @BeforeEach
    public void setUp() {
        bookEntity = new BookEntity(null,
                "title-1",
                LocalDate.of(2021, 2, 3),
                new AuthorEntity(null, "author-1"));
        bookEntity = repo.save(bookEntity);
    }

    @AfterEach
    public void tearDown() {
        repo.delete(bookEntity);
    }

    @Test
    void testSavedBookCanBeFoundById() {
        BookEntity savedBookEntity = repo.findById(bookEntity.getId()).orElse(null);

        assertNotNull(savedBookEntity);
        assertEquals(bookEntity.getId(), savedBookEntity.getId());
        assertEquals(bookEntity.getAuthorEntity(), savedBookEntity.getAuthorEntity());
        assertEquals(bookEntity.getTitle(), savedBookEntity.getTitle());
        assertEquals(bookEntity.getPublishedDate(), savedBookEntity.getPublishedDate());
    }
}

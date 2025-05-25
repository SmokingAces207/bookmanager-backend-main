package com.acme.bookmanagement.repository;

import com.acme.bookmanagement.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, Long> {
    List<BookEntity> findAllByPublishedDateBetween(LocalDate startDate, LocalDate endDate);
}

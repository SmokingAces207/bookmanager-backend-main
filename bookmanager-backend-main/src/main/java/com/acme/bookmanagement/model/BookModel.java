package com.acme.bookmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookModel {
    private Long id;
    private String title;
    private String author;
    private LocalDate publishedDate;
}

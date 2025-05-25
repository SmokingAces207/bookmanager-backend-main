package com.acme.bookmanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book")
public class BookEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private LocalDate publishedDate;

    @ManyToOne(cascade = CascadeType.ALL) //Automatically persist new authors with new books
    @JoinColumn(name = "author_id")
    private AuthorEntity authorEntity;

}

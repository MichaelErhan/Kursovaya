package com.example.cargo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Entity
@Table(name = "blog_posts")
public class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private String title;

    @Getter
    private LocalDate publicationDate;

    @Getter
    private String content;

    @Getter
    private String specialist;

    @Getter
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // Конструкторы
    public BlogPost() {}

    public Long getId() {
        return id;
    }
}

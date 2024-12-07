package com.example.cargo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Entity
@Table(name = "clients")
public class Clients {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private String FIO;

    @Getter
    private String BrandNumberAuto;

    @Getter
    private String Number;

    // Конструкторы
    public Clients() {}

    public Long getId() {
        return id;
    }
}

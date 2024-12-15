package com.example.cargo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Entity
@Table(name = "cargo")
public class CargoModel {
    private Long id;

    @Getter
    private String cargoName;

    @Getter
    private String cargoContents;

    @Getter
    private String departureCity;//

    @Getter
    private LocalDate departureDate;

    @Getter
    private String arrivalCity;//

    @Getter
    private LocalDate arrivalDate;

    // Конструкторы
    public CargoModel() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

}

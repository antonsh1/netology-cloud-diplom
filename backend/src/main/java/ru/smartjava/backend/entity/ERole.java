package ru.smartjava.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
public class ERole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EnumRole name;

    public String getName() {
        return name.toString();
    }
}

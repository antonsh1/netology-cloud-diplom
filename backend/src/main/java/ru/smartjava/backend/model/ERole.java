package ru.smartjava.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@Entity
public class ERole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Size(max = 100)
    private EnumRole name;

    public String getName() {
        return name.toString();
    }
}

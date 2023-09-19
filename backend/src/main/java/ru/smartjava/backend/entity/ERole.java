package ru.smartjava.backend.entity;

import jakarta.persistence.*;

@Entity
public class ERole {

    //    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
    @Id
//    @UniqueElements
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EnumRole name;

    public String getName() {
        return name.toString();
    }
}

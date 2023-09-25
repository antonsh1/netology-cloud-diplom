package ru.smartjava.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class ERole {

    private enum EnumRole {
        UPLOAD,
        DOWNLOAD,
        RENAME,
        DELETE

    }
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    Long id;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EnumRole name;

    public String getName() {
        return name.toString();
    }
}
